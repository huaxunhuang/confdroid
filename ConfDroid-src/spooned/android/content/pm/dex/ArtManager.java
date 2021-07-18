/**
 * Copyright 2017 The Android Open Source Project
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
package android.content.pm.dex;


/**
 * Class for retrieving various kinds of information related to the runtime artifacts of
 * packages that are currently installed on the device.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class ArtManager {
    private static final java.lang.String TAG = "ArtManager";

    /**
     * The snapshot failed because the package was not found.
     */
    public static final int SNAPSHOT_FAILED_PACKAGE_NOT_FOUND = 0;

    /**
     * The snapshot failed because the package code path does not exist.
     */
    public static final int SNAPSHOT_FAILED_CODE_PATH_NOT_FOUND = 1;

    /**
     * The snapshot failed because of an internal error (e.g. error during opening profiles).
     */
    public static final int SNAPSHOT_FAILED_INTERNAL_ERROR = 2;

    /**
     * Constant used for applications profiles.
     */
    public static final int PROFILE_APPS = 0;

    /**
     * Constant used for the boot image profile.
     */
    public static final int PROFILE_BOOT_IMAGE = 1;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "PROFILE_" }, value = { android.content.pm.dex.ArtManager.PROFILE_APPS, android.content.pm.dex.ArtManager.PROFILE_BOOT_IMAGE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ProfileType {}

    private final android.content.Context mContext;

    private final android.content.pm.dex.IArtManager mArtManager;

    /**
     *
     *
     * @unknown 
     */
    public ArtManager(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.content.pm.dex.IArtManager manager) {
        mContext = context;
        mArtManager = manager;
    }

    /**
     * Snapshots a runtime profile according to the {@code profileType} parameter.
     *
     * If {@code profileType} is {@link ArtManager#PROFILE_APPS} the method will snapshot
     * the profile for for an apk belonging to the package {@code packageName}.
     * The apk is identified by {@code codePath}.
     *
     * If {@code profileType} is {@code ArtManager.PROFILE_BOOT_IMAGE} the method will snapshot
     * the profile for the boot image. In this case {@code codePath can be null}. The parameters
     * {@code packageName} and {@code codePath} are ignored.
     * u
     * The calling process must have {@code android.permission.READ_RUNTIME_PROFILE} permission.
     *
     * The result will be posted on the {@code executor} using the given {@code callback}.
     * The profile will be available as a read-only {@link android.os.ParcelFileDescriptor}.
     *
     * This method will throw {@link IllegalStateException} if
     * {@link ArtManager#isRuntimeProfilingEnabled(int)} does not return true for the given
     * {@code profileType}.
     *
     * @param profileType
     * 		the type of profile that should be snapshot (boot image or app)
     * @param packageName
     * 		the target package name or null if the target is the boot image
     * @param codePath
     * 		the code path for which the profile should be retrieved or null if
     * 		the target is the boot image
     * @param callback
     * 		the callback which should be used for the result
     * @param executor
     * 		the executor which should be used to post the result
     */
    @android.annotation.RequiresPermission(allOf = { android.Manifest.permission.READ_RUNTIME_PROFILES, android.Manifest.permission.PACKAGE_USAGE_STATS })
    public void snapshotRuntimeProfile(@android.content.pm.dex.ArtManager.ProfileType
    int profileType, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.lang.String codePath, @android.annotation.NonNull
    @android.annotation.CallbackExecutor
    java.util.concurrent.Executor executor, @android.annotation.NonNull
    android.content.pm.dex.ArtManager.SnapshotRuntimeProfileCallback callback) {
        android.util.Slog.d(android.content.pm.dex.ArtManager.TAG, (("Requesting profile snapshot for " + packageName) + ":") + codePath);
        android.content.pm.dex.ArtManager.SnapshotRuntimeProfileCallbackDelegate delegate = new android.content.pm.dex.ArtManager.SnapshotRuntimeProfileCallbackDelegate(callback, executor);
        try {
            mArtManager.snapshotRuntimeProfile(profileType, packageName, codePath, delegate, mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /**
     * Returns true if runtime profiles are enabled for the given type, false otherwise.
     *
     * The calling process must have {@code android.permission.READ_RUNTIME_PROFILE} permission.
     *
     * @param profileType
     * 		can be either {@link ArtManager#PROFILE_APPS}
     * 		or {@link ArtManager#PROFILE_BOOT_IMAGE}
     */
    @android.annotation.RequiresPermission(allOf = { android.Manifest.permission.READ_RUNTIME_PROFILES, android.Manifest.permission.PACKAGE_USAGE_STATS })
    public boolean isRuntimeProfilingEnabled(@android.content.pm.dex.ArtManager.ProfileType
    int profileType) {
        try {
            return mArtManager.isRuntimeProfilingEnabled(profileType, mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /**
     * Callback used for retrieving runtime profiles.
     */
    public static abstract class SnapshotRuntimeProfileCallback {
        /**
         * Called when the profile snapshot finished with success.
         *
         * @param profileReadFd
         * 		the file descriptor that can be used to read the profile. Note that
         * 		the file might be empty (which is valid profile).
         */
        public abstract void onSuccess(android.os.ParcelFileDescriptor profileReadFd);

        /**
         * Called when the profile snapshot finished with an error.
         *
         * @param errCode
         * 		the error code {@see SNAPSHOT_FAILED_PACKAGE_NOT_FOUND,
         * 		SNAPSHOT_FAILED_CODE_PATH_NOT_FOUND, SNAPSHOT_FAILED_INTERNAL_ERROR}.
         */
        public abstract void onError(int errCode);
    }

    private static class SnapshotRuntimeProfileCallbackDelegate extends android.content.pm.dex.ISnapshotRuntimeProfileCallback.Stub {
        private final android.content.pm.dex.ArtManager.SnapshotRuntimeProfileCallback mCallback;

        private final java.util.concurrent.Executor mExecutor;

        private SnapshotRuntimeProfileCallbackDelegate(android.content.pm.dex.ArtManager.SnapshotRuntimeProfileCallback callback, java.util.concurrent.Executor executor) {
            mCallback = callback;
            mExecutor = executor;
        }

        @java.lang.Override
        public void onSuccess(final android.os.ParcelFileDescriptor profileReadFd) {
            mExecutor.execute(() -> mCallback.onSuccess(profileReadFd));
        }

        @java.lang.Override
        public void onError(int errCode) {
            mExecutor.execute(() -> mCallback.onError(errCode));
        }
    }

    /**
     * Return the profile name for the given split. If {@code splitName} is null the
     * method returns the profile name for the base apk.
     *
     * @unknown 
     */
    public static java.lang.String getProfileName(java.lang.String splitName) {
        return splitName == null ? "primary.prof" : splitName + ".split.prof";
    }

    /**
     * Return the path to the current profile corresponding to given package and split.
     *
     * @unknown 
     */
    public static java.lang.String getCurrentProfilePath(java.lang.String packageName, int userId, java.lang.String splitName) {
        java.io.File profileDir = android.os.Environment.getDataProfilesDePackageDirectory(userId, packageName);
        return new java.io.File(profileDir, android.content.pm.dex.ArtManager.getProfileName(splitName)).getAbsolutePath();
    }

    /**
     * Return the snapshot profile file for the given package and profile name.
     *
     * KEEP in sync with installd dexopt.cpp.
     * TODO(calin): inject the snapshot profile name from PM to avoid the dependency.
     *
     * @unknown 
     */
    public static java.io.File getProfileSnapshotFileForName(java.lang.String packageName, java.lang.String profileName) {
        java.io.File profileDir = android.os.Environment.getDataRefProfilesDePackageDirectory(packageName);
        return new java.io.File(profileDir, profileName + ".snapshot");
    }
}

