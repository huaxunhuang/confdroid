/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Global constant definitions et cetera related to the full-backup-to-fd
 * binary format.  Nothing in this namespace is part of any API; it's all
 * hidden details of the current implementation gathered into one location.
 *
 * @unknown 
 */
public class FullBackup {
    static final java.lang.String TAG = "FullBackup";

    /**
     * Enable this log tag to get verbose information while parsing the client xml.
     */
    static final java.lang.String TAG_XML_PARSER = "BackupXmlParserLogging";

    public static final java.lang.String APK_TREE_TOKEN = "a";

    public static final java.lang.String OBB_TREE_TOKEN = "obb";

    public static final java.lang.String ROOT_TREE_TOKEN = "r";

    public static final java.lang.String FILES_TREE_TOKEN = "f";

    public static final java.lang.String NO_BACKUP_TREE_TOKEN = "nb";

    public static final java.lang.String DATABASE_TREE_TOKEN = "db";

    public static final java.lang.String SHAREDPREFS_TREE_TOKEN = "sp";

    public static final java.lang.String CACHE_TREE_TOKEN = "c";

    public static final java.lang.String DEVICE_ROOT_TREE_TOKEN = "d_r";

    public static final java.lang.String DEVICE_FILES_TREE_TOKEN = "d_f";

    public static final java.lang.String DEVICE_NO_BACKUP_TREE_TOKEN = "d_nb";

    public static final java.lang.String DEVICE_DATABASE_TREE_TOKEN = "d_db";

    public static final java.lang.String DEVICE_SHAREDPREFS_TREE_TOKEN = "d_sp";

    public static final java.lang.String DEVICE_CACHE_TREE_TOKEN = "d_c";

    public static final java.lang.String MANAGED_EXTERNAL_TREE_TOKEN = "ef";

    public static final java.lang.String SHARED_STORAGE_TOKEN = "shared";

    public static final java.lang.String APPS_PREFIX = "apps/";

    public static final java.lang.String SHARED_PREFIX = android.app.backup.FullBackup.SHARED_STORAGE_TOKEN + "/";

    public static final java.lang.String FULL_BACKUP_INTENT_ACTION = "fullback";

    public static final java.lang.String FULL_RESTORE_INTENT_ACTION = "fullrest";

    public static final java.lang.String CONF_TOKEN_INTENT_EXTRA = "conftoken";

    /**
     *
     *
     * @unknown 
     */
    public static native int backupToTar(java.lang.String packageName, java.lang.String domain, java.lang.String linkdomain, java.lang.String rootpath, java.lang.String path, android.app.backup.FullBackupDataOutput output);

    private static final java.util.Map<java.lang.String, android.app.backup.FullBackup.BackupScheme> kPackageBackupSchemeMap = new android.util.ArrayMap<java.lang.String, android.app.backup.FullBackup.BackupScheme>();

    static synchronized android.app.backup.FullBackup.BackupScheme getBackupScheme(android.content.Context context) {
        android.app.backup.FullBackup.BackupScheme backupSchemeForPackage = android.app.backup.FullBackup.kPackageBackupSchemeMap.get(context.getPackageName());
        if (backupSchemeForPackage == null) {
            backupSchemeForPackage = new android.app.backup.FullBackup.BackupScheme(context);
            android.app.backup.FullBackup.kPackageBackupSchemeMap.put(context.getPackageName(), backupSchemeForPackage);
        }
        return backupSchemeForPackage;
    }

    public static android.app.backup.FullBackup.BackupScheme getBackupSchemeForTest(android.content.Context context) {
        android.app.backup.FullBackup.BackupScheme testing = new android.app.backup.FullBackup.BackupScheme(context);
        testing.mExcludes = new android.util.ArraySet();
        testing.mIncludes = new android.util.ArrayMap();
        return testing;
    }

    /**
     * Copy data from a socket to the given File location on permanent storage.  The
     * modification time and access mode of the resulting file will be set if desired,
     * although group/all rwx modes will be stripped: the restored file will not be
     * accessible from outside the target application even if the original file was.
     * If the {@code type} parameter indicates that the result should be a directory,
     * the socket parameter may be {@code null}; even if it is valid, no data will be
     * read from it in this case.
     * <p>
     * If the {@code mode} argument is negative, then the resulting output file will not
     * have its access mode or last modification time reset as part of this operation.
     *
     * @param data
     * 		Socket supplying the data to be copied to the output file.  If the
     * 		output is a directory, this may be {@code null}.
     * @param size
     * 		Number of bytes of data to copy from the socket to the file.  At least
     * 		this much data must be available through the {@code data} parameter.
     * @param type
     * 		Must be either {@link BackupAgent#TYPE_FILE} for ordinary file data
     * 		or {@link BackupAgent#TYPE_DIRECTORY} for a directory.
     * @param mode
     * 		Unix-style file mode (as used by the chmod(2) syscall) to be set on
     * 		the output file or directory.  group/all rwx modes are stripped even if set
     * 		in this parameter.  If this parameter is negative then neither
     * 		the mode nor the mtime values will be applied to the restored file.
     * @param mtime
     * 		A timestamp in the standard Unix epoch that will be imposed as the
     * 		last modification time of the output file.  if the {@code mode} parameter is
     * 		negative then this parameter will be ignored.
     * @param outFile
     * 		Location within the filesystem to place the data.  This must point
     * 		to a location that is writeable by the caller, preferably using an absolute path.
     * @throws IOException
     * 		
     */
    public static void restoreFile(android.os.ParcelFileDescriptor data, long size, int type, long mode, long mtime, java.io.File outFile) throws java.io.IOException {
        if (type == android.app.backup.BackupAgent.TYPE_DIRECTORY) {
            // Canonically a directory has no associated content, so we don't need to read
            // anything from the pipe in this case.  Just create the directory here and
            // drop down to the final metadata adjustment.
            if (outFile != null)
                outFile.mkdirs();

        } else {
            java.io.FileOutputStream out = null;
            // Pull the data from the pipe, copying it to the output file, until we're done
            try {
                if (outFile != null) {
                    java.io.File parent = outFile.getParentFile();
                    if (!parent.exists()) {
                        // in practice this will only be for the default semantic directories,
                        // and using the default mode for those is appropriate.
                        // This can also happen for the case where a parent directory has been
                        // excluded, but a file within that directory has been included.
                        parent.mkdirs();
                    }
                    out = new java.io.FileOutputStream(outFile);
                }
            } catch (java.io.IOException e) {
                android.util.Log.e(android.app.backup.FullBackup.TAG, "Unable to create/open file " + outFile.getPath(), e);
            }
            byte[] buffer = new byte[32 * 1024];
            final long origSize = size;
            java.io.FileInputStream in = new java.io.FileInputStream(data.getFileDescriptor());
            while (size > 0) {
                int toRead = (size > buffer.length) ? buffer.length : ((int) (size));
                int got = in.read(buffer, 0, toRead);
                if (got <= 0) {
                    android.util.Log.w(android.app.backup.FullBackup.TAG, (("Incomplete read: expected " + size) + " but got ") + (origSize - size));
                    break;
                }
                if (out != null) {
                    try {
                        out.write(buffer, 0, got);
                    } catch (java.io.IOException e) {
                        // Problem writing to the file.  Quit copying data and delete
                        // the file, but of course keep consuming the input stream.
                        android.util.Log.e(android.app.backup.FullBackup.TAG, "Unable to write to file " + outFile.getPath(), e);
                        out.close();
                        out = null;
                        outFile.delete();
                    }
                }
                size -= got;
            } 
            if (out != null)
                out.close();

        }
        // Now twiddle the state to match the backup, assuming all went well
        if ((mode >= 0) && (outFile != null)) {
            try {
                // explicitly prevent emplacement of files accessible by outside apps
                mode &= 0700;
                android.system.Os.chmod(outFile.getPath(), ((int) (mode)));
            } catch (android.system.ErrnoException e) {
                e.rethrowAsIOException();
            }
            outFile.setLastModified(mtime);
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    public static class BackupScheme {
        private final java.io.File FILES_DIR;

        private final java.io.File DATABASE_DIR;

        private final java.io.File ROOT_DIR;

        private final java.io.File SHAREDPREF_DIR;

        private final java.io.File CACHE_DIR;

        private final java.io.File NOBACKUP_DIR;

        private final java.io.File DEVICE_FILES_DIR;

        private final java.io.File DEVICE_DATABASE_DIR;

        private final java.io.File DEVICE_ROOT_DIR;

        private final java.io.File DEVICE_SHAREDPREF_DIR;

        private final java.io.File DEVICE_CACHE_DIR;

        private final java.io.File DEVICE_NOBACKUP_DIR;

        private final java.io.File EXTERNAL_DIR;

        final int mFullBackupContent;

        final android.content.pm.PackageManager mPackageManager;

        final android.os.storage.StorageManager mStorageManager;

        final java.lang.String mPackageName;

        // lazy initialized, only when needed
        private android.os.storage.StorageVolume[] mVolumes = null;

        /**
         * Parse out the semantic domains into the correct physical location.
         */
        java.lang.String tokenToDirectoryPath(java.lang.String domainToken) {
            try {
                if (domainToken.equals(android.app.backup.FullBackup.FILES_TREE_TOKEN)) {
                    return FILES_DIR.getCanonicalPath();
                } else
                    if (domainToken.equals(android.app.backup.FullBackup.DATABASE_TREE_TOKEN)) {
                        return DATABASE_DIR.getCanonicalPath();
                    } else
                        if (domainToken.equals(android.app.backup.FullBackup.ROOT_TREE_TOKEN)) {
                            return ROOT_DIR.getCanonicalPath();
                        } else
                            if (domainToken.equals(android.app.backup.FullBackup.SHAREDPREFS_TREE_TOKEN)) {
                                return SHAREDPREF_DIR.getCanonicalPath();
                            } else
                                if (domainToken.equals(android.app.backup.FullBackup.CACHE_TREE_TOKEN)) {
                                    return CACHE_DIR.getCanonicalPath();
                                } else
                                    if (domainToken.equals(android.app.backup.FullBackup.NO_BACKUP_TREE_TOKEN)) {
                                        return NOBACKUP_DIR.getCanonicalPath();
                                    } else
                                        if (domainToken.equals(android.app.backup.FullBackup.DEVICE_FILES_TREE_TOKEN)) {
                                            return DEVICE_FILES_DIR.getCanonicalPath();
                                        } else
                                            if (domainToken.equals(android.app.backup.FullBackup.DEVICE_DATABASE_TREE_TOKEN)) {
                                                return DEVICE_DATABASE_DIR.getCanonicalPath();
                                            } else
                                                if (domainToken.equals(android.app.backup.FullBackup.DEVICE_ROOT_TREE_TOKEN)) {
                                                    return DEVICE_ROOT_DIR.getCanonicalPath();
                                                } else
                                                    if (domainToken.equals(android.app.backup.FullBackup.DEVICE_SHAREDPREFS_TREE_TOKEN)) {
                                                        return DEVICE_SHAREDPREF_DIR.getCanonicalPath();
                                                    } else
                                                        if (domainToken.equals(android.app.backup.FullBackup.DEVICE_CACHE_TREE_TOKEN)) {
                                                            return DEVICE_CACHE_DIR.getCanonicalPath();
                                                        } else
                                                            if (domainToken.equals(android.app.backup.FullBackup.DEVICE_NO_BACKUP_TREE_TOKEN)) {
                                                                return DEVICE_NOBACKUP_DIR.getCanonicalPath();
                                                            } else
                                                                if (domainToken.equals(android.app.backup.FullBackup.MANAGED_EXTERNAL_TREE_TOKEN)) {
                                                                    if (EXTERNAL_DIR != null) {
                                                                        return EXTERNAL_DIR.getCanonicalPath();
                                                                    } else {
                                                                        return null;
                                                                    }
                                                                } else
                                                                    if (domainToken.startsWith(android.app.backup.FullBackup.SHARED_PREFIX)) {
                                                                        return sharedDomainToPath(domainToken);
                                                                    }













                // Not a supported location
                android.util.Log.i(android.app.backup.FullBackup.TAG, "Unrecognized domain " + domainToken);
                return null;
            } catch (java.lang.Exception e) {
                android.util.Log.i(android.app.backup.FullBackup.TAG, "Error reading directory for domain: " + domainToken);
                return null;
            }
        }

        private java.lang.String sharedDomainToPath(java.lang.String domain) throws java.io.IOException {
            // already known to start with SHARED_PREFIX, so we just look after that
            final java.lang.String volume = domain.substring(android.app.backup.FullBackup.SHARED_PREFIX.length());
            final android.os.storage.StorageVolume[] volumes = getVolumeList();
            final int volNum = java.lang.Integer.parseInt(volume);
            if (volNum < mVolumes.length) {
                return volumes[volNum].getPathFile().getCanonicalPath();
            }
            return null;
        }

        private android.os.storage.StorageVolume[] getVolumeList() {
            if (mStorageManager != null) {
                if (mVolumes == null) {
                    mVolumes = mStorageManager.getVolumeList();
                }
            } else {
                android.util.Log.e(android.app.backup.FullBackup.TAG, "Unable to access Storage Manager");
            }
            return mVolumes;
        }

        /**
         * A map of domain -> list of canonical file names in that domain that are to be included.
         * We keep track of the domain so that we can go through the file system in order later on.
         */
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> mIncludes;

        /**
         * e
         * List that will be populated with the canonical names of each file or directory that is
         * to be excluded.
         */
        android.util.ArraySet<java.lang.String> mExcludes;

        BackupScheme(android.content.Context context) {
            mFullBackupContent = context.getApplicationInfo().fullBackupContent;
            mStorageManager = ((android.os.storage.StorageManager) (context.getSystemService(android.content.Context.STORAGE_SERVICE)));
            mPackageManager = context.getPackageManager();
            mPackageName = context.getPackageName();
            // System apps have control over where their default storage context
            // is pointed, so we're always explicit when building paths.
            final android.content.Context ceContext = context.createCredentialProtectedStorageContext();
            FILES_DIR = ceContext.getFilesDir();
            DATABASE_DIR = ceContext.getDatabasePath("foo").getParentFile();
            ROOT_DIR = ceContext.getDataDir();
            SHAREDPREF_DIR = ceContext.getSharedPreferencesPath("foo").getParentFile();
            CACHE_DIR = ceContext.getCacheDir();
            NOBACKUP_DIR = ceContext.getNoBackupFilesDir();
            final android.content.Context deContext = context.createDeviceProtectedStorageContext();
            DEVICE_FILES_DIR = deContext.getFilesDir();
            DEVICE_DATABASE_DIR = deContext.getDatabasePath("foo").getParentFile();
            DEVICE_ROOT_DIR = deContext.getDataDir();
            DEVICE_SHAREDPREF_DIR = deContext.getSharedPreferencesPath("foo").getParentFile();
            DEVICE_CACHE_DIR = deContext.getCacheDir();
            DEVICE_NOBACKUP_DIR = deContext.getNoBackupFilesDir();
            if (android.os.Process.myUid() != android.os.Process.SYSTEM_UID) {
                EXTERNAL_DIR = context.getExternalFilesDir(null);
            } else {
                EXTERNAL_DIR = null;
            }
        }

        boolean isFullBackupContentEnabled() {
            if (mFullBackupContent < 0) {
                // android:fullBackupContent="false", bail.
                if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                    android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "android:fullBackupContent - \"false\"");
                }
                return false;
            }
            return true;
        }

        /**
         *
         *
         * @return A mapping of domain -> canonical paths within that domain. Each of these paths
        specifies a file that the client has explicitly included in their backup set. If this
        map is empty we will back up the entire data directory (including managed external
        storage).
         */
        public synchronized java.util.Map<java.lang.String, java.util.Set<java.lang.String>> maybeParseAndGetCanonicalIncludePaths() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            if (mIncludes == null) {
                maybeParseBackupSchemeLocked();
            }
            return mIncludes;
        }

        /**
         *
         *
         * @return A set of canonical paths that are to be excluded from the backup/restore set.
         */
        public synchronized android.util.ArraySet<java.lang.String> maybeParseAndGetCanonicalExcludePaths() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            if (mExcludes == null) {
                maybeParseBackupSchemeLocked();
            }
            return mExcludes;
        }

        private void maybeParseBackupSchemeLocked() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            // This not being null is how we know that we've tried to parse the xml already.
            mIncludes = new android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>>();
            mExcludes = new android.util.ArraySet<java.lang.String>();
            if (mFullBackupContent == 0) {
                // android:fullBackupContent="true" which means that we'll do everything.
                if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                    android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "android:fullBackupContent - \"true\"");
                }
            } else {
                // android:fullBackupContent="@xml/some_resource".
                if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                    android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "android:fullBackupContent - found xml resource");
                }
                android.content.res.XmlResourceParser parser = null;
                try {
                    parser = mPackageManager.getResourcesForApplication(mPackageName).getXml(mFullBackupContent);
                    parseBackupSchemeFromXmlLocked(parser, mExcludes, mIncludes);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    // Throw it as an IOException
                    throw new java.io.IOException(e);
                } finally {
                    if (parser != null) {
                        parser.close();
                    }
                }
            }
        }

        @com.android.internal.annotations.VisibleForTesting
        public void parseBackupSchemeFromXmlLocked(org.xmlpull.v1.XmlPullParser parser, java.util.Set<java.lang.String> excludes, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> includes) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            int event = parser.getEventType();// START_DOCUMENT

            while (event != org.xmlpull.v1.XmlPullParser.START_TAG) {
                event = parser.next();
            } 
            if (!"full-backup-content".equals(parser.getName())) {
                throw new org.xmlpull.v1.XmlPullParserException((("Xml file didn't start with correct tag" + " (<full-backup-content>). Found \"") + parser.getName()) + "\"");
            }
            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "\n");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "====================================================");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "Found valid fullBackupContent; parsing xml resource.");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "====================================================");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "");
            }
            while ((event = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case org.xmlpull.v1.XmlPullParser.START_TAG :
                        validateInnerTagContents(parser);
                        final java.lang.String domainFromXml = parser.getAttributeValue(null, "domain");
                        final java.io.File domainDirectory = getDirectoryForCriteriaDomain(domainFromXml);
                        if (domainDirectory == null) {
                            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, (((("...parsing \"" + parser.getName()) + "\": ") + "domain=\"") + domainFromXml) + "\" invalid; skipping");
                            }
                            break;
                        }
                        final java.io.File canonicalFile = extractCanonicalFile(domainDirectory, parser.getAttributeValue(null, "path"));
                        if (canonicalFile == null) {
                            break;
                        }
                        java.util.Set<java.lang.String> activeSet = parseCurrentTagForDomain(parser, excludes, includes, domainFromXml);
                        activeSet.add(canonicalFile.getCanonicalPath());
                        if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                            android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, ((("...parsed " + canonicalFile.getCanonicalPath()) + " for domain \"") + domainFromXml) + "\"");
                        }
                        // Special case journal files (not dirs) for sqlite database. frowny-face.
                        // Note that for a restore, the file is never a directory (b/c it doesn't
                        // exist). We have no way of knowing a priori whether or not to expect a
                        // dir, so we add the -journal anyway to be safe.
                        if ("database".equals(domainFromXml) && (!canonicalFile.isDirectory())) {
                            final java.lang.String canonicalJournalPath = canonicalFile.getCanonicalPath() + "-journal";
                            activeSet.add(canonicalJournalPath);
                            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, ("...automatically generated " + canonicalJournalPath) + ". Ignore if nonexistent.");
                            }
                            final java.lang.String canonicalWalPath = canonicalFile.getCanonicalPath() + "-wal";
                            activeSet.add(canonicalWalPath);
                            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, ("...automatically generated " + canonicalWalPath) + ". Ignore if nonexistent.");
                            }
                        }
                        // Special case for sharedpref files (not dirs) also add ".xml" suffix file.
                        if (("sharedpref".equals(domainFromXml) && (!canonicalFile.isDirectory())) && (!canonicalFile.getCanonicalPath().endsWith(".xml"))) {
                            final java.lang.String canonicalXmlPath = canonicalFile.getCanonicalPath() + ".xml";
                            activeSet.add(canonicalXmlPath);
                            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, ("...automatically generated " + canonicalXmlPath) + ". Ignore if nonexistent.");
                            }
                        }
                }
            } 
            if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "\n");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "Xml resource parsing complete.");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "Final tally.");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "Includes:");
                if (includes.isEmpty()) {
                    android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "  ...nothing specified (This means the entirety of app" + " data minus excludes)");
                } else {
                    for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : includes.entrySet()) {
                        android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "  domain=" + entry.getKey());
                        for (java.lang.String includeData : entry.getValue()) {
                            android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "  " + includeData);
                        }
                    }
                }
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "Excludes:");
                if (excludes.isEmpty()) {
                    android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "  ...nothing to exclude.");
                } else {
                    for (java.lang.String excludeData : excludes) {
                        android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "  " + excludeData);
                    }
                }
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "  ");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "====================================================");
                android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, "\n");
            }
        }

        private java.util.Set<java.lang.String> parseCurrentTagForDomain(org.xmlpull.v1.XmlPullParser parser, java.util.Set<java.lang.String> excludes, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> includes, java.lang.String domain) throws org.xmlpull.v1.XmlPullParserException {
            if ("include".equals(parser.getName())) {
                final java.lang.String domainToken = getTokenForXmlDomain(domain);
                java.util.Set<java.lang.String> includeSet = includes.get(domainToken);
                if (includeSet == null) {
                    includeSet = new android.util.ArraySet<java.lang.String>();
                    includes.put(domainToken, includeSet);
                }
                return includeSet;
            } else
                if ("exclude".equals(parser.getName())) {
                    return excludes;
                } else {
                    // Unrecognised tag => hard failure.
                    if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                        android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, ("Invalid tag found in xml \"" + parser.getName()) + "\"; aborting operation.");
                    }
                    throw new org.xmlpull.v1.XmlPullParserException((("Unrecognised tag in backup" + " criteria xml (") + parser.getName()) + ")");
                }

        }

        /**
         * Map xml specified domain (human-readable, what clients put in their manifest's xml) to
         * BackupAgent internal data token.
         *
         * @return null if the xml domain was invalid.
         */
        private java.lang.String getTokenForXmlDomain(java.lang.String xmlDomain) {
            if ("root".equals(xmlDomain)) {
                return android.app.backup.FullBackup.ROOT_TREE_TOKEN;
            } else
                if ("file".equals(xmlDomain)) {
                    return android.app.backup.FullBackup.FILES_TREE_TOKEN;
                } else
                    if ("database".equals(xmlDomain)) {
                        return android.app.backup.FullBackup.DATABASE_TREE_TOKEN;
                    } else
                        if ("sharedpref".equals(xmlDomain)) {
                            return android.app.backup.FullBackup.SHAREDPREFS_TREE_TOKEN;
                        } else
                            if ("device_root".equals(xmlDomain)) {
                                return android.app.backup.FullBackup.DEVICE_ROOT_TREE_TOKEN;
                            } else
                                if ("device_file".equals(xmlDomain)) {
                                    return android.app.backup.FullBackup.DEVICE_FILES_TREE_TOKEN;
                                } else
                                    if ("device_database".equals(xmlDomain)) {
                                        return android.app.backup.FullBackup.DEVICE_DATABASE_TREE_TOKEN;
                                    } else
                                        if ("device_sharedpref".equals(xmlDomain)) {
                                            return android.app.backup.FullBackup.DEVICE_SHAREDPREFS_TREE_TOKEN;
                                        } else
                                            if ("external".equals(xmlDomain)) {
                                                return android.app.backup.FullBackup.MANAGED_EXTERNAL_TREE_TOKEN;
                                            } else {
                                                return null;
                                            }








        }

        /**
         *
         *
         * @param domain
         * 		Directory where the specified file should exist. Not null.
         * @param filePathFromXml
         * 		parsed from xml. Not sanitised before calling this function so may be
         * 		null.
         * @return The canonical path of the file specified or null if no such file exists.
         */
        private java.io.File extractCanonicalFile(java.io.File domain, java.lang.String filePathFromXml) {
            if (filePathFromXml == null) {
                // Allow things like <include domain="sharedpref"/>
                filePathFromXml = "";
            }
            if (filePathFromXml.contains("..")) {
                if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                    android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, ((("...resolved \"" + domain.getPath()) + " ") + filePathFromXml) + "\", but the \"..\" path is not permitted; skipping.");
                }
                return null;
            }
            if (filePathFromXml.contains("//")) {
                if (android.util.Log.isLoggable(android.app.backup.FullBackup.TAG_XML_PARSER, android.util.Log.VERBOSE)) {
                    android.util.Log.v(android.app.backup.FullBackup.TAG_XML_PARSER, ((("...resolved \"" + domain.getPath()) + " ") + filePathFromXml) + "\", which contains the invalid \"//\" sequence; skipping.");
                }
                return null;
            }
            return new java.io.File(domain, filePathFromXml);
        }

        /**
         *
         *
         * @param domain
         * 		parsed from xml. Not sanitised before calling this function so may be null.
         * @return The directory relevant to the domain specified.
         */
        private java.io.File getDirectoryForCriteriaDomain(java.lang.String domain) {
            if (android.text.TextUtils.isEmpty(domain)) {
                return null;
            }
            if ("file".equals(domain)) {
                return FILES_DIR;
            } else
                if ("database".equals(domain)) {
                    return DATABASE_DIR;
                } else
                    if ("root".equals(domain)) {
                        return ROOT_DIR;
                    } else
                        if ("sharedpref".equals(domain)) {
                            return SHAREDPREF_DIR;
                        } else
                            if ("device_file".equals(domain)) {
                                return DEVICE_FILES_DIR;
                            } else
                                if ("device_database".equals(domain)) {
                                    return DEVICE_DATABASE_DIR;
                                } else
                                    if ("device_root".equals(domain)) {
                                        return DEVICE_ROOT_DIR;
                                    } else
                                        if ("device_sharedpref".equals(domain)) {
                                            return DEVICE_SHAREDPREF_DIR;
                                        } else
                                            if ("external".equals(domain)) {
                                                return EXTERNAL_DIR;
                                            } else {
                                                return null;
                                            }








        }

        /**
         * Let's be strict about the type of xml the client can write. If we see anything untoward,
         * throw an XmlPullParserException.
         */
        private void validateInnerTagContents(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
            if (parser.getAttributeCount() > 2) {
                throw new org.xmlpull.v1.XmlPullParserException(("At most 2 tag attributes allowed for \"" + parser.getName()) + "\" tag (\"domain\" & \"path\".");
            }
            if ((!"include".equals(parser.getName())) && (!"exclude".equals(parser.getName()))) {
                throw new org.xmlpull.v1.XmlPullParserException((("A valid tag is one of \"<include/>\" or" + " \"<exclude/>. You provided \"") + parser.getName()) + "\"");
            }
        }
    }
}

