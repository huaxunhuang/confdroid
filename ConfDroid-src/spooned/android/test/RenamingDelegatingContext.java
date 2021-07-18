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
package android.test;


/**
 * This is a class which delegates to the given context, but performs database
 * and file operations with a renamed database/file name (prefixes default
 * names with a given prefix).
 *
 * @deprecated New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class RenamingDelegatingContext extends android.content.ContextWrapper {
    private android.content.Context mFileContext;

    private java.lang.String mFilePrefix = null;

    private java.io.File mCacheDir;

    private final java.lang.Object mSync = new java.lang.Object();

    private java.util.Set<java.lang.String> mDatabaseNames = com.google.android.collect.Sets.newHashSet();

    private java.util.Set<java.lang.String> mFileNames = com.google.android.collect.Sets.newHashSet();

    public static <T extends android.content.ContentProvider> T providerWithRenamedContext(java.lang.Class<T> contentProvider, android.content.Context c, java.lang.String filePrefix) throws java.lang.IllegalAccessException, java.lang.InstantiationException {
        return android.test.RenamingDelegatingContext.providerWithRenamedContext(contentProvider, c, filePrefix, false);
    }

    public static <T extends android.content.ContentProvider> T providerWithRenamedContext(java.lang.Class<T> contentProvider, android.content.Context c, java.lang.String filePrefix, boolean allowAccessToExistingFilesAndDbs) throws java.lang.IllegalAccessException, java.lang.InstantiationException {
        java.lang.Class<T> mProviderClass = contentProvider;
        T mProvider = mProviderClass.newInstance();
        android.test.RenamingDelegatingContext mContext = new android.test.RenamingDelegatingContext(c, filePrefix);
        if (allowAccessToExistingFilesAndDbs) {
            mContext.makeExistingFilesAndDbsAccessible();
        }
        mProvider.attachInfoForTesting(mContext, null);
        return mProvider;
    }

    /**
     * Makes accessible all files and databases whose names match the filePrefix that was passed to
     * the constructor. Normally only files and databases that were created through this context are
     * accessible.
     */
    public void makeExistingFilesAndDbsAccessible() {
        java.lang.String[] databaseList = mFileContext.databaseList();
        for (java.lang.String diskName : databaseList) {
            if (shouldDiskNameBeVisible(diskName)) {
                mDatabaseNames.add(publicNameFromDiskName(diskName));
            }
        }
        java.lang.String[] fileList = mFileContext.fileList();
        for (java.lang.String diskName : fileList) {
            if (shouldDiskNameBeVisible(diskName)) {
                mFileNames.add(publicNameFromDiskName(diskName));
            }
        }
    }

    /**
     * Returns if the given diskName starts with the given prefix or not.
     *
     * @param diskName
     * 		name of the database/file.
     */
    boolean shouldDiskNameBeVisible(java.lang.String diskName) {
        return diskName.startsWith(mFilePrefix);
    }

    /**
     * Returns the public name (everything following the prefix) of the given diskName.
     *
     * @param diskName
     * 		name of the database/file.
     */
    java.lang.String publicNameFromDiskName(java.lang.String diskName) {
        if (!shouldDiskNameBeVisible(diskName)) {
            throw new java.lang.IllegalArgumentException("disk file should not be visible: " + diskName);
        }
        return diskName.substring(mFilePrefix.length(), diskName.length());
    }

    /**
     *
     *
     * @param context
     * 		: the context that will be delegated.
     * @param filePrefix
     * 		: a prefix with which database and file names will be
     * 		prefixed.
     */
    public RenamingDelegatingContext(android.content.Context context, java.lang.String filePrefix) {
        super(context);
        mFileContext = context;
        mFilePrefix = filePrefix;
    }

    /**
     *
     *
     * @param context
     * 		: the context that will be delegated.
     * @param fileContext
     * 		: the context that file and db methods will be delegated to
     * @param filePrefix
     * 		: a prefix with which database and file names will be
     * 		prefixed.
     */
    public RenamingDelegatingContext(android.content.Context context, android.content.Context fileContext, java.lang.String filePrefix) {
        super(context);
        mFileContext = fileContext;
        mFilePrefix = filePrefix;
    }

    public java.lang.String getDatabasePrefix() {
        return mFilePrefix;
    }

    private java.lang.String renamedFileName(java.lang.String name) {
        return mFilePrefix + name;
    }

    @java.lang.Override
    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String name, int mode, android.database.sqlite.SQLiteDatabase.CursorFactory factory) {
        final java.lang.String internalName = renamedFileName(name);
        if (!mDatabaseNames.contains(name)) {
            mDatabaseNames.add(name);
            mFileContext.deleteDatabase(internalName);
        }
        return mFileContext.openOrCreateDatabase(internalName, mode, factory);
    }

    @java.lang.Override
    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String name, int mode, android.database.sqlite.SQLiteDatabase.CursorFactory factory, android.database.DatabaseErrorHandler errorHandler) {
        final java.lang.String internalName = renamedFileName(name);
        if (!mDatabaseNames.contains(name)) {
            mDatabaseNames.add(name);
            mFileContext.deleteDatabase(internalName);
        }
        return mFileContext.openOrCreateDatabase(internalName, mode, factory, errorHandler);
    }

    @java.lang.Override
    public boolean deleteDatabase(java.lang.String name) {
        if (mDatabaseNames.contains(name)) {
            mDatabaseNames.remove(name);
            return mFileContext.deleteDatabase(renamedFileName(name));
        } else {
            return false;
        }
    }

    @java.lang.Override
    public java.io.File getDatabasePath(java.lang.String name) {
        return mFileContext.getDatabasePath(renamedFileName(name));
    }

    @java.lang.Override
    public java.lang.String[] databaseList() {
        return mDatabaseNames.toArray(new java.lang.String[]{  });
    }

    @java.lang.Override
    public java.io.FileInputStream openFileInput(java.lang.String name) throws java.io.FileNotFoundException {
        final java.lang.String internalName = renamedFileName(name);
        if (mFileNames.contains(name)) {
            return mFileContext.openFileInput(internalName);
        } else {
            throw new java.io.FileNotFoundException(internalName);
        }
    }

    @java.lang.Override
    public java.io.FileOutputStream openFileOutput(java.lang.String name, int mode) throws java.io.FileNotFoundException {
        mFileNames.add(name);
        return mFileContext.openFileOutput(renamedFileName(name), mode);
    }

    @java.lang.Override
    public java.io.File getFileStreamPath(java.lang.String name) {
        return mFileContext.getFileStreamPath(renamedFileName(name));
    }

    @java.lang.Override
    public boolean deleteFile(java.lang.String name) {
        if (mFileNames.contains(name)) {
            mFileNames.remove(name);
            return mFileContext.deleteFile(renamedFileName(name));
        } else {
            return false;
        }
    }

    @java.lang.Override
    public java.lang.String[] fileList() {
        return mFileNames.toArray(new java.lang.String[]{  });
    }

    /**
     * In order to support calls to getCacheDir(), we create a temp cache dir (inside the real
     * one) and return it instead.  This code is basically getCacheDir(), except it uses the real
     * cache dir as the parent directory and creates a test cache dir inside that.
     */
    @java.lang.Override
    public java.io.File getCacheDir() {
        synchronized(mSync) {
            if (mCacheDir == null) {
                mCacheDir = new java.io.File(mFileContext.getCacheDir(), renamedFileName("cache"));
            }
            if (!mCacheDir.exists()) {
                if (!mCacheDir.mkdirs()) {
                    android.util.Log.w("RenamingDelegatingContext", "Unable to create cache directory");
                    return null;
                }
                android.os.FileUtils.setPermissions(mCacheDir.getPath(), (android.os.FileUtils.S_IRWXU | android.os.FileUtils.S_IRWXG) | android.os.FileUtils.S_IXOTH, -1, -1);
            }
        }
        return mCacheDir;
    }
}

