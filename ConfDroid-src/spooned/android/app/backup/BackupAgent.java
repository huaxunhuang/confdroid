/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.app.backup;


/**
 * Provides the central interface between an
 * application and Android's data backup infrastructure.  An application that wishes
 * to participate in the backup and restore mechanism will declare a subclass of
 * {@link android.app.backup.BackupAgent}, implement the
 * {@link #onBackup(ParcelFileDescriptor, BackupDataOutput, ParcelFileDescriptor) onBackup()}
 * and {@link #onRestore(BackupDataInput, int, ParcelFileDescriptor) onRestore()} methods,
 * and provide the name of its backup agent class in its {@code AndroidManifest.xml} file via
 * the <code>
 * <a href="{@docRoot }guide/topics/manifest/application-element.html">&lt;application&gt;</a></code>
 * tag's {@code android:backupAgent} attribute.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using BackupAgent, read the
 * <a href="{@docRoot }guide/topics/data/backup.html">Data Backup</a> developer guide.</p></div>
 *
 * <h3>Basic Operation</h3>
 * <p>
 * When the application makes changes to data that it wishes to keep backed up,
 * it should call the
 * {@link android.app.backup.BackupManager#dataChanged() BackupManager.dataChanged()} method.
 * This notifies the Android Backup Manager that the application needs an opportunity
 * to update its backup image.  The Backup Manager, in turn, schedules a
 * backup pass to be performed at an opportune time.
 * <p>
 * Restore operations are typically performed only when applications are first
 * installed on a device.  At that time, the operating system checks to see whether
 * there is a previously-saved data set available for the application being installed, and if so,
 * begins an immediate restore pass to deliver the backup data as part of the installation
 * process.
 * <p>
 * When a backup or restore pass is run, the application's process is launched
 * (if not already running), the manifest-declared backup agent class (in the {@code android:backupAgent} attribute) is instantiated within
 * that process, and the agent's {@link #onCreate()} method is invoked.  This prepares the
 * agent instance to run the actual backup or restore logic.  At this point the
 * agent's
 * {@link #onBackup(ParcelFileDescriptor, BackupDataOutput, ParcelFileDescriptor) onBackup()} or
 * {@link #onRestore(BackupDataInput, int, ParcelFileDescriptor) onRestore()} method will be
 * invoked as appropriate for the operation being performed.
 * <p>
 * A backup data set consists of one or more "entities," flattened binary data
 * records that are each identified with a key string unique within the data set.  Adding a
 * record to the active data set or updating an existing record is done by simply
 * writing new entity data under the desired key.  Deleting an entity from the data set
 * is done by writing an entity under that key with header specifying a negative data
 * size, and no actual entity data.
 * <p>
 * <b>Helper Classes</b>
 * <p>
 * An extensible agent based on convenient helper classes is available in
 * {@link android.app.backup.BackupAgentHelper}.  That class is particularly
 * suited to handling of simple file or {@link android.content.SharedPreferences}
 * backup and restore.
 *
 * @see android.app.backup.BackupManager
 * @see android.app.backup.BackupAgentHelper
 * @see android.app.backup.BackupDataInput
 * @see android.app.backup.BackupDataOutput
 */
public abstract class BackupAgent extends android.content.ContextWrapper {
    private static final java.lang.String TAG = "BackupAgent";

    private static final boolean DEBUG = false;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_EOF = 0;

    /**
     * During a full restore, indicates that the file system object being restored
     * is an ordinary file.
     */
    public static final int TYPE_FILE = 1;

    /**
     * During a full restore, indicates that the file system object being restored
     * is a directory.
     */
    public static final int TYPE_DIRECTORY = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_SYMLINK = 3;

    android.os.Handler mHandler = null;

    android.os.Handler getHandler() {
        if (mHandler == null) {
            mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        }
        return mHandler;
    }

    class SharedPrefsSynchronizer implements java.lang.Runnable {
        public final java.util.concurrent.CountDownLatch mLatch = new java.util.concurrent.CountDownLatch(1);

        @java.lang.Override
        public void run() {
            android.app.QueuedWork.waitToFinish();
            mLatch.countDown();
        }
    }

    // Syncing shared preferences deferred writes needs to happen on the main looper thread
    private void waitForSharedPrefs() {
        android.os.Handler h = getHandler();
        final android.app.backup.BackupAgent.SharedPrefsSynchronizer s = new android.app.backup.BackupAgent.SharedPrefsSynchronizer();
        h.postAtFrontOfQueue(s);
        try {
            s.mLatch.await();
        } catch (java.lang.InterruptedException e) {
            /* ignored */
        }
    }

    public BackupAgent() {
        super(null);
    }

    /**
     * Provided as a convenience for agent implementations that need an opportunity
     * to do one-time initialization before the actual backup or restore operation
     * is begun.
     * <p>
     */
    public void onCreate() {
    }

    /**
     * Provided as a convenience for agent implementations that need to do some
     * sort of shutdown process after backup or restore is completed.
     * <p>
     * Agents do not need to override this method.
     */
    public void onDestroy() {
    }

    /**
     * The application is being asked to write any data changed since the last
     * time it performed a backup operation. The state data recorded during the
     * last backup pass is provided in the <code>oldState</code> file
     * descriptor. If <code>oldState</code> is <code>null</code>, no old state
     * is available and the application should perform a full backup. In both
     * cases, a representation of the final backup state after this pass should
     * be written to the file pointed to by the file descriptor wrapped in
     * <code>newState</code>.
     * <p>
     * Each entity written to the {@link android.app.backup.BackupDataOutput}
     * <code>data</code> stream will be transmitted
     * over the current backup transport and stored in the remote data set under
     * the key supplied as part of the entity.  Writing an entity with a negative
     * data size instructs the transport to delete whatever entity currently exists
     * under that key from the remote data set.
     *
     * @param oldState
     * 		An open, read-only ParcelFileDescriptor pointing to the
     * 		last backup state provided by the application. May be
     * 		<code>null</code>, in which case no prior state is being
     * 		provided and the application should perform a full backup.
     * @param data
     * 		A structured wrapper around an open, read/write
     * 		file descriptor pointing to the backup data destination.
     * 		Typically the application will use backup helper classes to
     * 		write to this file.
     * @param newState
     * 		An open, read/write ParcelFileDescriptor pointing to an
     * 		empty file. The application should record the final backup
     * 		state here after writing the requested data to the <code>data</code>
     * 		output stream.
     */
    public abstract void onBackup(android.os.ParcelFileDescriptor oldState, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newState) throws java.io.IOException;

    /**
     * The application is being restored from backup and should replace any
     * existing data with the contents of the backup. The backup data is
     * provided through the <code>data</code> parameter. Once
     * the restore is finished, the application should write a representation of
     * the final state to the <code>newState</code> file descriptor.
     * <p>
     * The application is responsible for properly erasing its old data and
     * replacing it with the data supplied to this method. No "clear user data"
     * operation will be performed automatically by the operating system. The
     * exception to this is in the case of a failed restore attempt: if
     * onRestore() throws an exception, the OS will assume that the
     * application's data may now be in an incoherent state, and will clear it
     * before proceeding.
     *
     * @param data
     * 		A structured wrapper around an open, read-only
     * 		file descriptor pointing to a full snapshot of the
     * 		application's data.  The application should consume every
     * 		entity represented in this data stream.
     * @param appVersionCode
     * 		The value of the <a
     * 		href="{@docRoot }guide/topics/manifest/manifest-element.html#vcode">{@code android:versionCode}</a> manifest attribute,
     * 		from the application that backed up this particular data set. This
     * 		makes it possible for an application's agent to distinguish among any
     * 		possible older data versions when asked to perform the restore
     * 		operation.
     * @param newState
     * 		An open, read/write ParcelFileDescriptor pointing to an
     * 		empty file. The application should record the final backup
     * 		state here after restoring its data from the <code>data</code> stream.
     * 		When a full-backup dataset is being restored, this will be <code>null</code>.
     */
    public abstract void onRestore(android.app.backup.BackupDataInput data, int appVersionCode, android.os.ParcelFileDescriptor newState) throws java.io.IOException;

    /**
     * The application is having its entire file system contents backed up.  {@code data}
     * points to the backup destination, and the app has the opportunity to choose which
     * files are to be stored.  To commit a file as part of the backup, call the
     * {@link #fullBackupFile(File, FullBackupDataOutput)} helper method.  After all file
     * data is written to the output, the agent returns from this method and the backup
     * operation concludes.
     *
     * <p>Certain parts of the app's data are never backed up even if the app explicitly
     * sends them to the output:
     *
     * <ul>
     * <li>The contents of the {@link #getCacheDir()} directory</li>
     * <li>The contents of the {@link #getCodeCacheDir()} directory</li>
     * <li>The contents of the {@link #getNoBackupFilesDir()} directory</li>
     * <li>The contents of the app's shared library directory</li>
     * </ul>
     *
     * <p>The default implementation of this method backs up the entirety of the
     * application's "owned" file system trees to the output other than the few exceptions
     * listed above.  Apps only need to override this method if they need to impose special
     * limitations on which files are being stored beyond the control that
     * {@link #getNoBackupFilesDir()} offers.
     * Alternatively they can provide an xml resource to specify what data to include or exclude.
     *
     * @param data
     * 		A structured wrapper pointing to the backup destination.
     * @throws IOException
     * 		
     * @see Context#getNoBackupFilesDir()
     * @see ApplicationInfo#fullBackupContent
     * @see #fullBackupFile(File, FullBackupDataOutput)
     * @see #onRestoreFile(ParcelFileDescriptor, long, File, int, long, long)
     */
    public void onFullBackup(android.app.backup.FullBackupDataOutput data) throws java.io.IOException {
        android.app.backup.FullBackup.BackupScheme backupScheme = android.app.backup.FullBackup.getBackupScheme(this);
        if (!backupScheme.isFullBackupContentEnabled()) {
            return;
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> manifestIncludeMap;
        android.util.ArraySet<java.lang.String> manifestExcludeSet;
        try {
            manifestIncludeMap = backupScheme.maybeParseAndGetCanonicalIncludePaths();
            manifestExcludeSet = backupScheme.maybeParseAndGetCanonicalExcludePaths();
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "Exception trying to parse fullBackupContent xml file!" + " Aborting full backup.", e);
            }
            return;
        }
        final java.lang.String packageName = getPackageName();
        final android.content.pm.ApplicationInfo appInfo = getApplicationInfo();
        // System apps have control over where their default storage context
        // is pointed, so we're always explicit when building paths.
        final android.content.Context ceContext = createCredentialProtectedStorageContext();
        final java.lang.String rootDir = ceContext.getDataDir().getCanonicalPath();
        final java.lang.String filesDir = ceContext.getFilesDir().getCanonicalPath();
        final java.lang.String noBackupDir = ceContext.getNoBackupFilesDir().getCanonicalPath();
        final java.lang.String databaseDir = ceContext.getDatabasePath("foo").getParentFile().getCanonicalPath();
        final java.lang.String sharedPrefsDir = ceContext.getSharedPreferencesPath("foo").getParentFile().getCanonicalPath();
        final java.lang.String cacheDir = ceContext.getCacheDir().getCanonicalPath();
        final java.lang.String codeCacheDir = ceContext.getCodeCacheDir().getCanonicalPath();
        final android.content.Context deContext = createDeviceProtectedStorageContext();
        final java.lang.String deviceRootDir = deContext.getDataDir().getCanonicalPath();
        final java.lang.String deviceFilesDir = deContext.getFilesDir().getCanonicalPath();
        final java.lang.String deviceNoBackupDir = deContext.getNoBackupFilesDir().getCanonicalPath();
        final java.lang.String deviceDatabaseDir = deContext.getDatabasePath("foo").getParentFile().getCanonicalPath();
        final java.lang.String deviceSharedPrefsDir = deContext.getSharedPreferencesPath("foo").getParentFile().getCanonicalPath();
        final java.lang.String deviceCacheDir = deContext.getCacheDir().getCanonicalPath();
        final java.lang.String deviceCodeCacheDir = deContext.getCodeCacheDir().getCanonicalPath();
        final java.lang.String libDir = (appInfo.nativeLibraryDir != null) ? new java.io.File(appInfo.nativeLibraryDir).getCanonicalPath() : null;
        // Maintain a set of excluded directories so that as we traverse the tree we know we're not
        // going places we don't expect, and so the manifest includes can't take precedence over
        // what the framework decides is not to be included.
        final android.util.ArraySet<java.lang.String> traversalExcludeSet = new android.util.ArraySet<java.lang.String>();
        // Add the directories we always exclude.
        traversalExcludeSet.add(filesDir);
        traversalExcludeSet.add(noBackupDir);
        traversalExcludeSet.add(databaseDir);
        traversalExcludeSet.add(sharedPrefsDir);
        traversalExcludeSet.add(cacheDir);
        traversalExcludeSet.add(codeCacheDir);
        traversalExcludeSet.add(deviceFilesDir);
        traversalExcludeSet.add(deviceNoBackupDir);
        traversalExcludeSet.add(deviceDatabaseDir);
        traversalExcludeSet.add(deviceSharedPrefsDir);
        traversalExcludeSet.add(deviceCacheDir);
        traversalExcludeSet.add(deviceCodeCacheDir);
        if (libDir != null) {
            traversalExcludeSet.add(libDir);
        }
        // Root dir first.
        applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.ROOT_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
        traversalExcludeSet.add(rootDir);
        applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.DEVICE_ROOT_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
        traversalExcludeSet.add(deviceRootDir);
        // Data dir next.
        traversalExcludeSet.remove(filesDir);
        applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.FILES_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
        traversalExcludeSet.add(filesDir);
        traversalExcludeSet.remove(deviceFilesDir);
        applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.DEVICE_FILES_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
        traversalExcludeSet.add(deviceFilesDir);
        // Database directory.
        traversalExcludeSet.remove(databaseDir);
        applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.DATABASE_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
        traversalExcludeSet.add(databaseDir);
        traversalExcludeSet.remove(deviceDatabaseDir);
        applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.DEVICE_DATABASE_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
        traversalExcludeSet.add(deviceDatabaseDir);
        // SharedPrefs.
        traversalExcludeSet.remove(sharedPrefsDir);
        applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.SHAREDPREFS_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
        traversalExcludeSet.add(sharedPrefsDir);
        traversalExcludeSet.remove(deviceSharedPrefsDir);
        applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.DEVICE_SHAREDPREFS_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
        traversalExcludeSet.add(deviceSharedPrefsDir);
        // getExternalFilesDir() location associated with this app.  Technically there should
        // not be any files here if the app does not properly have permission to access
        // external storage, but edge cases happen. fullBackupFileTree() catches
        // IOExceptions and similar, and treats them as non-fatal, so we rely on that; and
        // we know a priori that processes running as the system UID are not permitted to
        // access external storage, so we check for that as well to avoid nastygrams in
        // the log.
        if (android.os.Process.myUid() != android.os.Process.SYSTEM_UID) {
            java.io.File efLocation = getExternalFilesDir(null);
            if (efLocation != null) {
                applyXmlFiltersAndDoFullBackupForDomain(packageName, android.app.backup.FullBackup.MANAGED_EXTERNAL_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            }
        }
    }

    /**
     * Notification that the application's current backup operation causes it to exceed
     * the maximum size permitted by the transport.  The ongoing backup operation is
     * halted and rolled back: any data that had been stored by a previous backup operation
     * is still intact.  Typically the quota-exceeded state will be detected before any data
     * is actually transmitted over the network.
     *
     * <p>The {@code quotaBytes} value is the total data size currently permitted for this
     * application.  If desired, the application can use this as a hint for determining
     * how much data to store.  For example, a messaging application might choose to
     * store only the newest messages, dropping enough older content to stay under
     * the quota.
     *
     * <p class="note">Note that the maximum quota for the application can change over
     * time.  In particular, in the future the quota may grow.  Applications that adapt
     * to the quota when deciding what data to store should be aware of this and implement
     * their data storage mechanisms in a way that can take advantage of additional
     * quota.
     *
     * @param backupDataBytes
     * 		The amount of data measured while initializing the backup
     * 		operation, if the total exceeds the app's alloted quota.  If initial measurement
     * 		suggested that the data would fit but then too much data was actually submitted
     * 		as part of the operation, then this value is the amount of data that had been
     * 		streamed into the transport at the time the quota was reached.
     * @param quotaBytes
     * 		The maximum data size that the transport currently permits
     * 		this application to store as a backup.
     */
    public void onQuotaExceeded(long backupDataBytes, long quotaBytes) {
    }

    /**
     * Check whether the xml yielded any <include/> tag for the provided <code>domainToken</code>.
     * If so, perform a {@link #fullBackupFileTree} which backs up the file or recurses if the path
     * is a directory.
     */
    private void applyXmlFiltersAndDoFullBackupForDomain(java.lang.String packageName, java.lang.String domainToken, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> includeMap, android.util.ArraySet<java.lang.String> filterSet, android.util.ArraySet<java.lang.String> traversalExcludeSet, android.app.backup.FullBackupDataOutput data) throws java.io.IOException {
        if ((includeMap == null) || (includeMap.size() == 0)) {
            // Do entire sub-tree for the provided token.
            fullBackupFileTree(packageName, domainToken, android.app.backup.FullBackup.getBackupScheme(this).tokenToDirectoryPath(domainToken), filterSet, traversalExcludeSet, data);
        } else
            if (includeMap.get(domainToken) != null) {
                // This will be null if the xml parsing didn't yield any rules for
                // this domain (there may still be rules for other domains).
                for (java.lang.String includeFile : includeMap.get(domainToken)) {
                    fullBackupFileTree(packageName, domainToken, includeFile, filterSet, traversalExcludeSet, data);
                }
            }

    }

    /**
     * Write an entire file as part of a full-backup operation.  The file's contents
     * will be delivered to the backup destination along with the metadata necessary
     * to place it with the proper location and permissions on the device where the
     * data is restored.
     *
     * <p class="note">Attempting to back up files in directories that are ignored by
     * the backup system will have no effect.  For example, if the app calls this method
     * with a file inside the {@link #getNoBackupFilesDir()} directory, it will be ignored.
     * See {@link #onFullBackup(FullBackupDataOutput) for details on what directories
     * are excluded from backups.
     *
     * @param file
     * 		The file to be backed up.  The file must exist and be readable by
     * 		the caller.
     * @param output
     * 		The destination to which the backed-up file data will be sent.
     */
    public final void fullBackupFile(java.io.File file, android.app.backup.FullBackupDataOutput output) {
        // Look up where all of our various well-defined dir trees live on this device
        final java.lang.String rootDir;
        final java.lang.String filesDir;
        final java.lang.String nbFilesDir;
        final java.lang.String dbDir;
        final java.lang.String spDir;
        final java.lang.String cacheDir;
        final java.lang.String codeCacheDir;
        final java.lang.String deviceRootDir;
        final java.lang.String deviceFilesDir;
        final java.lang.String deviceNbFilesDir;
        final java.lang.String deviceDbDir;
        final java.lang.String deviceSpDir;
        final java.lang.String deviceCacheDir;
        final java.lang.String deviceCodeCacheDir;
        final java.lang.String libDir;
        java.lang.String efDir = null;
        java.lang.String filePath;
        android.content.pm.ApplicationInfo appInfo = getApplicationInfo();
        try {
            // System apps have control over where their default storage context
            // is pointed, so we're always explicit when building paths.
            final android.content.Context ceContext = createCredentialProtectedStorageContext();
            rootDir = ceContext.getDataDir().getCanonicalPath();
            filesDir = ceContext.getFilesDir().getCanonicalPath();
            nbFilesDir = ceContext.getNoBackupFilesDir().getCanonicalPath();
            dbDir = ceContext.getDatabasePath("foo").getParentFile().getCanonicalPath();
            spDir = ceContext.getSharedPreferencesPath("foo").getParentFile().getCanonicalPath();
            cacheDir = ceContext.getCacheDir().getCanonicalPath();
            codeCacheDir = ceContext.getCodeCacheDir().getCanonicalPath();
            final android.content.Context deContext = createDeviceProtectedStorageContext();
            deviceRootDir = deContext.getDataDir().getCanonicalPath();
            deviceFilesDir = deContext.getFilesDir().getCanonicalPath();
            deviceNbFilesDir = deContext.getNoBackupFilesDir().getCanonicalPath();
            deviceDbDir = deContext.getDatabasePath("foo").getParentFile().getCanonicalPath();
            deviceSpDir = deContext.getSharedPreferencesPath("foo").getParentFile().getCanonicalPath();
            deviceCacheDir = deContext.getCacheDir().getCanonicalPath();
            deviceCodeCacheDir = deContext.getCodeCacheDir().getCanonicalPath();
            libDir = (appInfo.nativeLibraryDir == null) ? null : new java.io.File(appInfo.nativeLibraryDir).getCanonicalPath();
            // may or may not have external files access to attempt backup/restore there
            if (android.os.Process.myUid() != android.os.Process.SYSTEM_UID) {
                java.io.File efLocation = getExternalFilesDir(null);
                if (efLocation != null) {
                    efDir = efLocation.getCanonicalPath();
                }
            }
            // Now figure out which well-defined tree the file is placed in, working from
            // most to least specific.  We also specifically exclude the lib, cache,
            // and code_cache dirs.
            filePath = file.getCanonicalPath();
        } catch (java.io.IOException e) {
            android.util.Log.w(android.app.backup.BackupAgent.TAG, "Unable to obtain canonical paths");
            return;
        }
        if ((((((filePath.startsWith(cacheDir) || filePath.startsWith(codeCacheDir)) || filePath.startsWith(nbFilesDir)) || filePath.startsWith(deviceCacheDir)) || filePath.startsWith(deviceCodeCacheDir)) || filePath.startsWith(deviceNbFilesDir)) || filePath.startsWith(libDir)) {
            android.util.Log.w(android.app.backup.BackupAgent.TAG, "lib, cache, code_cache, and no_backup files are not backed up");
            return;
        }
        final java.lang.String domain;
        java.lang.String rootpath = null;
        if (filePath.startsWith(dbDir)) {
            domain = android.app.backup.FullBackup.DATABASE_TREE_TOKEN;
            rootpath = dbDir;
        } else
            if (filePath.startsWith(spDir)) {
                domain = android.app.backup.FullBackup.SHAREDPREFS_TREE_TOKEN;
                rootpath = spDir;
            } else
                if (filePath.startsWith(filesDir)) {
                    domain = android.app.backup.FullBackup.FILES_TREE_TOKEN;
                    rootpath = filesDir;
                } else
                    if (filePath.startsWith(rootDir)) {
                        domain = android.app.backup.FullBackup.ROOT_TREE_TOKEN;
                        rootpath = rootDir;
                    } else
                        if (filePath.startsWith(deviceDbDir)) {
                            domain = android.app.backup.FullBackup.DEVICE_DATABASE_TREE_TOKEN;
                            rootpath = deviceDbDir;
                        } else
                            if (filePath.startsWith(deviceSpDir)) {
                                domain = android.app.backup.FullBackup.DEVICE_SHAREDPREFS_TREE_TOKEN;
                                rootpath = deviceSpDir;
                            } else
                                if (filePath.startsWith(deviceFilesDir)) {
                                    domain = android.app.backup.FullBackup.DEVICE_FILES_TREE_TOKEN;
                                    rootpath = deviceFilesDir;
                                } else
                                    if (filePath.startsWith(deviceRootDir)) {
                                        domain = android.app.backup.FullBackup.DEVICE_ROOT_TREE_TOKEN;
                                        rootpath = deviceRootDir;
                                    } else
                                        if ((efDir != null) && filePath.startsWith(efDir)) {
                                            domain = android.app.backup.FullBackup.MANAGED_EXTERNAL_TREE_TOKEN;
                                            rootpath = efDir;
                                        } else {
                                            android.util.Log.w(android.app.backup.BackupAgent.TAG, ("File " + filePath) + " is in an unsupported location; skipping");
                                            return;
                                        }








        // And now that we know where it lives, semantically, back it up appropriately
        // In the measurement case, backupToTar() updates the size in output and returns
        // without transmitting any file data.
        if (android.app.backup.BackupAgent.DEBUG)
            android.util.Log.i(android.app.backup.BackupAgent.TAG, (((("backupFile() of " + filePath) + " => domain=") + domain) + " rootpath=") + rootpath);

        android.app.backup.FullBackup.backupToTar(getPackageName(), domain, null, rootpath, filePath, output);
    }

    /**
     * Scan the dir tree (if it actually exists) and process each entry we find.  If the
     * 'excludes' parameters are non-null, they are consulted each time a new file system entity
     * is visited to see whether that entity (and its subtree, if appropriate) should be
     * omitted from the backup process.
     *
     * @param systemExcludes
     * 		An optional list of excludes.
     * @unknown 
     */
    protected final void fullBackupFileTree(java.lang.String packageName, java.lang.String domain, java.lang.String startingPath, android.util.ArraySet<java.lang.String> manifestExcludes, android.util.ArraySet<java.lang.String> systemExcludes, android.app.backup.FullBackupDataOutput output) {
        // Pull out the domain and set it aside to use when making the tarball.
        java.lang.String domainPath = android.app.backup.FullBackup.getBackupScheme(this).tokenToDirectoryPath(domain);
        if (domainPath == null) {
            // Should never happen.
            return;
        }
        java.io.File rootFile = new java.io.File(startingPath);
        if (rootFile.exists()) {
            java.util.LinkedList<java.io.File> scanQueue = new java.util.LinkedList<java.io.File>();
            scanQueue.add(rootFile);
            while (scanQueue.size() > 0) {
                java.io.File file = scanQueue.remove(0);
                java.lang.String filePath;
                try {
                    // Ignore things that aren't "real" files or dirs
                    android.system.StructStat stat = android.system.Os.lstat(file.getPath());
                    if ((!android.system.OsConstants.S_ISREG(stat.st_mode)) && (!android.system.OsConstants.S_ISDIR(stat.st_mode))) {
                        if (android.app.backup.BackupAgent.DEBUG)
                            android.util.Log.i(android.app.backup.BackupAgent.TAG, "Not a file/dir (skipping)!: " + file);

                        continue;
                    }
                    // For all other verification, look at the canonicalized path
                    filePath = file.getCanonicalPath();
                    // prune this subtree?
                    if ((manifestExcludes != null) && manifestExcludes.contains(filePath)) {
                        continue;
                    }
                    if ((systemExcludes != null) && systemExcludes.contains(filePath)) {
                        continue;
                    }
                    // If it's a directory, enqueue its contents for scanning.
                    if (android.system.OsConstants.S_ISDIR(stat.st_mode)) {
                        java.io.File[] contents = file.listFiles();
                        if (contents != null) {
                            for (java.io.File entry : contents) {
                                scanQueue.add(0, entry);
                            }
                        }
                    }
                } catch (java.io.IOException e) {
                    if (android.app.backup.BackupAgent.DEBUG)
                        android.util.Log.w(android.app.backup.BackupAgent.TAG, "Error canonicalizing path of " + file);

                    if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                        android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "Error canonicalizing path of " + file);
                    }
                    continue;
                } catch (android.system.ErrnoException e) {
                    if (android.app.backup.BackupAgent.DEBUG)
                        android.util.Log.w(android.app.backup.BackupAgent.TAG, (("Error scanning file " + file) + " : ") + e);

                    if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                        android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, (("Error scanning file " + file) + " : ") + e);
                    }
                    continue;
                }
                // Finally, back this file up (or measure it) before proceeding
                android.app.backup.FullBackup.backupToTar(packageName, domain, null, domainPath, filePath, output);
            } 
        }
    }

    /**
     * Handle the data delivered via the given file descriptor during a full restore
     * operation.  The agent is given the path to the file's original location as well
     * as its size and metadata.
     * <p>
     * The file descriptor can only be read for {@code size} bytes; attempting to read
     * more data has undefined behavior.
     * <p>
     * The default implementation creates the destination file/directory and populates it
     * with the data from the file descriptor, then sets the file's access mode and
     * modification time to match the restore arguments.
     *
     * @param data
     * 		A read-only file descriptor from which the agent can read {@code size}
     * 		bytes of file data.
     * @param size
     * 		The number of bytes of file content to be restored to the given
     * 		destination.  If the file system object being restored is a directory, {@code size}
     * 		will be zero.
     * @param destination
     * 		The File on disk to be restored with the given data.
     * @param type
     * 		The kind of file system object being restored.  This will be either
     * 		{@link BackupAgent#TYPE_FILE} or {@link BackupAgent#TYPE_DIRECTORY}.
     * @param mode
     * 		The access mode to be assigned to the destination after its data is
     * 		written.  This is in the standard format used by {@code chmod()}.
     * @param mtime
     * 		The modification time of the file when it was backed up, suitable to
     * 		be assigned to the file after its data is written.
     * @throws IOException
     * 		
     */
    public void onRestoreFile(android.os.ParcelFileDescriptor data, long size, java.io.File destination, int type, long mode, long mtime) throws java.io.IOException {
        final boolean accept = isFileEligibleForRestore(destination);
        // If we don't accept the file, consume the bytes from the pipe anyway.
        android.app.backup.FullBackup.restoreFile(data, size, type, mode, mtime, accept ? destination : null);
    }

    private boolean isFileEligibleForRestore(java.io.File destination) throws java.io.IOException {
        android.app.backup.FullBackup.BackupScheme bs = android.app.backup.FullBackup.getBackupScheme(this);
        if (!bs.isFullBackupContentEnabled()) {
            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, (("onRestoreFile \"" + destination.getCanonicalPath()) + "\" : fullBackupContent not enabled for ") + getPackageName());
            }
            return false;
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> includes = null;
        android.util.ArraySet<java.lang.String> excludes = null;
        final java.lang.String destinationCanonicalPath = destination.getCanonicalPath();
        try {
            includes = bs.maybeParseAndGetCanonicalIncludePaths();
            excludes = bs.maybeParseAndGetCanonicalExcludePaths();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, (("onRestoreFile \"" + destinationCanonicalPath) + "\" : Exception trying to parse fullBackupContent xml file!") + " Aborting onRestoreFile.", e);
            }
            return false;
        }
        if ((excludes != null) && isFileSpecifiedInPathList(destination, excludes)) {
            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, (("onRestoreFile: \"" + destinationCanonicalPath) + "\": listed in") + " excludes; skipping.");
            }
            return false;
        }
        if ((includes != null) && (!includes.isEmpty())) {
            // Rather than figure out the <include/> domain based on the path (a lot of code, and
            // it's a small list), we'll go through and look for it.
            boolean explicitlyIncluded = false;
            for (java.util.Set<java.lang.String> domainIncludes : includes.values()) {
                explicitlyIncluded |= isFileSpecifiedInPathList(destination, domainIncludes);
                if (explicitlyIncluded) {
                    break;
                }
            }
            if (!explicitlyIncluded) {
                if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                    android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, (("onRestoreFile: Trying to restore \"" + destinationCanonicalPath) + "\" but it isn\'t specified") + " in the included files; skipping.");
                }
                return false;
            }
        }
        return true;
    }

    /**
     *
     *
     * @return True if the provided file is either directly in the provided list, or the provided
    file is within a directory in the list.
     */
    private boolean isFileSpecifiedInPathList(java.io.File file, java.util.Collection<java.lang.String> canonicalPathList) throws java.io.IOException {
        for (java.lang.String canonicalPath : canonicalPathList) {
            java.io.File fileFromList = new java.io.File(canonicalPath);
            if (fileFromList.isDirectory()) {
                if (file.isDirectory()) {
                    // If they are both directories check exact equals.
                    return file.equals(fileFromList);
                } else {
                    // O/w we have to check if the file is within the directory from the list.
                    return file.getCanonicalPath().startsWith(canonicalPath);
                }
            } else {
                if (file.equals(fileFromList)) {
                    // Need to check the explicit "equals" so we don't end up with substrings.
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Only specialized platform agents should overload this entry point to support
     * restores to crazy non-app locations.
     *
     * @unknown 
     */
    protected void onRestoreFile(android.os.ParcelFileDescriptor data, long size, int type, java.lang.String domain, java.lang.String path, long mode, long mtime) throws java.io.IOException {
        java.lang.String basePath = null;
        if (android.app.backup.BackupAgent.DEBUG)
            android.util.Log.d(android.app.backup.BackupAgent.TAG, (((((((((("onRestoreFile() size=" + size) + " type=") + type) + " domain=") + domain) + " relpath=") + path) + " mode=") + mode) + " mtime=") + mtime);

        basePath = android.app.backup.FullBackup.getBackupScheme(this).tokenToDirectoryPath(domain);
        if (domain.equals(android.app.backup.FullBackup.MANAGED_EXTERNAL_TREE_TOKEN)) {
            mode = -1;// < 0 is a token to skip attempting a chmod()

        }
        // Now that we've figured out where the data goes, send it on its way
        if (basePath != null) {
            // Canonicalize the nominal path and verify that it lies within the stated domain
            java.io.File outFile = new java.io.File(basePath, path);
            java.lang.String outPath = outFile.getCanonicalPath();
            if (outPath.startsWith(basePath + java.io.File.separatorChar)) {
                if (android.app.backup.BackupAgent.DEBUG)
                    android.util.Log.i(android.app.backup.BackupAgent.TAG, (((("[" + domain) + " : ") + path) + "] mapped to ") + outPath);

                onRestoreFile(data, size, outFile, type, mode, mtime);
                return;
            } else {
                // Attempt to restore to a path outside the file's nominal domain.
                if (android.app.backup.BackupAgent.DEBUG) {
                    android.util.Log.e(android.app.backup.BackupAgent.TAG, "Cross-domain restore attempt: " + outPath);
                }
            }
        }
        // Not a supported output location, or bad path:  we need to consume the data
        // anyway, so just use the default "copy the data out" implementation
        // with a null destination.
        if (android.app.backup.BackupAgent.DEBUG)
            android.util.Log.i(android.app.backup.BackupAgent.TAG, ("[ skipping file " + path) + "]");

        android.app.backup.FullBackup.restoreFile(data, size, type, mode, mtime, null);
    }

    /**
     * The application's restore operation has completed.  This method is called after
     * all available data has been delivered to the application for restore (via either
     * the {@link #onRestore(BackupDataInput, int, ParcelFileDescriptor) onRestore()} or
     * {@link #onRestoreFile(ParcelFileDescriptor, long, File, int, long, long) onRestoreFile()}
     * callbacks).  This provides the app with a stable end-of-restore opportunity to
     * perform any appropriate post-processing on the data that was just delivered.
     *
     * @see #onRestore(BackupDataInput, int, ParcelFileDescriptor)
     * @see #onRestoreFile(ParcelFileDescriptor, long, File, int, long, long)
     */
    public void onRestoreFinished() {
    }

    // ----- Core implementation -----
    /**
     *
     *
     * @unknown 
     */
    public final android.os.IBinder onBind() {
        return mBinder;
    }

    private final android.os.IBinder mBinder = new android.app.backup.BackupAgent.BackupServiceBinder().asBinder();

    /**
     *
     *
     * @unknown 
     */
    public void attach(android.content.Context context) {
        attachBaseContext(context);
    }

    // ----- IBackupService binder interface -----
    private class BackupServiceBinder extends android.app.IBackupAgent.Stub {
        private static final java.lang.String TAG = "BackupServiceBinder";

        @java.lang.Override
        public void doBackup(android.os.ParcelFileDescriptor oldState, android.os.ParcelFileDescriptor data, android.os.ParcelFileDescriptor newState, int token, android.app.backup.IBackupManager callbackBinder) throws android.os.RemoteException {
            // Ensure that we're running with the app's normal permission level
            long ident = android.os.Binder.clearCallingIdentity();
            if (android.app.backup.BackupAgent.DEBUG)
                android.util.Log.v(android.app.backup.BackupAgent.BackupServiceBinder.TAG, "doBackup() invoked");

            android.app.backup.BackupDataOutput output = new android.app.backup.BackupDataOutput(data.getFileDescriptor());
            try {
                android.app.backup.BackupAgent.this.onBackup(oldState, output, newState);
            } catch (java.io.IOException ex) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onBackup (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", ex);
                throw new java.lang.RuntimeException(ex);
            } catch (java.lang.RuntimeException ex) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onBackup (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", ex);
                throw ex;
            } finally {
                // Ensure that any SharedPreferences writes have landed after the backup,
                // in case the app code has side effects (since apps cannot provide this
                // guarantee themselves).
                waitForSharedPrefs();
                android.os.Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (android.os.RemoteException e) {
                    // we'll time out anyway, so we're safe
                }
                // Don't close the fd out from under the system service if this was local
                if (android.os.Binder.getCallingPid() != android.os.Process.myPid()) {
                    libcore.io.IoUtils.closeQuietly(oldState);
                    libcore.io.IoUtils.closeQuietly(data);
                    libcore.io.IoUtils.closeQuietly(newState);
                }
            }
        }

        @java.lang.Override
        public void doRestore(android.os.ParcelFileDescriptor data, int appVersionCode, android.os.ParcelFileDescriptor newState, int token, android.app.backup.IBackupManager callbackBinder) throws android.os.RemoteException {
            // Ensure that we're running with the app's normal permission level
            long ident = android.os.Binder.clearCallingIdentity();
            if (android.app.backup.BackupAgent.DEBUG)
                android.util.Log.v(android.app.backup.BackupAgent.BackupServiceBinder.TAG, "doRestore() invoked");

            android.app.backup.BackupDataInput input = new android.app.backup.BackupDataInput(data.getFileDescriptor());
            try {
                android.app.backup.BackupAgent.this.onRestore(input, appVersionCode, newState);
            } catch (java.io.IOException ex) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onRestore (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", ex);
                throw new java.lang.RuntimeException(ex);
            } catch (java.lang.RuntimeException ex) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onRestore (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", ex);
                throw ex;
            } finally {
                // Ensure that any side-effect SharedPreferences writes have landed
                waitForSharedPrefs();
                android.os.Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (android.os.RemoteException e) {
                    // we'll time out anyway, so we're safe
                }
                if (android.os.Binder.getCallingPid() != android.os.Process.myPid()) {
                    libcore.io.IoUtils.closeQuietly(data);
                    libcore.io.IoUtils.closeQuietly(newState);
                }
            }
        }

        @java.lang.Override
        public void doFullBackup(android.os.ParcelFileDescriptor data, int token, android.app.backup.IBackupManager callbackBinder) {
            // Ensure that we're running with the app's normal permission level
            long ident = android.os.Binder.clearCallingIdentity();
            if (android.app.backup.BackupAgent.DEBUG)
                android.util.Log.v(android.app.backup.BackupAgent.BackupServiceBinder.TAG, "doFullBackup() invoked");

            // Ensure that any SharedPreferences writes have landed *before*
            // we potentially try to back up the underlying files directly.
            waitForSharedPrefs();
            try {
                android.app.backup.BackupAgent.this.onFullBackup(new android.app.backup.FullBackupDataOutput(data));
            } catch (java.io.IOException ex) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onFullBackup (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", ex);
                throw new java.lang.RuntimeException(ex);
            } catch (java.lang.RuntimeException ex) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onFullBackup (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", ex);
                throw ex;
            } finally {
                // ... and then again after, as in the doBackup() case
                waitForSharedPrefs();
                // Send the EOD marker indicating that there is no more data
                // forthcoming from this agent.
                try {
                    java.io.FileOutputStream out = new java.io.FileOutputStream(data.getFileDescriptor());
                    byte[] buf = new byte[4];
                    out.write(buf);
                } catch (java.io.IOException e) {
                    android.util.Log.e(android.app.backup.BackupAgent.BackupServiceBinder.TAG, "Unable to finalize backup stream!");
                }
                android.os.Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (android.os.RemoteException e) {
                    // we'll time out anyway, so we're safe
                }
                if (android.os.Binder.getCallingPid() != android.os.Process.myPid()) {
                    libcore.io.IoUtils.closeQuietly(data);
                }
            }
        }

        public void doMeasureFullBackup(int token, android.app.backup.IBackupManager callbackBinder) {
            // Ensure that we're running with the app's normal permission level
            final long ident = android.os.Binder.clearCallingIdentity();
            android.app.backup.FullBackupDataOutput measureOutput = new android.app.backup.FullBackupDataOutput();
            waitForSharedPrefs();
            try {
                android.app.backup.BackupAgent.this.onFullBackup(measureOutput);
            } catch (java.io.IOException ex) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onFullBackup[M] (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", ex);
                throw new java.lang.RuntimeException(ex);
            } catch (java.lang.RuntimeException ex) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onFullBackup[M] (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", ex);
                throw ex;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, measureOutput.getSize());
                } catch (android.os.RemoteException e) {
                    // timeout, so we're safe
                }
            }
        }

        @java.lang.Override
        public void doRestoreFile(android.os.ParcelFileDescriptor data, long size, int type, java.lang.String domain, java.lang.String path, long mode, long mtime, int token, android.app.backup.IBackupManager callbackBinder) throws android.os.RemoteException {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.app.backup.BackupAgent.this.onRestoreFile(data, size, type, domain, path, mode, mtime);
            } catch (java.io.IOException e) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onRestoreFile (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", e);
                throw new java.lang.RuntimeException(e);
            } finally {
                // Ensure that any side-effect SharedPreferences writes have landed
                waitForSharedPrefs();
                android.os.Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (android.os.RemoteException e) {
                    // we'll time out anyway, so we're safe
                }
                if (android.os.Binder.getCallingPid() != android.os.Process.myPid()) {
                    libcore.io.IoUtils.closeQuietly(data);
                }
            }
        }

        @java.lang.Override
        public void doRestoreFinished(int token, android.app.backup.IBackupManager callbackBinder) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.app.backup.BackupAgent.this.onRestoreFinished();
            } catch (java.lang.Exception e) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onRestoreFinished (" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", e);
                throw e;
            } finally {
                // Ensure that any side-effect SharedPreferences writes have landed
                waitForSharedPrefs();
                android.os.Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opComplete(token, 0);
                } catch (android.os.RemoteException e) {
                    // we'll time out anyway, so we're safe
                }
            }
        }

        @java.lang.Override
        public void fail(java.lang.String message) {
            getHandler().post(new android.app.backup.BackupAgent.FailRunnable(message));
        }

        @java.lang.Override
        public void doQuotaExceeded(long backupDataBytes, long quotaBytes) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.app.backup.BackupAgent.this.onQuotaExceeded(backupDataBytes, quotaBytes);
            } catch (java.lang.Exception e) {
                android.util.Log.d(android.app.backup.BackupAgent.BackupServiceBinder.TAG, ("onQuotaExceeded(" + android.app.backup.BackupAgent.this.getClass().getName()) + ") threw", e);
                throw e;
            } finally {
                waitForSharedPrefs();
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    static class FailRunnable implements java.lang.Runnable {
        private java.lang.String mMessage;

        FailRunnable(java.lang.String message) {
            mMessage = message;
        }

        @java.lang.Override
        public void run() {
            throw new java.lang.IllegalStateException(mMessage);
        }
    }
}

