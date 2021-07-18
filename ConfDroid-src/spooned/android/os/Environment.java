/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.os;


/**
 * Provides access to environment variables.
 */
public class Environment {
    private static final java.lang.String TAG = "Environment";

    private static final java.lang.String ENV_EXTERNAL_STORAGE = "EXTERNAL_STORAGE";

    private static final java.lang.String ENV_ANDROID_ROOT = "ANDROID_ROOT";

    private static final java.lang.String ENV_ANDROID_DATA = "ANDROID_DATA";

    private static final java.lang.String ENV_ANDROID_EXPAND = "ANDROID_EXPAND";

    private static final java.lang.String ENV_ANDROID_STORAGE = "ANDROID_STORAGE";

    private static final java.lang.String ENV_DOWNLOAD_CACHE = "DOWNLOAD_CACHE";

    private static final java.lang.String ENV_OEM_ROOT = "OEM_ROOT";

    private static final java.lang.String ENV_ODM_ROOT = "ODM_ROOT";

    private static final java.lang.String ENV_VENDOR_ROOT = "VENDOR_ROOT";

    /**
     * {@hide }
     */
    public static final java.lang.String DIR_ANDROID = "Android";

    private static final java.lang.String DIR_DATA = "data";

    private static final java.lang.String DIR_MEDIA = "media";

    private static final java.lang.String DIR_OBB = "obb";

    private static final java.lang.String DIR_FILES = "files";

    private static final java.lang.String DIR_CACHE = "cache";

    /**
     * {@hide }
     */
    @java.lang.Deprecated
    public static final java.lang.String DIRECTORY_ANDROID = android.os.Environment.DIR_ANDROID;

    private static final java.io.File DIR_ANDROID_ROOT = android.os.Environment.getDirectory(android.os.Environment.ENV_ANDROID_ROOT, "/system");

    private static final java.io.File DIR_ANDROID_DATA = android.os.Environment.getDirectory(android.os.Environment.ENV_ANDROID_DATA, "/data");

    private static final java.io.File DIR_ANDROID_EXPAND = android.os.Environment.getDirectory(android.os.Environment.ENV_ANDROID_EXPAND, "/mnt/expand");

    private static final java.io.File DIR_ANDROID_STORAGE = android.os.Environment.getDirectory(android.os.Environment.ENV_ANDROID_STORAGE, "/storage");

    private static final java.io.File DIR_DOWNLOAD_CACHE = android.os.Environment.getDirectory(android.os.Environment.ENV_DOWNLOAD_CACHE, "/cache");

    private static final java.io.File DIR_OEM_ROOT = android.os.Environment.getDirectory(android.os.Environment.ENV_OEM_ROOT, "/oem");

    private static final java.io.File DIR_ODM_ROOT = android.os.Environment.getDirectory(android.os.Environment.ENV_ODM_ROOT, "/odm");

    private static final java.io.File DIR_VENDOR_ROOT = android.os.Environment.getDirectory(android.os.Environment.ENV_VENDOR_ROOT, "/vendor");

    private static android.os.Environment.UserEnvironment sCurrentUser;

    private static boolean sUserRequired;

    static {
        android.os.Environment.initForCurrentUser();
    }

    /**
     * {@hide }
     */
    public static void initForCurrentUser() {
        final int userId = android.os.UserHandle.myUserId();
        android.os.Environment.sCurrentUser = new android.os.Environment.UserEnvironment(userId);
    }

    /**
     * {@hide }
     */
    public static class UserEnvironment {
        private final int mUserId;

        public UserEnvironment(int userId) {
            mUserId = userId;
        }

        public java.io.File[] getExternalDirs() {
            final android.os.storage.StorageVolume[] volumes = android.os.storage.StorageManager.getVolumeList(mUserId, android.os.storage.StorageManager.FLAG_FOR_WRITE);
            final java.io.File[] files = new java.io.File[volumes.length];
            for (int i = 0; i < volumes.length; i++) {
                files[i] = volumes[i].getPathFile();
            }
            return files;
        }

        @java.lang.Deprecated
        public java.io.File getExternalStorageDirectory() {
            return getExternalDirs()[0];
        }

        @java.lang.Deprecated
        public java.io.File getExternalStoragePublicDirectory(java.lang.String type) {
            return buildExternalStoragePublicDirs(type)[0];
        }

        public java.io.File[] buildExternalStoragePublicDirs(java.lang.String type) {
            return android.os.Environment.buildPaths(getExternalDirs(), type);
        }

        public java.io.File[] buildExternalStorageAndroidDataDirs() {
            return android.os.Environment.buildPaths(getExternalDirs(), android.os.Environment.DIR_ANDROID, android.os.Environment.DIR_DATA);
        }

        public java.io.File[] buildExternalStorageAndroidObbDirs() {
            return android.os.Environment.buildPaths(getExternalDirs(), android.os.Environment.DIR_ANDROID, android.os.Environment.DIR_OBB);
        }

        public java.io.File[] buildExternalStorageAppDataDirs(java.lang.String packageName) {
            return android.os.Environment.buildPaths(getExternalDirs(), android.os.Environment.DIR_ANDROID, android.os.Environment.DIR_DATA, packageName);
        }

        public java.io.File[] buildExternalStorageAppMediaDirs(java.lang.String packageName) {
            return android.os.Environment.buildPaths(getExternalDirs(), android.os.Environment.DIR_ANDROID, android.os.Environment.DIR_MEDIA, packageName);
        }

        public java.io.File[] buildExternalStorageAppObbDirs(java.lang.String packageName) {
            return android.os.Environment.buildPaths(getExternalDirs(), android.os.Environment.DIR_ANDROID, android.os.Environment.DIR_OBB, packageName);
        }

        public java.io.File[] buildExternalStorageAppFilesDirs(java.lang.String packageName) {
            return android.os.Environment.buildPaths(getExternalDirs(), android.os.Environment.DIR_ANDROID, android.os.Environment.DIR_DATA, packageName, android.os.Environment.DIR_FILES);
        }

        public java.io.File[] buildExternalStorageAppCacheDirs(java.lang.String packageName) {
            return android.os.Environment.buildPaths(getExternalDirs(), android.os.Environment.DIR_ANDROID, android.os.Environment.DIR_DATA, packageName, android.os.Environment.DIR_CACHE);
        }
    }

    /**
     * Return root of the "system" partition holding the core Android OS.
     * Always present and mounted read-only.
     */
    public static java.io.File getRootDirectory() {
        return android.os.Environment.DIR_ANDROID_ROOT;
    }

    /**
     * {@hide }
     */
    public static java.io.File getStorageDirectory() {
        return android.os.Environment.DIR_ANDROID_STORAGE;
    }

    /**
     * Return root directory of the "oem" partition holding OEM customizations,
     * if any. If present, the partition is mounted read-only.
     *
     * @unknown 
     */
    public static java.io.File getOemDirectory() {
        return android.os.Environment.DIR_OEM_ROOT;
    }

    /**
     * Return root directory of the "odm" partition holding ODM customizations,
     * if any. If present, the partition is mounted read-only.
     *
     * @unknown 
     */
    public static java.io.File getOdmDirectory() {
        return android.os.Environment.DIR_ODM_ROOT;
    }

    /**
     * Return root directory of the "vendor" partition that holds vendor-provided
     * software that should persist across simple reflashing of the "system" partition.
     *
     * @unknown 
     */
    public static java.io.File getVendorDirectory() {
        return android.os.Environment.DIR_VENDOR_ROOT;
    }

    /**
     * Return the system directory for a user. This is for use by system
     * services to store files relating to the user. This directory will be
     * automatically deleted when the user is removed.
     *
     * @deprecated This directory is valid and still exists, but callers should
    <em>strongly</em> consider switching to
    {@link #getDataSystemCeDirectory(int)} which is protected
    with user credentials or
    {@link #getDataSystemDeDirectory(int)} which supports fast
    user wipe.
     * @unknown 
     */
    @java.lang.Deprecated
    public static java.io.File getUserSystemDirectory(int userId) {
        return new java.io.File(new java.io.File(android.os.Environment.getDataSystemDirectory(), "users"), java.lang.Integer.toString(userId));
    }

    /**
     * Returns the config directory for a user. This is for use by system
     * services to store files relating to the user which should be readable by
     * any app running as that user.
     *
     * @deprecated This directory is valid and still exists, but callers should
    <em>strongly</em> consider switching to
    {@link #getDataMiscCeDirectory(int)} which is protected with
    user credentials or {@link #getDataMiscDeDirectory(int)}
    which supports fast user wipe.
     * @unknown 
     */
    @java.lang.Deprecated
    public static java.io.File getUserConfigDirectory(int userId) {
        return new java.io.File(new java.io.File(new java.io.File(android.os.Environment.getDataDirectory(), "misc"), "user"), java.lang.Integer.toString(userId));
    }

    /**
     * Return the user data directory.
     */
    public static java.io.File getDataDirectory() {
        return android.os.Environment.DIR_ANDROID_DATA;
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataDirectory(java.lang.String volumeUuid) {
        if (android.text.TextUtils.isEmpty(volumeUuid)) {
            return android.os.Environment.DIR_ANDROID_DATA;
        } else {
            return new java.io.File("/mnt/expand/" + volumeUuid);
        }
    }

    /**
     * {@hide }
     */
    public static java.io.File getExpandDirectory() {
        return android.os.Environment.DIR_ANDROID_EXPAND;
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataSystemDirectory() {
        return new java.io.File(android.os.Environment.getDataDirectory(), "system");
    }

    /**
     * Returns the base directory for per-user system directory, device encrypted.
     * {@hide }
     */
    public static java.io.File getDataSystemDeDirectory() {
        return android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), "system_de");
    }

    /**
     * Returns the base directory for per-user system directory, credential encrypted.
     * {@hide }
     */
    public static java.io.File getDataSystemCeDirectory() {
        return android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), "system_ce");
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataSystemCeDirectory(int userId) {
        return android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), "system_ce", java.lang.String.valueOf(userId));
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataSystemDeDirectory(int userId) {
        return android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), "system_de", java.lang.String.valueOf(userId));
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataMiscDirectory() {
        return new java.io.File(android.os.Environment.getDataDirectory(), "misc");
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataMiscCeDirectory(int userId) {
        return android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), "misc_ce", java.lang.String.valueOf(userId));
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataMiscDeDirectory(int userId) {
        return android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), "misc_de", java.lang.String.valueOf(userId));
    }

    private static java.io.File getDataProfilesDeDirectory(int userId) {
        return android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), "misc", "profiles", "cur", java.lang.String.valueOf(userId));
    }

    /**
     * {@hide }
     */
    public static java.io.File getReferenceProfile(java.lang.String packageName) {
        return android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), "misc", "profiles", "ref", packageName);
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataProfilesDePackageDirectory(int userId, java.lang.String packageName) {
        return android.os.Environment.buildPath(android.os.Environment.getDataProfilesDeDirectory(userId), packageName);
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataProfilesDeForeignDexDirectory(int userId) {
        return android.os.Environment.buildPath(android.os.Environment.getDataProfilesDeDirectory(userId), "foreign-dex");
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataAppDirectory(java.lang.String volumeUuid) {
        return new java.io.File(android.os.Environment.getDataDirectory(volumeUuid), "app");
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataAppEphemeralDirectory(java.lang.String volumeUuid) {
        return new java.io.File(android.os.Environment.getDataDirectory(volumeUuid), "app-ephemeral");
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataUserCeDirectory(java.lang.String volumeUuid) {
        return new java.io.File(android.os.Environment.getDataDirectory(volumeUuid), "user");
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataUserCeDirectory(java.lang.String volumeUuid, int userId) {
        return new java.io.File(android.os.Environment.getDataUserCeDirectory(volumeUuid), java.lang.String.valueOf(userId));
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataUserCePackageDirectory(java.lang.String volumeUuid, int userId, java.lang.String packageName) {
        // TODO: keep consistent with installd
        return new java.io.File(android.os.Environment.getDataUserCeDirectory(volumeUuid, userId), packageName);
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataUserDeDirectory(java.lang.String volumeUuid) {
        return new java.io.File(android.os.Environment.getDataDirectory(volumeUuid), "user_de");
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataUserDeDirectory(java.lang.String volumeUuid, int userId) {
        return new java.io.File(android.os.Environment.getDataUserDeDirectory(volumeUuid), java.lang.String.valueOf(userId));
    }

    /**
     * {@hide }
     */
    public static java.io.File getDataUserDePackageDirectory(java.lang.String volumeUuid, int userId, java.lang.String packageName) {
        // TODO: keep consistent with installd
        return new java.io.File(android.os.Environment.getDataUserDeDirectory(volumeUuid, userId), packageName);
    }

    /**
     * Return preloads directory.
     * <p>This directory may contain pre-loaded content such as
     * {@link #getDataPreloadsDemoDirectory() demo videos} and
     * {@link #getDataPreloadsAppsDirectory() APK files} .
     * {@hide }
     */
    public static java.io.File getDataPreloadsDirectory() {
        return new java.io.File(android.os.Environment.getDataDirectory(), "preloads");
    }

    /**
     *
     *
     * @see #getDataPreloadsDirectory()
    {@hide }
     */
    public static java.io.File getDataPreloadsDemoDirectory() {
        return new java.io.File(android.os.Environment.getDataPreloadsDirectory(), "demo");
    }

    /**
     *
     *
     * @see #getDataPreloadsDirectory()
    {@hide }
     */
    public static java.io.File getDataPreloadsAppsDirectory() {
        return new java.io.File(android.os.Environment.getDataPreloadsDirectory(), "apps");
    }

    /**
     *
     *
     * @see #getDataPreloadsDirectory()
    {@hide }
     */
    public static java.io.File getDataPreloadsMediaDirectory() {
        return new java.io.File(android.os.Environment.getDataPreloadsDirectory(), "media");
    }

    /**
     * Return the primary shared/external storage directory. This directory may
     * not currently be accessible if it has been mounted by the user on their
     * computer, has been removed from the device, or some other problem has
     * happened. You can determine its current state with
     * {@link #getExternalStorageState()}.
     * <p>
     * <em>Note: don't be confused by the word "external" here. This directory
     * can better be thought as media/shared storage. It is a filesystem that
     * can hold a relatively large amount of data and that is shared across all
     * applications (does not enforce permissions). Traditionally this is an SD
     * card, but it may also be implemented as built-in storage in a device that
     * is distinct from the protected internal storage and can be mounted as a
     * filesystem on a computer.</em>
     * <p>
     * On devices with multiple users (as described by {@link UserManager}),
     * each user has their own isolated shared storage. Applications only have
     * access to the shared storage for the user they're running as.
     * <p>
     * In devices with multiple shared/external storage directories, this
     * directory represents the primary storage that the user will interact
     * with. Access to secondary storage is available through
     * {@link Context#getExternalFilesDirs(String)},
     * {@link Context#getExternalCacheDirs()}, and
     * {@link Context#getExternalMediaDirs()}.
     * <p>
     * Applications should not directly use this top-level directory, in order
     * to avoid polluting the user's root namespace. Any files that are private
     * to the application should be placed in a directory returned by
     * {@link android.content.Context#getExternalFilesDir
     * Context.getExternalFilesDir}, which the system will take care of deleting
     * if the application is uninstalled. Other shared files should be placed in
     * one of the directories returned by
     * {@link #getExternalStoragePublicDirectory}.
     * <p>
     * Writing to this path requires the
     * {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission,
     * and starting in {@link android.os.Build.VERSION_CODES#KITKAT}, read access requires the
     * {@link android.Manifest.permission#READ_EXTERNAL_STORAGE} permission,
     * which is automatically granted if you hold the write permission.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#KITKAT}, if your
     * application only needs to store internal data, consider using
     * {@link Context#getExternalFilesDir(String)},
     * {@link Context#getExternalCacheDir()}, or
     * {@link Context#getExternalMediaDirs()}, which require no permissions to
     * read or write.
     * <p>
     * This path may change between platform versions, so applications should
     * only persist relative paths.
     * <p>
     * Here is an example of typical code to monitor the state of external
     * storage:
     * <p>
     * {@sample development/samples/ApiDemos/src/com/example/android/apis/content/ExternalStorage.java
     * monitor_storage}
     *
     * @see #getExternalStorageState()
     * @see #isExternalStorageRemovable()
     */
    public static java.io.File getExternalStorageDirectory() {
        android.os.Environment.throwIfUserRequired();
        return android.os.Environment.sCurrentUser.getExternalDirs()[0];
    }

    /**
     * {@hide }
     */
    public static java.io.File getLegacyExternalStorageDirectory() {
        return new java.io.File(java.lang.System.getenv(android.os.Environment.ENV_EXTERNAL_STORAGE));
    }

    /**
     * {@hide }
     */
    public static java.io.File getLegacyExternalStorageObbDirectory() {
        return android.os.Environment.buildPath(android.os.Environment.getLegacyExternalStorageDirectory(), android.os.Environment.DIR_ANDROID, android.os.Environment.DIR_OBB);
    }

    /**
     * Standard directory in which to place any audio files that should be
     * in the regular list of music for the user.
     * This may be combined with
     * {@link #DIRECTORY_PODCASTS}, {@link #DIRECTORY_NOTIFICATIONS},
     * {@link #DIRECTORY_ALARMS}, and {@link #DIRECTORY_RINGTONES} as a series
     * of directories to categories a particular audio file as more than one
     * type.
     */
    public static java.lang.String DIRECTORY_MUSIC = "Music";

    /**
     * Standard directory in which to place any audio files that should be
     * in the list of podcasts that the user can select (not as regular
     * music).
     * This may be combined with {@link #DIRECTORY_MUSIC},
     * {@link #DIRECTORY_NOTIFICATIONS},
     * {@link #DIRECTORY_ALARMS}, and {@link #DIRECTORY_RINGTONES} as a series
     * of directories to categories a particular audio file as more than one
     * type.
     */
    public static java.lang.String DIRECTORY_PODCASTS = "Podcasts";

    /**
     * Standard directory in which to place any audio files that should be
     * in the list of ringtones that the user can select (not as regular
     * music).
     * This may be combined with {@link #DIRECTORY_MUSIC},
     * {@link #DIRECTORY_PODCASTS}, {@link #DIRECTORY_NOTIFICATIONS}, and
     * {@link #DIRECTORY_ALARMS} as a series
     * of directories to categories a particular audio file as more than one
     * type.
     */
    public static java.lang.String DIRECTORY_RINGTONES = "Ringtones";

    /**
     * Standard directory in which to place any audio files that should be
     * in the list of alarms that the user can select (not as regular
     * music).
     * This may be combined with {@link #DIRECTORY_MUSIC},
     * {@link #DIRECTORY_PODCASTS}, {@link #DIRECTORY_NOTIFICATIONS},
     * and {@link #DIRECTORY_RINGTONES} as a series
     * of directories to categories a particular audio file as more than one
     * type.
     */
    public static java.lang.String DIRECTORY_ALARMS = "Alarms";

    /**
     * Standard directory in which to place any audio files that should be
     * in the list of notifications that the user can select (not as regular
     * music).
     * This may be combined with {@link #DIRECTORY_MUSIC},
     * {@link #DIRECTORY_PODCASTS},
     * {@link #DIRECTORY_ALARMS}, and {@link #DIRECTORY_RINGTONES} as a series
     * of directories to categories a particular audio file as more than one
     * type.
     */
    public static java.lang.String DIRECTORY_NOTIFICATIONS = "Notifications";

    /**
     * Standard directory in which to place pictures that are available to
     * the user.  Note that this is primarily a convention for the top-level
     * public directory, as the media scanner will find and collect pictures
     * in any directory.
     */
    public static java.lang.String DIRECTORY_PICTURES = "Pictures";

    /**
     * Standard directory in which to place movies that are available to
     * the user.  Note that this is primarily a convention for the top-level
     * public directory, as the media scanner will find and collect movies
     * in any directory.
     */
    public static java.lang.String DIRECTORY_MOVIES = "Movies";

    /**
     * Standard directory in which to place files that have been downloaded by
     * the user.  Note that this is primarily a convention for the top-level
     * public directory, you are free to download files anywhere in your own
     * private directories.  Also note that though the constant here is
     * named DIRECTORY_DOWNLOADS (plural), the actual file name is non-plural for
     * backwards compatibility reasons.
     */
    public static java.lang.String DIRECTORY_DOWNLOADS = "Download";

    /**
     * The traditional location for pictures and videos when mounting the
     * device as a camera.  Note that this is primarily a convention for the
     * top-level public directory, as this convention makes no sense elsewhere.
     */
    public static java.lang.String DIRECTORY_DCIM = "DCIM";

    /**
     * Standard directory in which to place documents that have been created by
     * the user.
     */
    public static java.lang.String DIRECTORY_DOCUMENTS = "Documents";

    /**
     * List of standard storage directories.
     * <p>
     * Each of its values have its own constant:
     * <ul>
     *   <li>{@link #DIRECTORY_MUSIC}
     *   <li>{@link #DIRECTORY_PODCASTS}
     *   <li>{@link #DIRECTORY_ALARMS}
     *   <li>{@link #DIRECTORY_RINGTONES}
     *   <li>{@link #DIRECTORY_NOTIFICATIONS}
     *   <li>{@link #DIRECTORY_PICTURES}
     *   <li>{@link #DIRECTORY_MOVIES}
     *   <li>{@link #DIRECTORY_DOWNLOADS}
     *   <li>{@link #DIRECTORY_DCIM}
     *   <li>{@link #DIRECTORY_DOCUMENTS}
     * </ul>
     *
     * @unknown 
     */
    public static final java.lang.String[] STANDARD_DIRECTORIES = new java.lang.String[]{ android.os.Environment.DIRECTORY_MUSIC, android.os.Environment.DIRECTORY_PODCASTS, android.os.Environment.DIRECTORY_RINGTONES, android.os.Environment.DIRECTORY_ALARMS, android.os.Environment.DIRECTORY_NOTIFICATIONS, android.os.Environment.DIRECTORY_PICTURES, android.os.Environment.DIRECTORY_MOVIES, android.os.Environment.DIRECTORY_DOWNLOADS, android.os.Environment.DIRECTORY_DCIM, android.os.Environment.DIRECTORY_DOCUMENTS };

    /**
     *
     *
     * @unknown 
     */
    public static boolean isStandardDirectory(java.lang.String dir) {
        for (java.lang.String valid : android.os.Environment.STANDARD_DIRECTORIES) {
            if (valid.equals(dir)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a top-level shared/external storage directory for placing files of a
     * particular type. This is where the user will typically place and manage
     * their own files, so you should be careful about what you put here to
     * ensure you don't erase their files or get in the way of their own
     * organization.
     * <p>
     * On devices with multiple users (as described by {@link UserManager}),
     * each user has their own isolated shared storage. Applications only have
     * access to the shared storage for the user they're running as.
     * </p>
     * <p>
     * Here is an example of typical code to manipulate a picture on the public
     * shared storage:
     * </p>
     * {@sample development/samples/ApiDemos/src/com/example/android/apis/content/ExternalStorage.java
     * public_picture}
     *
     * @param type
     * 		The type of storage directory to return. Should be one of
     * 		{@link #DIRECTORY_MUSIC}, {@link #DIRECTORY_PODCASTS},
     * 		{@link #DIRECTORY_RINGTONES}, {@link #DIRECTORY_ALARMS},
     * 		{@link #DIRECTORY_NOTIFICATIONS}, {@link #DIRECTORY_PICTURES},
     * 		{@link #DIRECTORY_MOVIES}, {@link #DIRECTORY_DOWNLOADS},
     * 		{@link #DIRECTORY_DCIM}, or {@link #DIRECTORY_DOCUMENTS}. May not be null.
     * @return Returns the File path for the directory. Note that this directory
    may not yet exist, so you must make sure it exists before using
    it such as with {@link File#mkdirs File.mkdirs()}.
     */
    public static java.io.File getExternalStoragePublicDirectory(java.lang.String type) {
        android.os.Environment.throwIfUserRequired();
        return android.os.Environment.sCurrentUser.buildExternalStoragePublicDirs(type)[0];
    }

    /**
     * Returns the path for android-specific data on the SD card.
     *
     * @unknown 
     */
    public static java.io.File[] buildExternalStorageAndroidDataDirs() {
        android.os.Environment.throwIfUserRequired();
        return android.os.Environment.sCurrentUser.buildExternalStorageAndroidDataDirs();
    }

    /**
     * Generates the raw path to an application's data
     *
     * @unknown 
     */
    public static java.io.File[] buildExternalStorageAppDataDirs(java.lang.String packageName) {
        android.os.Environment.throwIfUserRequired();
        return android.os.Environment.sCurrentUser.buildExternalStorageAppDataDirs(packageName);
    }

    /**
     * Generates the raw path to an application's media
     *
     * @unknown 
     */
    public static java.io.File[] buildExternalStorageAppMediaDirs(java.lang.String packageName) {
        android.os.Environment.throwIfUserRequired();
        return android.os.Environment.sCurrentUser.buildExternalStorageAppMediaDirs(packageName);
    }

    /**
     * Generates the raw path to an application's OBB files
     *
     * @unknown 
     */
    public static java.io.File[] buildExternalStorageAppObbDirs(java.lang.String packageName) {
        android.os.Environment.throwIfUserRequired();
        return android.os.Environment.sCurrentUser.buildExternalStorageAppObbDirs(packageName);
    }

    /**
     * Generates the path to an application's files.
     *
     * @unknown 
     */
    public static java.io.File[] buildExternalStorageAppFilesDirs(java.lang.String packageName) {
        android.os.Environment.throwIfUserRequired();
        return android.os.Environment.sCurrentUser.buildExternalStorageAppFilesDirs(packageName);
    }

    /**
     * Generates the path to an application's cache.
     *
     * @unknown 
     */
    public static java.io.File[] buildExternalStorageAppCacheDirs(java.lang.String packageName) {
        android.os.Environment.throwIfUserRequired();
        return android.os.Environment.sCurrentUser.buildExternalStorageAppCacheDirs(packageName);
    }

    /**
     * Return the download/cache content directory.
     */
    public static java.io.File getDownloadCacheDirectory() {
        return android.os.Environment.DIR_DOWNLOAD_CACHE;
    }

    /**
     * Unknown storage state, such as when a path isn't backed by known storage
     * media.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_UNKNOWN = "unknown";

    /**
     * Storage state if the media is not present.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_REMOVED = "removed";

    /**
     * Storage state if the media is present but not mounted.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_UNMOUNTED = "unmounted";

    /**
     * Storage state if the media is present and being disk-checked.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_CHECKING = "checking";

    /**
     * Storage state if the media is present but is blank or is using an
     * unsupported filesystem.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_NOFS = "nofs";

    /**
     * Storage state if the media is present and mounted at its mount point with
     * read/write access.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_MOUNTED = "mounted";

    /**
     * Storage state if the media is present and mounted at its mount point with
     * read-only access.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_MOUNTED_READ_ONLY = "mounted_ro";

    /**
     * Storage state if the media is present not mounted, and shared via USB
     * mass storage.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_SHARED = "shared";

    /**
     * Storage state if the media was removed before it was unmounted.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_BAD_REMOVAL = "bad_removal";

    /**
     * Storage state if the media is present but cannot be mounted. Typically
     * this happens if the file system on the media is corrupted.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_UNMOUNTABLE = "unmountable";

    /**
     * Storage state if the media is in the process of being ejected.
     *
     * @see #getExternalStorageState(File)
     */
    public static final java.lang.String MEDIA_EJECTING = "ejecting";

    /**
     * Returns the current state of the primary shared/external storage media.
     *
     * @see #getExternalStorageDirectory()
     * @return one of {@link #MEDIA_UNKNOWN}, {@link #MEDIA_REMOVED},
    {@link #MEDIA_UNMOUNTED}, {@link #MEDIA_CHECKING},
    {@link #MEDIA_NOFS}, {@link #MEDIA_MOUNTED},
    {@link #MEDIA_MOUNTED_READ_ONLY}, {@link #MEDIA_SHARED},
    {@link #MEDIA_BAD_REMOVAL}, or {@link #MEDIA_UNMOUNTABLE}.
     */
    public static java.lang.String getExternalStorageState() {
        final java.io.File externalDir = android.os.Environment.sCurrentUser.getExternalDirs()[0];
        return android.os.Environment.getExternalStorageState(externalDir);
    }

    /**
     *
     *
     * @deprecated use {@link #getExternalStorageState(File)}
     */
    @java.lang.Deprecated
    public static java.lang.String getStorageState(java.io.File path) {
        return android.os.Environment.getExternalStorageState(path);
    }

    /**
     * Returns the current state of the shared/external storage media at the
     * given path.
     *
     * @return one of {@link #MEDIA_UNKNOWN}, {@link #MEDIA_REMOVED},
    {@link #MEDIA_UNMOUNTED}, {@link #MEDIA_CHECKING},
    {@link #MEDIA_NOFS}, {@link #MEDIA_MOUNTED},
    {@link #MEDIA_MOUNTED_READ_ONLY}, {@link #MEDIA_SHARED},
    {@link #MEDIA_BAD_REMOVAL}, or {@link #MEDIA_UNMOUNTABLE}.
     */
    public static java.lang.String getExternalStorageState(java.io.File path) {
        final android.os.storage.StorageVolume volume = android.os.storage.StorageManager.getStorageVolume(path, android.os.UserHandle.myUserId());
        if (volume != null) {
            return volume.getState();
        } else {
            return android.os.Environment.MEDIA_UNKNOWN;
        }
    }

    /**
     * Returns whether the primary shared/external storage media is physically
     * removable.
     *
     * @return true if the storage device can be removed (such as an SD card),
    or false if the storage device is built in and cannot be
    physically removed.
     */
    public static boolean isExternalStorageRemovable() {
        if (android.os.Environment.isStorageDisabled())
            return false;

        final java.io.File externalDir = android.os.Environment.sCurrentUser.getExternalDirs()[0];
        return android.os.Environment.isExternalStorageRemovable(externalDir);
    }

    /**
     * Returns whether the shared/external storage media at the given path is
     * physically removable.
     *
     * @return true if the storage device can be removed (such as an SD card),
    or false if the storage device is built in and cannot be
    physically removed.
     * @throws IllegalArgumentException
     * 		if the path is not a valid storage
     * 		device.
     */
    public static boolean isExternalStorageRemovable(java.io.File path) {
        final android.os.storage.StorageVolume volume = android.os.storage.StorageManager.getStorageVolume(path, android.os.UserHandle.myUserId());
        if (volume != null) {
            return volume.isRemovable();
        } else {
            throw new java.lang.IllegalArgumentException("Failed to find storage device at " + path);
        }
    }

    /**
     * Returns whether the primary shared/external storage media is emulated.
     * <p>
     * The contents of emulated storage devices are backed by a private user
     * data partition, which means there is little benefit to apps storing data
     * here instead of the private directories returned by
     * {@link Context#getFilesDir()}, etc.
     * <p>
     * This returns true when emulated storage is backed by either internal
     * storage or an adopted storage device.
     *
     * @see DevicePolicyManager#setStorageEncryption(android.content.ComponentName,
    boolean)
     */
    public static boolean isExternalStorageEmulated() {
        if (android.os.Environment.isStorageDisabled())
            return false;

        final java.io.File externalDir = android.os.Environment.sCurrentUser.getExternalDirs()[0];
        return android.os.Environment.isExternalStorageEmulated(externalDir);
    }

    /**
     * Returns whether the shared/external storage media at the given path is
     * emulated.
     * <p>
     * The contents of emulated storage devices are backed by a private user
     * data partition, which means there is little benefit to apps storing data
     * here instead of the private directories returned by
     * {@link Context#getFilesDir()}, etc.
     * <p>
     * This returns true when emulated storage is backed by either internal
     * storage or an adopted storage device.
     *
     * @throws IllegalArgumentException
     * 		if the path is not a valid storage
     * 		device.
     */
    public static boolean isExternalStorageEmulated(java.io.File path) {
        final android.os.storage.StorageVolume volume = android.os.storage.StorageManager.getStorageVolume(path, android.os.UserHandle.myUserId());
        if (volume != null) {
            return volume.isEmulated();
        } else {
            throw new java.lang.IllegalArgumentException("Failed to find storage device at " + path);
        }
    }

    static java.io.File getDirectory(java.lang.String variableName, java.lang.String defaultPath) {
        java.lang.String path = java.lang.System.getenv(variableName);
        return path == null ? new java.io.File(defaultPath) : new java.io.File(path);
    }

    /**
     * {@hide }
     */
    public static void setUserRequired(boolean userRequired) {
        android.os.Environment.sUserRequired = userRequired;
    }

    private static void throwIfUserRequired() {
        if (android.os.Environment.sUserRequired) {
            android.util.Log.wtf(android.os.Environment.TAG, "Path requests must specify a user by using UserEnvironment", new java.lang.Throwable());
        }
    }

    /**
     * Append path segments to each given base path, returning result.
     *
     * @unknown 
     */
    public static java.io.File[] buildPaths(java.io.File[] base, java.lang.String... segments) {
        java.io.File[] result = new java.io.File[base.length];
        for (int i = 0; i < base.length; i++) {
            result[i] = android.os.Environment.buildPath(base[i], segments);
        }
        return result;
    }

    /**
     * Append path segments to given base path, returning result.
     *
     * @unknown 
     */
    public static java.io.File buildPath(java.io.File base, java.lang.String... segments) {
        java.io.File cur = base;
        for (java.lang.String segment : segments) {
            if (cur == null) {
                cur = new java.io.File(segment);
            } else {
                cur = new java.io.File(cur, segment);
            }
        }
        return cur;
    }

    private static boolean isStorageDisabled() {
        return android.os.SystemProperties.getBoolean("config.disable_storage", false);
    }

    /**
     * If the given path exists on emulated external storage, return the
     * translated backing path hosted on internal storage. This bypasses any
     * emulation later, improving performance. This is <em>only</em> suitable
     * for read-only access.
     * <p>
     * Returns original path if given path doesn't meet these criteria. Callers
     * must hold {@link android.Manifest.permission#WRITE_MEDIA_STORAGE}
     * permission.
     *
     * @unknown 
     */
    public static java.io.File maybeTranslateEmulatedPathToInternal(java.io.File path) {
        return android.os.storage.StorageManager.maybeTranslateEmulatedPathToInternal(path);
    }
}

