/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.content.res;


/**
 * The loaded, immutable, in-memory representation of an APK.
 *
 * The main implementation is native C++ and there is very little API surface exposed here. The APK
 * is mainly accessed via {@link AssetManager}.
 *
 * Since the ApkAssets instance is immutable, it can be reused and shared across AssetManagers,
 * making the creation of AssetManagers very cheap.
 *
 * @unknown 
 */
public final class ApkAssets {
    @com.android.internal.annotations.GuardedBy("this")
    private final long mNativePtr;

    @com.android.internal.annotations.GuardedBy("this")
    private final android.content.res.StringBlock mStringBlock;

    @com.android.internal.annotations.GuardedBy("this")
    private boolean mOpen = true;

    /**
     * Creates a new ApkAssets instance from the given path on disk.
     *
     * @param path
     * 		The path to an APK on disk.
     * @return a new instance of ApkAssets.
     * @throws IOException
     * 		if a disk I/O error or parsing error occurred.
     */
    @android.annotation.NonNull
    public static android.content.res.ApkAssets loadFromPath(@android.annotation.NonNull
    java.lang.String path) throws java.io.IOException {
        return /* system */
        /* forceSharedLib */
        /* overlay */
        new android.content.res.ApkAssets(path, false, false, false);
    }

    /**
     * Creates a new ApkAssets instance from the given path on disk.
     *
     * @param path
     * 		The path to an APK on disk.
     * @param system
     * 		When true, the APK is loaded as a system APK (framework).
     * @return a new instance of ApkAssets.
     * @throws IOException
     * 		if a disk I/O error or parsing error occurred.
     */
    @android.annotation.NonNull
    public static android.content.res.ApkAssets loadFromPath(@android.annotation.NonNull
    java.lang.String path, boolean system) throws java.io.IOException {
        return /* forceSharedLib */
        /* overlay */
        new android.content.res.ApkAssets(path, system, false, false);
    }

    /**
     * Creates a new ApkAssets instance from the given path on disk.
     *
     * @param path
     * 		The path to an APK on disk.
     * @param system
     * 		When true, the APK is loaded as a system APK (framework).
     * @param forceSharedLibrary
     * 		When true, any packages within the APK with package ID 0x7f are
     * 		loaded as a shared library.
     * @return a new instance of ApkAssets.
     * @throws IOException
     * 		if a disk I/O error or parsing error occurred.
     */
    @android.annotation.NonNull
    public static android.content.res.ApkAssets loadFromPath(@android.annotation.NonNull
    java.lang.String path, boolean system, boolean forceSharedLibrary) throws java.io.IOException {
        return /* overlay */
        new android.content.res.ApkAssets(path, system, forceSharedLibrary, false);
    }

    /**
     * Creates a new ApkAssets instance from the given file descriptor. Not for use by applications.
     *
     * Performs a dup of the underlying fd, so you must take care of still closing
     * the FileDescriptor yourself (and can do that whenever you want).
     *
     * @param fd
     * 		The FileDescriptor of an open, readable APK.
     * @param friendlyName
     * 		The friendly name used to identify this ApkAssets when logging.
     * @param system
     * 		When true, the APK is loaded as a system APK (framework).
     * @param forceSharedLibrary
     * 		When true, any packages within the APK with package ID 0x7f are
     * 		loaded as a shared library.
     * @return a new instance of ApkAssets.
     * @throws IOException
     * 		if a disk I/O error or parsing error occurred.
     */
    @android.annotation.NonNull
    public static android.content.res.ApkAssets loadFromFd(@android.annotation.NonNull
    java.io.FileDescriptor fd, @android.annotation.NonNull
    java.lang.String friendlyName, boolean system, boolean forceSharedLibrary) throws java.io.IOException {
        return new android.content.res.ApkAssets(fd, friendlyName, system, forceSharedLibrary);
    }

    /**
     * Creates a new ApkAssets instance from the IDMAP at idmapPath. The overlay APK path
     * is encoded within the IDMAP.
     *
     * @param idmapPath
     * 		Path to the IDMAP of an overlay APK.
     * @param system
     * 		When true, the APK is loaded as a system APK (framework).
     * @return a new instance of ApkAssets.
     * @throws IOException
     * 		if a disk I/O error or parsing error occurred.
     */
    @android.annotation.NonNull
    public static android.content.res.ApkAssets loadOverlayFromPath(@android.annotation.NonNull
    java.lang.String idmapPath, boolean system) throws java.io.IOException {
        return /* forceSharedLibrary */
        /* overlay */
        new android.content.res.ApkAssets(idmapPath, system, false, true);
    }

    private ApkAssets(@android.annotation.NonNull
    java.lang.String path, boolean system, boolean forceSharedLib, boolean overlay) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(path, "path");
        mNativePtr = android.content.res.ApkAssets.nativeLoad(path, system, forceSharedLib, overlay);
        mStringBlock = /* useSparse */
        new android.content.res.StringBlock(android.content.res.ApkAssets.nativeGetStringBlock(mNativePtr), true);
    }

    private ApkAssets(@android.annotation.NonNull
    java.io.FileDescriptor fd, @android.annotation.NonNull
    java.lang.String friendlyName, boolean system, boolean forceSharedLib) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(fd, "fd");
        com.android.internal.util.Preconditions.checkNotNull(friendlyName, "friendlyName");
        mNativePtr = android.content.res.ApkAssets.nativeLoadFromFd(fd, friendlyName, system, forceSharedLib);
        mStringBlock = /* useSparse */
        new android.content.res.StringBlock(android.content.res.ApkAssets.nativeGetStringBlock(mNativePtr), true);
    }

    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public java.lang.String getAssetPath() {
        synchronized(this) {
            return android.content.res.ApkAssets.nativeGetAssetPath(mNativePtr);
        }
    }

    java.lang.CharSequence getStringFromPool(int idx) {
        synchronized(this) {
            return mStringBlock.get(idx);
        }
    }

    /**
     * Retrieve a parser for a compiled XML file. This is associated with a single APK and
     * <em>NOT</em> a full AssetManager. This means that shared-library references will not be
     * dynamically assigned runtime package IDs.
     *
     * @param fileName
     * 		The path to the file within the APK.
     * @return An XmlResourceParser.
     * @throws IOException
     * 		if the file was not found or an error occurred retrieving it.
     */
    @android.annotation.NonNull
    public android.content.res.XmlResourceParser openXml(@android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(fileName, "fileName");
        synchronized(this) {
            long nativeXmlPtr = android.content.res.ApkAssets.nativeOpenXml(mNativePtr, fileName);
            try (android.content.res.XmlBlock block = new android.content.res.XmlBlock(null, nativeXmlPtr)) {
                android.content.res.XmlResourceParser parser = block.newParser();
                // If nativeOpenXml doesn't throw, it will always return a valid native pointer,
                // which makes newParser always return non-null. But let's be paranoid.
                if (parser == null) {
                    throw new java.lang.AssertionError("block.newParser() returned a null parser");
                }
                return parser;
            }
        }
    }

    /**
     * Returns false if the underlying APK was changed since this ApkAssets was loaded.
     */
    public boolean isUpToDate() {
        synchronized(this) {
            return android.content.res.ApkAssets.nativeIsUpToDate(mNativePtr);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("ApkAssets{path=" + getAssetPath()) + "}";
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        close();
    }

    /**
     * Closes this class and the contained {@link #mStringBlock}.
     */
    public void close() throws java.lang.Throwable {
        synchronized(this) {
            if (mOpen) {
                mOpen = false;
                mStringBlock.close();
                android.content.res.ApkAssets.nativeDestroy(mNativePtr);
            }
        }
    }

    private static native long nativeLoad(@android.annotation.NonNull
    java.lang.String path, boolean system, boolean forceSharedLib, boolean overlay) throws java.io.IOException;

    private static native long nativeLoadFromFd(@android.annotation.NonNull
    java.io.FileDescriptor fd, @android.annotation.NonNull
    java.lang.String friendlyName, boolean system, boolean forceSharedLib) throws java.io.IOException;

    private static native void nativeDestroy(long ptr);

    @android.annotation.NonNull
    private static native java.lang.String nativeGetAssetPath(long ptr);

    private static native long nativeGetStringBlock(long ptr);

    private static native boolean nativeIsUpToDate(long ptr);

    private static native long nativeOpenXml(long ptr, @android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException;
}

