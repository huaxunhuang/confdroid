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
 * Information about a storage volume that may be mounted. A volume may be a
 * partition on a physical {@link DiskInfo}, an emulated volume above some other
 * storage medium, or a standalone container like an ASEC or OBB.
 * <p>
 * Volumes may be mounted with various flags:
 * <ul>
 * <li>{@link #MOUNT_FLAG_PRIMARY} means the volume provides primary external
 * storage, historically found at {@code /sdcard}.
 * <li>{@link #MOUNT_FLAG_VISIBLE} means the volume is visible to third-party
 * apps for direct filesystem access. The system should send out relevant
 * storage broadcasts and index any media on visible volumes. Visible volumes
 * are considered a more stable part of the device, which is why we take the
 * time to index them. In particular, transient volumes like USB OTG devices
 * <em>should not</em> be marked as visible; their contents should be surfaced
 * to apps through the Storage Access Framework.
 * </ul>
 *
 * @unknown 
 */
public class VolumeInfo implements android.os.Parcelable {
    public static final java.lang.String ACTION_VOLUME_STATE_CHANGED = "android.os.storage.action.VOLUME_STATE_CHANGED";

    public static final java.lang.String EXTRA_VOLUME_ID = "android.os.storage.extra.VOLUME_ID";

    public static final java.lang.String EXTRA_VOLUME_STATE = "android.os.storage.extra.VOLUME_STATE";

    /**
     * Stub volume representing internal private storage
     */
    public static final java.lang.String ID_PRIVATE_INTERNAL = "private";

    /**
     * Real volume representing internal emulated storage
     */
    public static final java.lang.String ID_EMULATED_INTERNAL = "emulated";

    public static final int TYPE_PUBLIC = 0;

    public static final int TYPE_PRIVATE = 1;

    public static final int TYPE_EMULATED = 2;

    public static final int TYPE_ASEC = 3;

    public static final int TYPE_OBB = 4;

    public static final int STATE_UNMOUNTED = 0;

    public static final int STATE_CHECKING = 1;

    public static final int STATE_MOUNTED = 2;

    public static final int STATE_MOUNTED_READ_ONLY = 3;

    public static final int STATE_FORMATTING = 4;

    public static final int STATE_EJECTING = 5;

    public static final int STATE_UNMOUNTABLE = 6;

    public static final int STATE_REMOVED = 7;

    public static final int STATE_BAD_REMOVAL = 8;

    public static final int MOUNT_FLAG_PRIMARY = 1 << 0;

    public static final int MOUNT_FLAG_VISIBLE = 1 << 1;

    private static android.util.SparseArray<java.lang.String> sStateToEnvironment = new android.util.SparseArray<>();

    private static android.util.ArrayMap<java.lang.String, java.lang.String> sEnvironmentToBroadcast = new android.util.ArrayMap<>();

    private static android.util.SparseIntArray sStateToDescrip = new android.util.SparseIntArray();

    private static final java.util.Comparator<android.os.storage.VolumeInfo> sDescriptionComparator = new java.util.Comparator<android.os.storage.VolumeInfo>() {
        @java.lang.Override
        public int compare(android.os.storage.VolumeInfo lhs, android.os.storage.VolumeInfo rhs) {
            if (android.os.storage.VolumeInfo.ID_PRIVATE_INTERNAL.equals(lhs.getId())) {
                return -1;
            } else
                if (lhs.getDescription() == null) {
                    return 1;
                } else
                    if (rhs.getDescription() == null) {
                        return -1;
                    } else {
                        return lhs.getDescription().compareTo(rhs.getDescription());
                    }


        }
    };

    static {
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_UNMOUNTED, android.os.Environment.MEDIA_UNMOUNTED);
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_CHECKING, android.os.Environment.MEDIA_CHECKING);
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_MOUNTED, android.os.Environment.MEDIA_MOUNTED);
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_MOUNTED_READ_ONLY, android.os.Environment.MEDIA_MOUNTED_READ_ONLY);
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_FORMATTING, android.os.Environment.MEDIA_UNMOUNTED);
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_EJECTING, android.os.Environment.MEDIA_EJECTING);
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_UNMOUNTABLE, android.os.Environment.MEDIA_UNMOUNTABLE);
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_REMOVED, android.os.Environment.MEDIA_REMOVED);
        android.os.storage.VolumeInfo.sStateToEnvironment.put(android.os.storage.VolumeInfo.STATE_BAD_REMOVAL, android.os.Environment.MEDIA_BAD_REMOVAL);
        android.os.storage.VolumeInfo.sEnvironmentToBroadcast.put(android.os.Environment.MEDIA_UNMOUNTED, android.content.Intent.ACTION_MEDIA_UNMOUNTED);
        android.os.storage.VolumeInfo.sEnvironmentToBroadcast.put(android.os.Environment.MEDIA_CHECKING, android.content.Intent.ACTION_MEDIA_CHECKING);
        android.os.storage.VolumeInfo.sEnvironmentToBroadcast.put(android.os.Environment.MEDIA_MOUNTED, android.content.Intent.ACTION_MEDIA_MOUNTED);
        android.os.storage.VolumeInfo.sEnvironmentToBroadcast.put(android.os.Environment.MEDIA_MOUNTED_READ_ONLY, android.content.Intent.ACTION_MEDIA_MOUNTED);
        android.os.storage.VolumeInfo.sEnvironmentToBroadcast.put(android.os.Environment.MEDIA_EJECTING, android.content.Intent.ACTION_MEDIA_EJECT);
        android.os.storage.VolumeInfo.sEnvironmentToBroadcast.put(android.os.Environment.MEDIA_UNMOUNTABLE, android.content.Intent.ACTION_MEDIA_UNMOUNTABLE);
        android.os.storage.VolumeInfo.sEnvironmentToBroadcast.put(android.os.Environment.MEDIA_REMOVED, android.content.Intent.ACTION_MEDIA_REMOVED);
        android.os.storage.VolumeInfo.sEnvironmentToBroadcast.put(android.os.Environment.MEDIA_BAD_REMOVAL, android.content.Intent.ACTION_MEDIA_BAD_REMOVAL);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_UNMOUNTED, R.string.ext_media_status_unmounted);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_CHECKING, R.string.ext_media_status_checking);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_MOUNTED, R.string.ext_media_status_mounted);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_MOUNTED_READ_ONLY, R.string.ext_media_status_mounted_ro);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_FORMATTING, R.string.ext_media_status_formatting);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_EJECTING, R.string.ext_media_status_ejecting);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_UNMOUNTABLE, R.string.ext_media_status_unmountable);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_REMOVED, R.string.ext_media_status_removed);
        android.os.storage.VolumeInfo.sStateToDescrip.put(android.os.storage.VolumeInfo.STATE_BAD_REMOVAL, R.string.ext_media_status_bad_removal);
    }

    /**
     * vold state
     */
    public final java.lang.String id;

    public final int type;

    public final android.os.storage.DiskInfo disk;

    public final java.lang.String partGuid;

    public int mountFlags = 0;

    public int mountUserId = -1;

    public int state = android.os.storage.VolumeInfo.STATE_UNMOUNTED;

    public java.lang.String fsType;

    public java.lang.String fsUuid;

    public java.lang.String fsLabel;

    public java.lang.String path;

    public java.lang.String internalPath;

    public VolumeInfo(java.lang.String id, int type, android.os.storage.DiskInfo disk, java.lang.String partGuid) {
        this.id = com.android.internal.util.Preconditions.checkNotNull(id);
        this.type = type;
        this.disk = disk;
        this.partGuid = partGuid;
    }

    public VolumeInfo(android.os.Parcel parcel) {
        id = parcel.readString();
        type = parcel.readInt();
        if (parcel.readInt() != 0) {
            disk = android.os.storage.DiskInfo.CREATOR.createFromParcel(parcel);
        } else {
            disk = null;
        }
        partGuid = parcel.readString();
        mountFlags = parcel.readInt();
        mountUserId = parcel.readInt();
        state = parcel.readInt();
        fsType = parcel.readString();
        fsUuid = parcel.readString();
        fsLabel = parcel.readString();
        path = parcel.readString();
        internalPath = parcel.readString();
    }

    @android.annotation.NonNull
    public static java.lang.String getEnvironmentForState(int state) {
        final java.lang.String envState = android.os.storage.VolumeInfo.sStateToEnvironment.get(state);
        if (envState != null) {
            return envState;
        } else {
            return android.os.Environment.MEDIA_UNKNOWN;
        }
    }

    @android.annotation.Nullable
    public static java.lang.String getBroadcastForEnvironment(java.lang.String envState) {
        return android.os.storage.VolumeInfo.sEnvironmentToBroadcast.get(envState);
    }

    @android.annotation.Nullable
    public static java.lang.String getBroadcastForState(int state) {
        return android.os.storage.VolumeInfo.getBroadcastForEnvironment(android.os.storage.VolumeInfo.getEnvironmentForState(state));
    }

    @android.annotation.NonNull
    public static java.util.Comparator<android.os.storage.VolumeInfo> getDescriptionComparator() {
        return android.os.storage.VolumeInfo.sDescriptionComparator;
    }

    @android.annotation.NonNull
    public java.lang.String getId() {
        return id;
    }

    @android.annotation.Nullable
    public android.os.storage.DiskInfo getDisk() {
        return disk;
    }

    @android.annotation.Nullable
    public java.lang.String getDiskId() {
        return disk != null ? disk.id : null;
    }

    public int getType() {
        return type;
    }

    public int getState() {
        return state;
    }

    public int getStateDescription() {
        return android.os.storage.VolumeInfo.sStateToDescrip.get(state, 0);
    }

    @android.annotation.Nullable
    public java.lang.String getFsUuid() {
        return fsUuid;
    }

    public int getMountUserId() {
        return mountUserId;
    }

    @android.annotation.Nullable
    public java.lang.String getDescription() {
        if (android.os.storage.VolumeInfo.ID_PRIVATE_INTERNAL.equals(id) || android.os.storage.VolumeInfo.ID_EMULATED_INTERNAL.equals(id)) {
            return android.content.res.Resources.getSystem().getString(com.android.internal.R.string.storage_internal);
        } else
            if (!android.text.TextUtils.isEmpty(fsLabel)) {
                return fsLabel;
            } else {
                return null;
            }

    }

    public boolean isMountedReadable() {
        return (state == android.os.storage.VolumeInfo.STATE_MOUNTED) || (state == android.os.storage.VolumeInfo.STATE_MOUNTED_READ_ONLY);
    }

    public boolean isMountedWritable() {
        return state == android.os.storage.VolumeInfo.STATE_MOUNTED;
    }

    public boolean isPrimary() {
        return (mountFlags & android.os.storage.VolumeInfo.MOUNT_FLAG_PRIMARY) != 0;
    }

    public boolean isPrimaryPhysical() {
        return isPrimary() && (getType() == android.os.storage.VolumeInfo.TYPE_PUBLIC);
    }

    public boolean isVisible() {
        return (mountFlags & android.os.storage.VolumeInfo.MOUNT_FLAG_VISIBLE) != 0;
    }

    public boolean isVisibleForRead(int userId) {
        if (type == android.os.storage.VolumeInfo.TYPE_PUBLIC) {
            if (isPrimary() && (mountUserId != userId)) {
                // Primary physical is only visible to single user
                return false;
            } else {
                return isVisible();
            }
        } else
            if (type == android.os.storage.VolumeInfo.TYPE_EMULATED) {
                return isVisible();
            } else {
                return false;
            }

    }

    public boolean isVisibleForWrite(int userId) {
        if ((type == android.os.storage.VolumeInfo.TYPE_PUBLIC) && (mountUserId == userId)) {
            return isVisible();
        } else
            if (type == android.os.storage.VolumeInfo.TYPE_EMULATED) {
                return isVisible();
            } else {
                return false;
            }

    }

    public java.io.File getPath() {
        return path != null ? new java.io.File(path) : null;
    }

    public java.io.File getInternalPath() {
        return internalPath != null ? new java.io.File(internalPath) : null;
    }

    public java.io.File getPathForUser(int userId) {
        if (path == null) {
            return null;
        } else
            if (type == android.os.storage.VolumeInfo.TYPE_PUBLIC) {
                return new java.io.File(path);
            } else
                if (type == android.os.storage.VolumeInfo.TYPE_EMULATED) {
                    return new java.io.File(path, java.lang.Integer.toString(userId));
                } else {
                    return null;
                }


    }

    /**
     * Path which is accessible to apps holding
     * {@link android.Manifest.permission#WRITE_MEDIA_STORAGE}.
     */
    public java.io.File getInternalPathForUser(int userId) {
        if (type == android.os.storage.VolumeInfo.TYPE_PUBLIC) {
            // TODO: plumb through cleaner path from vold
            return new java.io.File(path.replace("/storage/", "/mnt/media_rw/"));
        } else {
            return getPathForUser(userId);
        }
    }

    public android.os.storage.StorageVolume buildStorageVolume(android.content.Context context, int userId, boolean reportUnmounted) {
        final android.os.storage.StorageManager storage = context.getSystemService(android.os.storage.StorageManager.class);
        final boolean removable;
        final boolean emulated;
        final boolean allowMassStorage = false;
        final java.lang.String envState = (reportUnmounted) ? android.os.Environment.MEDIA_UNMOUNTED : android.os.storage.VolumeInfo.getEnvironmentForState(state);
        java.io.File userPath = getPathForUser(userId);
        if (userPath == null) {
            userPath = new java.io.File("/dev/null");
        }
        java.lang.String description = null;
        java.lang.String derivedFsUuid = fsUuid;
        long mtpReserveSize = 0;
        long maxFileSize = 0;
        int mtpStorageId = android.os.storage.StorageVolume.STORAGE_ID_INVALID;
        if (type == android.os.storage.VolumeInfo.TYPE_EMULATED) {
            emulated = true;
            final android.os.storage.VolumeInfo privateVol = storage.findPrivateForEmulated(this);
            if (privateVol != null) {
                description = storage.getBestVolumeDescription(privateVol);
                derivedFsUuid = privateVol.fsUuid;
            }
            if (isPrimary()) {
                mtpStorageId = android.os.storage.StorageVolume.STORAGE_ID_PRIMARY;
            }
            mtpReserveSize = storage.getStorageLowBytes(userPath);
            if (android.os.storage.VolumeInfo.ID_EMULATED_INTERNAL.equals(id)) {
                removable = false;
            } else {
                removable = true;
            }
        } else
            if (type == android.os.storage.VolumeInfo.TYPE_PUBLIC) {
                emulated = false;
                removable = true;
                description = storage.getBestVolumeDescription(this);
                if (isPrimary()) {
                    mtpStorageId = android.os.storage.StorageVolume.STORAGE_ID_PRIMARY;
                } else {
                    // Since MediaProvider currently persists this value, we need a
                    // value that is stable over time.
                    mtpStorageId = android.os.storage.VolumeInfo.buildStableMtpStorageId(fsUuid);
                }
                if ("vfat".equals(fsType)) {
                    maxFileSize = 4294967295L;
                }
            } else {
                throw new java.lang.IllegalStateException("Unexpected volume type " + type);
            }

        if (description == null) {
            description = context.getString(android.R.string.unknownName);
        }
        return new android.os.storage.StorageVolume(id, mtpStorageId, userPath, description, isPrimary(), removable, emulated, mtpReserveSize, allowMassStorage, maxFileSize, new android.os.UserHandle(userId), derivedFsUuid, envState);
    }

    public static int buildStableMtpStorageId(java.lang.String fsUuid) {
        if (android.text.TextUtils.isEmpty(fsUuid)) {
            return android.os.storage.StorageVolume.STORAGE_ID_INVALID;
        } else {
            int hash = 0;
            for (int i = 0; i < fsUuid.length(); ++i) {
                hash = (31 * hash) + fsUuid.charAt(i);
            }
            hash = (hash ^ (hash << 16)) & 0xffff0000;
            // Work around values that the spec doesn't allow, or that we've
            // reserved for primary
            if (hash == 0x0)
                hash = 0x20000;

            if (hash == 0x10000)
                hash = 0x20000;

            if (hash == 0xffff0000)
                hash = 0xfffe0000;

            return hash | 0x1;
        }
    }

    // TODO: avoid this layering violation
    private static final java.lang.String DOCUMENT_AUTHORITY = "com.android.externalstorage.documents";

    private static final java.lang.String DOCUMENT_ROOT_PRIMARY_EMULATED = "primary";

    /**
     * Build an intent to browse the contents of this volume. Only valid for
     * {@link #TYPE_EMULATED} or {@link #TYPE_PUBLIC}.
     */
    public android.content.Intent buildBrowseIntent() {
        final android.net.Uri uri;
        if (type == android.os.storage.VolumeInfo.TYPE_PUBLIC) {
            uri = android.provider.DocumentsContract.buildRootUri(android.os.storage.VolumeInfo.DOCUMENT_AUTHORITY, fsUuid);
        } else
            if ((type == android.os.storage.VolumeInfo.TYPE_EMULATED) && isPrimary()) {
                uri = android.provider.DocumentsContract.buildRootUri(android.os.storage.VolumeInfo.DOCUMENT_AUTHORITY, android.os.storage.VolumeInfo.DOCUMENT_ROOT_PRIMARY_EMULATED);
            } else {
                return null;
            }

        final android.content.Intent intent = new android.content.Intent(android.provider.DocumentsContract.ACTION_BROWSE);
        intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, android.provider.DocumentsContract.Root.MIME_TYPE_ITEM);
        // note that docsui treats this as *force* show advanced. So sending
        // false permits advanced to be shown based on user preferences.
        intent.putExtra(android.provider.DocumentsContract.EXTRA_SHOW_ADVANCED, isPrimary());
        intent.putExtra(android.provider.DocumentsContract.EXTRA_FANCY_FEATURES, true);
        intent.putExtra(android.provider.DocumentsContract.EXTRA_SHOW_FILESIZE, true);
        return intent;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
        dump(new com.android.internal.util.IndentingPrintWriter(writer, "    ", 80));
        return writer.toString();
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println(("VolumeInfo{" + id) + "}:");
        pw.increaseIndent();
        pw.printPair("type", android.util.DebugUtils.valueToString(getClass(), "TYPE_", type));
        pw.printPair("diskId", getDiskId());
        pw.printPair("partGuid", partGuid);
        pw.printPair("mountFlags", android.util.DebugUtils.flagsToString(getClass(), "MOUNT_FLAG_", mountFlags));
        pw.printPair("mountUserId", mountUserId);
        pw.printPair("state", android.util.DebugUtils.valueToString(getClass(), "STATE_", state));
        pw.println();
        pw.printPair("fsType", fsType);
        pw.printPair("fsUuid", fsUuid);
        pw.printPair("fsLabel", fsLabel);
        pw.println();
        pw.printPair("path", path);
        pw.printPair("internalPath", internalPath);
        pw.decreaseIndent();
        pw.println();
    }

    @java.lang.Override
    public android.os.storage.VolumeInfo clone() {
        final android.os.Parcel temp = android.os.Parcel.obtain();
        try {
            writeToParcel(temp, 0);
            temp.setDataPosition(0);
            return android.os.storage.VolumeInfo.CREATOR.createFromParcel(temp);
        } finally {
            temp.recycle();
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.os.storage.VolumeInfo) {
            return java.util.Objects.equals(id, ((android.os.storage.VolumeInfo) (o)).id);
        } else {
            return false;
        }
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final android.os.Parcelable.Creator<android.os.storage.VolumeInfo> CREATOR = new android.os.Parcelable.Creator<android.os.storage.VolumeInfo>() {
        @java.lang.Override
        public android.os.storage.VolumeInfo createFromParcel(android.os.Parcel in) {
            return new android.os.storage.VolumeInfo(in);
        }

        @java.lang.Override
        public android.os.storage.VolumeInfo[] newArray(int size) {
            return new android.os.storage.VolumeInfo[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeInt(type);
        if (disk != null) {
            parcel.writeInt(1);
            disk.writeToParcel(parcel, flags);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeString(partGuid);
        parcel.writeInt(mountFlags);
        parcel.writeInt(mountUserId);
        parcel.writeInt(state);
        parcel.writeString(fsType);
        parcel.writeString(fsUuid);
        parcel.writeString(fsLabel);
        parcel.writeString(path);
        parcel.writeString(internalPath);
    }
}

