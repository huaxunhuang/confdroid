/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.content.res.loader;


/**
 * Provides methods to load resources data from APKs ({@code .apk}) and resources tables
 * (eg. {@code resources.arsc}) for use with {@link ResourcesLoader ResourcesLoader(s)}.
 */
public class ResourcesProvider implements java.io.Closeable , java.lang.AutoCloseable {
    private static final java.lang.String TAG = "ResourcesProvider";

    private final java.lang.Object mLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mLock")
    private boolean mOpen = true;

    @com.android.internal.annotations.GuardedBy("mLock")
    private int mOpenCount = 0;

    @com.android.internal.annotations.GuardedBy("mLock")
    private final android.content.res.ApkAssets mApkAssets;

    /**
     * Creates an empty ResourcesProvider with no resource data. This is useful for loading
     * file-based assets not associated with resource identifiers.
     *
     * @param assetsProvider
     * 		the assets provider that implements the loading of file-based resources
     */
    @android.annotation.NonNull
    public static android.content.res.loader.ResourcesProvider empty(@android.annotation.NonNull
    android.content.res.loader.AssetsProvider assetsProvider) {
        return new android.content.res.loader.ResourcesProvider(android.content.res.ApkAssets.loadEmptyForLoader(android.content.res.ApkAssets.PROPERTY_LOADER, assetsProvider));
    }

    /**
     * Creates a ResourcesProvider from an APK ({@code .apk}) file descriptor.
     *
     * <p>The file descriptor is duplicated and the original may be closed by the application at any
     * time without affecting the ResourcesProvider.
     *
     * @param fileDescriptor
     * 		the file descriptor of the APK to load
     * @see ParcelFileDescriptor#open(File, int)
     * @see android.system.Os#memfd_create(String, int)
     */
    @android.annotation.NonNull
    public static android.content.res.loader.ResourcesProvider loadFromApk(@android.annotation.NonNull
    android.os.ParcelFileDescriptor fileDescriptor) throws java.io.IOException {
        return /* assetsProvider */
        android.content.res.loader.ResourcesProvider.loadFromApk(fileDescriptor, null);
    }

    /**
     * Creates a ResourcesProvider from an APK ({@code .apk}) file descriptor.
     *
     * <p>The file descriptor is duplicated and the original may be closed by the application at any
     * time without affecting the ResourcesProvider.
     *
     * <p>The assets provider can override the loading of files within the APK and can provide
     * entirely new files that do not exist in the APK.
     *
     * @param fileDescriptor
     * 		the file descriptor of the APK to load
     * @param assetsProvider
     * 		the assets provider that overrides the loading of file-based resources
     * @see ParcelFileDescriptor#open(File, int)
     * @see android.system.Os#memfd_create(String, int)
     */
    @android.annotation.NonNull
    public static android.content.res.loader.ResourcesProvider loadFromApk(@android.annotation.NonNull
    android.os.ParcelFileDescriptor fileDescriptor, @android.annotation.Nullable
    android.content.res.loader.AssetsProvider assetsProvider) throws java.io.IOException {
        return new android.content.res.loader.ResourcesProvider(android.content.res.ApkAssets.loadFromFd(fileDescriptor.getFileDescriptor(), fileDescriptor.toString(), android.content.res.ApkAssets.PROPERTY_LOADER, assetsProvider));
    }

    /**
     * Creates a ResourcesProvider from an APK ({@code .apk}) file descriptor.
     *
     * <p>The file descriptor is duplicated and the original may be closed by the application at any
     * time without affecting the ResourcesProvider.
     *
     * <p>The assets provider can override the loading of files within the APK and can provide
     * entirely new files that do not exist in the APK.
     *
     * @param fileDescriptor
     * 		the file descriptor of the APK to load
     * @param offset
     * 		The location within the file that the apk starts. This must be 0 if length is
     * 		{@link AssetFileDescriptor#UNKNOWN_LENGTH}.
     * @param length
     * 		The number of bytes of the apk, or {@link AssetFileDescriptor#UNKNOWN_LENGTH}
     * 		if it extends to the end of the file.
     * @param assetsProvider
     * 		the assets provider that overrides the loading of file-based resources
     * @see ParcelFileDescriptor#open(File, int)
     * @see android.system.Os#memfd_create(String, int)
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.NonNull
    public static android.content.res.loader.ResourcesProvider loadFromApk(@android.annotation.NonNull
    android.os.ParcelFileDescriptor fileDescriptor, long offset, long length, @android.annotation.Nullable
    android.content.res.loader.AssetsProvider assetsProvider) throws java.io.IOException {
        return new android.content.res.loader.ResourcesProvider(android.content.res.ApkAssets.loadFromFd(fileDescriptor.getFileDescriptor(), fileDescriptor.toString(), offset, length, android.content.res.ApkAssets.PROPERTY_LOADER, assetsProvider));
    }

    /**
     * Creates a ResourcesProvider from a resources table ({@code .arsc}) file descriptor.
     *
     * <p>The file descriptor is duplicated and the original may be closed by the application at any
     * time without affecting the ResourcesProvider.
     *
     * <p>The resources table format is not an archive format and therefore cannot asset files
     * within itself. The assets provider can instead provide files that are potentially referenced
     * by path in the resources table.
     *
     * @param fileDescriptor
     * 		the file descriptor of the resources table to load
     * @param assetsProvider
     * 		the assets provider that implements the loading of file-based resources
     * @see ParcelFileDescriptor#open(File, int)
     * @see android.system.Os#memfd_create(String, int)
     */
    @android.annotation.NonNull
    public static android.content.res.loader.ResourcesProvider loadFromTable(@android.annotation.NonNull
    android.os.ParcelFileDescriptor fileDescriptor, @android.annotation.Nullable
    android.content.res.loader.AssetsProvider assetsProvider) throws java.io.IOException {
        return new android.content.res.loader.ResourcesProvider(android.content.res.ApkAssets.loadTableFromFd(fileDescriptor.getFileDescriptor(), fileDescriptor.toString(), android.content.res.ApkAssets.PROPERTY_LOADER, assetsProvider));
    }

    /**
     * Creates a ResourcesProvider from a resources table ({@code .arsc}) file descriptor.
     *
     * The file descriptor is duplicated and the original may be closed by the application at any
     * time without affecting the ResourcesProvider.
     *
     * <p>The resources table format is not an archive format and therefore cannot asset files
     * within itself. The assets provider can instead provide files that are potentially referenced
     * by path in the resources table.
     *
     * @param fileDescriptor
     * 		the file descriptor of the resources table to load
     * @param offset
     * 		The location within the file that the table starts. This must be 0 if length is
     * 		{@link AssetFileDescriptor#UNKNOWN_LENGTH}.
     * @param length
     * 		The number of bytes of the table, or {@link AssetFileDescriptor#UNKNOWN_LENGTH}
     * 		if it extends to the end of the file.
     * @param assetsProvider
     * 		the assets provider that overrides the loading of file-based resources
     * @see ParcelFileDescriptor#open(File, int)
     * @see android.system.Os#memfd_create(String, int)
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.NonNull
    public static android.content.res.loader.ResourcesProvider loadFromTable(@android.annotation.NonNull
    android.os.ParcelFileDescriptor fileDescriptor, long offset, long length, @android.annotation.Nullable
    android.content.res.loader.AssetsProvider assetsProvider) throws java.io.IOException {
        return new android.content.res.loader.ResourcesProvider(android.content.res.ApkAssets.loadTableFromFd(fileDescriptor.getFileDescriptor(), fileDescriptor.toString(), offset, length, android.content.res.ApkAssets.PROPERTY_LOADER, assetsProvider));
    }

    /**
     * Read from a split installed alongside the application, which may not have been
     * loaded initially because the application requested isolated split loading.
     *
     * @param context
     * 		a context of the package that contains the split
     * @param splitName
     * 		the name of the split to load
     */
    @android.annotation.NonNull
    public static android.content.res.loader.ResourcesProvider loadFromSplit(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    java.lang.String splitName) throws java.io.IOException {
        android.content.pm.ApplicationInfo appInfo = context.getApplicationInfo();
        int splitIndex = com.android.internal.util.ArrayUtils.indexOf(appInfo.splitNames, splitName);
        if (splitIndex < 0) {
            throw new java.lang.IllegalArgumentException(("Split " + splitName) + " not found");
        }
        java.lang.String splitPath = appInfo.getSplitCodePaths()[splitIndex];
        return new android.content.res.loader.ResourcesProvider(/* assetsProvider */
        android.content.res.ApkAssets.loadFromPath(splitPath, android.content.res.ApkAssets.PROPERTY_LOADER, null));
    }

    /**
     * Creates a ResourcesProvider from a directory path.
     *
     * File-based resources will be resolved within the directory as if the directory is an APK.
     *
     * @param path
     * 		the path of the directory to treat as an APK
     * @param assetsProvider
     * 		the assets provider that overrides the loading of file-based resources
     */
    @android.annotation.NonNull
    public static android.content.res.loader.ResourcesProvider loadFromDirectory(@android.annotation.NonNull
    java.lang.String path, @android.annotation.Nullable
    android.content.res.loader.AssetsProvider assetsProvider) throws java.io.IOException {
        return new android.content.res.loader.ResourcesProvider(android.content.res.ApkAssets.loadFromDir(path, android.content.res.ApkAssets.PROPERTY_LOADER, assetsProvider));
    }

    private ResourcesProvider(@android.annotation.NonNull
    android.content.res.ApkAssets apkAssets) {
        this.mApkAssets = apkAssets;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.content.res.ApkAssets getApkAssets() {
        return mApkAssets;
    }

    final void incrementRefCount() {
        synchronized(mLock) {
            if (!mOpen) {
                throw new java.lang.IllegalStateException("Operation failed: resources provider is closed");
            }
            mOpenCount++;
        }
    }

    final void decrementRefCount() {
        synchronized(mLock) {
            mOpenCount--;
        }
    }

    /**
     * Frees internal data structures. Closed providers can no longer be added to
     * {@link ResourcesLoader ResourcesLoader(s)}.
     *
     * @throws IllegalStateException
     * 		if provider is currently used by a ResourcesLoader
     */
    @java.lang.Override
    public void close() {
        synchronized(mLock) {
            if (!mOpen) {
                return;
            }
            if (mOpenCount != 0) {
                throw new java.lang.IllegalStateException(("Failed to close provider used by " + mOpenCount) + " ResourcesLoader instances");
            }
            mOpen = false;
        }
        try {
            mApkAssets.close();
        } catch (java.lang.Throwable ignored) {
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        synchronized(mLock) {
            if (mOpenCount != 0) {
                android.util.Log.w(android.content.res.loader.ResourcesProvider.TAG, (("ResourcesProvider " + this) + " finalized with non-zero refs: ") + mOpenCount);
            }
            if (mOpen) {
                mOpen = false;
                mApkAssets.close();
            }
        }
    }
}

