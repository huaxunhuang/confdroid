/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.pm;


/**
 * Exposes {@link #SYSTEM_PARTITIONS} which represents the partitions in which application packages
 * can be installed. The partitions are ordered from most generic (lowest priority) to most specific
 * (greatest priority).
 *
 * @unknown 
 */
public class PackagePartitions {
    public static final int PARTITION_SYSTEM = 0;

    public static final int PARTITION_VENDOR = 1;

    public static final int PARTITION_ODM = 2;

    public static final int PARTITION_OEM = 3;

    public static final int PARTITION_PRODUCT = 4;

    public static final int PARTITION_SYSTEM_EXT = 5;

    @android.annotation.IntDef(flag = true, prefix = { "PARTITION_" }, value = { android.content.pm.PackagePartitions.PARTITION_SYSTEM, android.content.pm.PackagePartitions.PARTITION_VENDOR, android.content.pm.PackagePartitions.PARTITION_ODM, android.content.pm.PackagePartitions.PARTITION_OEM, android.content.pm.PackagePartitions.PARTITION_PRODUCT, android.content.pm.PackagePartitions.PARTITION_SYSTEM_EXT })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PartitionType {}

    /**
     * The list of all system partitions that may contain packages in ascending order of
     * specificity (the more generic, the earlier in the list a partition appears).
     */
    private static final java.util.ArrayList<android.content.pm.PackagePartitions.SystemPartition> SYSTEM_PARTITIONS = new java.util.ArrayList(java.util.Arrays.asList(/* containsPrivApp */
    /* containsOverlay */
    new android.content.pm.PackagePartitions.SystemPartition(android.os.Environment.getRootDirectory(), android.content.pm.PackagePartitions.PARTITION_SYSTEM, true, false), /* containsPrivApp */
    /* containsOverlay */
    new android.content.pm.PackagePartitions.SystemPartition(android.os.Environment.getVendorDirectory(), android.content.pm.PackagePartitions.PARTITION_VENDOR, true, true), /* containsPrivApp */
    /* containsOverlay */
    new android.content.pm.PackagePartitions.SystemPartition(android.os.Environment.getOdmDirectory(), android.content.pm.PackagePartitions.PARTITION_ODM, true, true), /* containsPrivApp */
    /* containsOverlay */
    new android.content.pm.PackagePartitions.SystemPartition(android.os.Environment.getOemDirectory(), android.content.pm.PackagePartitions.PARTITION_OEM, false, true), /* containsPrivApp */
    /* containsOverlay */
    new android.content.pm.PackagePartitions.SystemPartition(android.os.Environment.getProductDirectory(), android.content.pm.PackagePartitions.PARTITION_PRODUCT, true, true), /* containsPrivApp */
    /* containsOverlay */
    new android.content.pm.PackagePartitions.SystemPartition(android.os.Environment.getSystemExtDirectory(), android.content.pm.PackagePartitions.PARTITION_SYSTEM_EXT, true, true)));

    /**
     * Returns a list in which the elements are products of the specified function applied to the
     * list of {@link #SYSTEM_PARTITIONS} in increasing specificity order.
     */
    public static <T> java.util.ArrayList<T> getOrderedPartitions(@android.annotation.NonNull
    java.util.function.Function<android.content.pm.PackagePartitions.SystemPartition, T> producer) {
        final java.util.ArrayList<T> out = new java.util.ArrayList<>();
        for (int i = 0, n = android.content.pm.PackagePartitions.SYSTEM_PARTITIONS.size(); i < n; i++) {
            final T v = producer.apply(android.content.pm.PackagePartitions.SYSTEM_PARTITIONS.get(i));
            if (v != null) {
                out.add(v);
            }
        }
        return out;
    }

    private static java.io.File canonicalize(java.io.File path) {
        try {
            return path.getCanonicalFile();
        } catch (java.io.IOException e) {
            return path;
        }
    }

    /**
     * Represents a partition that contains application packages.
     */
    @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public static class SystemPartition {
        @android.content.pm.PackagePartitions.PartitionType
        public final int type;

        @android.annotation.NonNull
        private final android.content.pm.PackagePartitions.DeferredCanonicalFile mFolder;

        @android.annotation.Nullable
        private final android.content.pm.PackagePartitions.DeferredCanonicalFile mAppFolder;

        @android.annotation.Nullable
        private final android.content.pm.PackagePartitions.DeferredCanonicalFile mPrivAppFolder;

        @android.annotation.Nullable
        private final android.content.pm.PackagePartitions.DeferredCanonicalFile mOverlayFolder;

        private SystemPartition(@android.annotation.NonNull
        java.io.File folder, @android.content.pm.PackagePartitions.PartitionType
        int type, boolean containsPrivApp, boolean containsOverlay) {
            this.type = type;
            this.mFolder = new android.content.pm.PackagePartitions.DeferredCanonicalFile(folder);
            this.mAppFolder = new android.content.pm.PackagePartitions.DeferredCanonicalFile(folder, "app");
            this.mPrivAppFolder = (containsPrivApp) ? new android.content.pm.PackagePartitions.DeferredCanonicalFile(folder, "priv-app") : null;
            this.mOverlayFolder = (containsOverlay) ? new android.content.pm.PackagePartitions.DeferredCanonicalFile(folder, "overlay") : null;
        }

        public SystemPartition(@android.annotation.NonNull
        android.content.pm.PackagePartitions.SystemPartition original) {
            this.type = original.type;
            this.mFolder = new android.content.pm.PackagePartitions.DeferredCanonicalFile(original.mFolder.getFile());
            this.mAppFolder = original.mAppFolder;
            this.mPrivAppFolder = original.mPrivAppFolder;
            this.mOverlayFolder = original.mOverlayFolder;
        }

        /**
         * Creates a partition containing the same folders as the original partition but with a
         * different root folder.
         */
        public SystemPartition(@android.annotation.NonNull
        java.io.File rootFolder, @android.annotation.NonNull
        android.content.pm.PackagePartitions.SystemPartition partition) {
            this(rootFolder, partition.type, partition.mPrivAppFolder != null, partition.mOverlayFolder != null);
        }

        /**
         * Returns the canonical folder of the partition.
         */
        @android.annotation.NonNull
        public java.io.File getFolder() {
            return mFolder.getFile();
        }

        /**
         * Returns the canonical app folder of the partition.
         */
        @android.annotation.Nullable
        public java.io.File getAppFolder() {
            return mAppFolder == null ? null : mAppFolder.getFile();
        }

        /**
         * Returns the canonical priv-app folder of the partition, if one exists.
         */
        @android.annotation.Nullable
        public java.io.File getPrivAppFolder() {
            return mPrivAppFolder == null ? null : mPrivAppFolder.getFile();
        }

        /**
         * Returns the canonical overlay folder of the partition, if one exists.
         */
        @android.annotation.Nullable
        public java.io.File getOverlayFolder() {
            return mOverlayFolder == null ? null : mOverlayFolder.getFile();
        }

        /**
         * Returns whether the partition contains the specified file.
         */
        public boolean containsPath(@android.annotation.NonNull
        java.lang.String path) {
            return containsFile(new java.io.File(path));
        }

        /**
         * Returns whether the partition contains the specified file.
         */
        public boolean containsFile(@android.annotation.NonNull
        java.io.File file) {
            return android.os.FileUtils.contains(mFolder.getFile(), android.content.pm.PackagePartitions.canonicalize(file));
        }

        /**
         * Returns whether the partition contains the specified file in its priv-app folder.
         */
        public boolean containsPrivApp(@android.annotation.NonNull
        java.io.File scanFile) {
            return (mPrivAppFolder != null) && android.os.FileUtils.contains(mPrivAppFolder.getFile(), android.content.pm.PackagePartitions.canonicalize(scanFile));
        }

        /**
         * Returns whether the partition contains the specified file in its app folder.
         */
        public boolean containsApp(@android.annotation.NonNull
        java.io.File scanFile) {
            return (mAppFolder != null) && android.os.FileUtils.contains(mAppFolder.getFile(), android.content.pm.PackagePartitions.canonicalize(scanFile));
        }

        /**
         * Returns whether the partition contains the specified file in its overlay folder.
         */
        public boolean containsOverlay(@android.annotation.NonNull
        java.io.File scanFile) {
            return (mOverlayFolder != null) && android.os.FileUtils.contains(mOverlayFolder.getFile(), android.content.pm.PackagePartitions.canonicalize(scanFile));
        }
    }

    /**
     * A class that defers the canonicalization of its underlying file. This must be done so
     * processes do not attempt to canonicalize files in directories for which the process does not
     * have the correct selinux policies.
     */
    private static class DeferredCanonicalFile {
        private boolean mIsCanonical = false;

        @android.annotation.NonNull
        private java.io.File mFile;

        private DeferredCanonicalFile(@android.annotation.NonNull
        java.io.File dir) {
            mFile = dir;
        }

        private DeferredCanonicalFile(@android.annotation.NonNull
        java.io.File dir, @android.annotation.NonNull
        java.lang.String fileName) {
            mFile = new java.io.File(dir, fileName);
        }

        @android.annotation.NonNull
        private java.io.File getFile() {
            if (!mIsCanonical) {
                mFile = android.content.pm.PackagePartitions.canonicalize(mFile);
                mIsCanonical = true;
            }
            return mFile;
        }
    }
}

