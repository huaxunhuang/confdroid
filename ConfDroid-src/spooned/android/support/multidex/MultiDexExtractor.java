/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.multidex;


/**
 * Exposes application secondary dex files as files in the application data
 * directory.
 */
final class MultiDexExtractor {
    private static final java.lang.String TAG = android.support.multidex.MultiDex.TAG;

    /**
     * We look for additional dex files named {@code classes2.dex},
     * {@code classes3.dex}, etc.
     */
    private static final java.lang.String DEX_PREFIX = "classes";

    private static final java.lang.String DEX_SUFFIX = ".dex";

    private static final java.lang.String EXTRACTED_NAME_EXT = ".classes";

    private static final java.lang.String EXTRACTED_SUFFIX = ".zip";

    private static final int MAX_EXTRACT_ATTEMPTS = 3;

    private static final java.lang.String PREFS_FILE = "multidex.version";

    private static final java.lang.String KEY_TIME_STAMP = "timestamp";

    private static final java.lang.String KEY_CRC = "crc";

    private static final java.lang.String KEY_DEX_NUMBER = "dex.number";

    /**
     * Size of reading buffers.
     */
    private static final int BUFFER_SIZE = 0x4000;

    /* Keep value away from 0 because it is a too probable time stamp value */
    private static final long NO_VALUE = -1L;

    private static final java.lang.String LOCK_FILENAME = "MultiDex.lock";

    /**
     * Extracts application secondary dexes into files in the application data
     * directory.
     *
     * @return a list of files that were created. The list may be empty if there
    are no secondary dex files. Never return null.
     * @throws IOException
     * 		if encounters a problem while reading or writing
     * 		secondary dex files
     */
    static java.util.List<java.io.File> load(android.content.Context context, android.content.pm.ApplicationInfo applicationInfo, java.io.File dexDir, boolean forceReload) throws java.io.IOException {
        android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, ((("MultiDexExtractor.load(" + applicationInfo.sourceDir) + ", ") + forceReload) + ")");
        final java.io.File sourceApk = new java.io.File(applicationInfo.sourceDir);
        long currentCrc = android.support.multidex.MultiDexExtractor.getZipCrc(sourceApk);
        // Validity check and extraction must be done only while the lock file has been taken.
        java.io.File lockFile = new java.io.File(dexDir, android.support.multidex.MultiDexExtractor.LOCK_FILENAME);
        java.io.RandomAccessFile lockRaf = new java.io.RandomAccessFile(lockFile, "rw");
        java.nio.channels.FileChannel lockChannel = null;
        java.nio.channels.FileLock cacheLock = null;
        java.util.List<java.io.File> files;
        java.io.IOException releaseLockException = null;
        try {
            lockChannel = lockRaf.getChannel();
            android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, "Blocking on lock " + lockFile.getPath());
            cacheLock = lockChannel.lock();
            android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, lockFile.getPath() + " locked");
            if ((!forceReload) && (!android.support.multidex.MultiDexExtractor.isModified(context, sourceApk, currentCrc))) {
                try {
                    files = android.support.multidex.MultiDexExtractor.loadExistingExtractions(context, sourceApk, dexDir);
                } catch (java.io.IOException ioe) {
                    android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, "Failed to reload existing extracted secondary dex files," + " falling back to fresh extraction", ioe);
                    files = android.support.multidex.MultiDexExtractor.performExtractions(sourceApk, dexDir);
                    android.support.multidex.MultiDexExtractor.putStoredApkInfo(context, android.support.multidex.MultiDexExtractor.getTimeStamp(sourceApk), currentCrc, files.size() + 1);
                }
            } else {
                android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, "Detected that extraction must be performed.");
                files = android.support.multidex.MultiDexExtractor.performExtractions(sourceApk, dexDir);
                android.support.multidex.MultiDexExtractor.putStoredApkInfo(context, android.support.multidex.MultiDexExtractor.getTimeStamp(sourceApk), currentCrc, files.size() + 1);
            }
        } finally {
            if (cacheLock != null) {
                try {
                    cacheLock.release();
                } catch (java.io.IOException e) {
                    android.util.Log.e(android.support.multidex.MultiDexExtractor.TAG, "Failed to release lock on " + lockFile.getPath());
                    // Exception while releasing the lock is bad, we want to report it, but not at
                    // the price of overriding any already pending exception.
                    releaseLockException = e;
                }
            }
            if (lockChannel != null) {
                android.support.multidex.MultiDexExtractor.closeQuietly(lockChannel);
            }
            android.support.multidex.MultiDexExtractor.closeQuietly(lockRaf);
        }
        if (releaseLockException != null) {
            throw releaseLockException;
        }
        android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, ("load found " + files.size()) + " secondary dex files");
        return files;
    }

    /**
     * Load previously extracted secondary dex files. Should be called only while owning the lock on
     * {@link #LOCK_FILENAME}.
     */
    private static java.util.List<java.io.File> loadExistingExtractions(android.content.Context context, java.io.File sourceApk, java.io.File dexDir) throws java.io.IOException {
        android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, "loading existing secondary dex files");
        final java.lang.String extractedFilePrefix = sourceApk.getName() + android.support.multidex.MultiDexExtractor.EXTRACTED_NAME_EXT;
        int totalDexNumber = android.support.multidex.MultiDexExtractor.getMultiDexPreferences(context).getInt(android.support.multidex.MultiDexExtractor.KEY_DEX_NUMBER, 1);
        final java.util.List<java.io.File> files = new java.util.ArrayList<java.io.File>(totalDexNumber);
        for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
            java.lang.String fileName = (extractedFilePrefix + secondaryNumber) + android.support.multidex.MultiDexExtractor.EXTRACTED_SUFFIX;
            java.io.File extractedFile = new java.io.File(dexDir, fileName);
            if (extractedFile.isFile()) {
                files.add(extractedFile);
                if (!android.support.multidex.MultiDexExtractor.verifyZipFile(extractedFile)) {
                    android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, "Invalid zip file: " + extractedFile);
                    throw new java.io.IOException("Invalid ZIP file.");
                }
            } else {
                throw new java.io.IOException(("Missing extracted secondary dex file '" + extractedFile.getPath()) + "'");
            }
        }
        return files;
    }

    /**
     * Compare current archive and crc with values stored in {@link SharedPreferences}. Should be
     * called only while owning the lock on {@link #LOCK_FILENAME}.
     */
    private static boolean isModified(android.content.Context context, java.io.File archive, long currentCrc) {
        android.content.SharedPreferences prefs = android.support.multidex.MultiDexExtractor.getMultiDexPreferences(context);
        return (prefs.getLong(android.support.multidex.MultiDexExtractor.KEY_TIME_STAMP, android.support.multidex.MultiDexExtractor.NO_VALUE) != android.support.multidex.MultiDexExtractor.getTimeStamp(archive)) || (prefs.getLong(android.support.multidex.MultiDexExtractor.KEY_CRC, android.support.multidex.MultiDexExtractor.NO_VALUE) != currentCrc);
    }

    private static long getTimeStamp(java.io.File archive) {
        long timeStamp = archive.lastModified();
        if (timeStamp == android.support.multidex.MultiDexExtractor.NO_VALUE) {
            // never return NO_VALUE
            timeStamp--;
        }
        return timeStamp;
    }

    private static long getZipCrc(java.io.File archive) throws java.io.IOException {
        long computedValue = android.support.multidex.ZipUtil.getZipCrc(archive);
        if (computedValue == android.support.multidex.MultiDexExtractor.NO_VALUE) {
            // never return NO_VALUE
            computedValue--;
        }
        return computedValue;
    }

    private static java.util.List<java.io.File> performExtractions(java.io.File sourceApk, java.io.File dexDir) throws java.io.IOException {
        final java.lang.String extractedFilePrefix = sourceApk.getName() + android.support.multidex.MultiDexExtractor.EXTRACTED_NAME_EXT;
        // Ensure that whatever deletions happen in prepareDexDir only happen if the zip that
        // contains a secondary dex file in there is not consistent with the latest apk.  Otherwise,
        // multi-process race conditions can cause a crash loop where one process deletes the zip
        // while another had created it.
        android.support.multidex.MultiDexExtractor.prepareDexDir(dexDir, extractedFilePrefix);
        java.util.List<java.io.File> files = new java.util.ArrayList<java.io.File>();
        final java.util.zip.ZipFile apk = new java.util.zip.ZipFile(sourceApk);
        try {
            int secondaryNumber = 2;
            java.util.zip.ZipEntry dexFile = apk.getEntry((android.support.multidex.MultiDexExtractor.DEX_PREFIX + secondaryNumber) + android.support.multidex.MultiDexExtractor.DEX_SUFFIX);
            while (dexFile != null) {
                java.lang.String fileName = (extractedFilePrefix + secondaryNumber) + android.support.multidex.MultiDexExtractor.EXTRACTED_SUFFIX;
                java.io.File extractedFile = new java.io.File(dexDir, fileName);
                files.add(extractedFile);
                android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, "Extraction is needed for file " + extractedFile);
                int numAttempts = 0;
                boolean isExtractionSuccessful = false;
                while ((numAttempts < android.support.multidex.MultiDexExtractor.MAX_EXTRACT_ATTEMPTS) && (!isExtractionSuccessful)) {
                    numAttempts++;
                    // Create a zip file (extractedFile) containing only the secondary dex file
                    // (dexFile) from the apk.
                    android.support.multidex.MultiDexExtractor.extract(apk, dexFile, extractedFile, extractedFilePrefix);
                    // Verify that the extracted file is indeed a zip file.
                    isExtractionSuccessful = android.support.multidex.MultiDexExtractor.verifyZipFile(extractedFile);
                    // Log the sha1 of the extracted zip file
                    android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, (((("Extraction " + (isExtractionSuccessful ? "success" : "failed")) + " - length ") + extractedFile.getAbsolutePath()) + ": ") + extractedFile.length());
                    if (!isExtractionSuccessful) {
                        // Delete the extracted file
                        extractedFile.delete();
                        if (extractedFile.exists()) {
                            android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, ("Failed to delete corrupted secondary dex '" + extractedFile.getPath()) + "'");
                        }
                    }
                } 
                if (!isExtractionSuccessful) {
                    throw new java.io.IOException(((("Could not create zip file " + extractedFile.getAbsolutePath()) + " for secondary dex (") + secondaryNumber) + ")");
                }
                secondaryNumber++;
                dexFile = apk.getEntry((android.support.multidex.MultiDexExtractor.DEX_PREFIX + secondaryNumber) + android.support.multidex.MultiDexExtractor.DEX_SUFFIX);
            } 
        } finally {
            try {
                apk.close();
            } catch (java.io.IOException e) {
                android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, "Failed to close resource", e);
            }
        }
        return files;
    }

    /**
     * Save {@link SharedPreferences}. Should be called only while owning the lock on
     * {@link #LOCK_FILENAME}.
     */
    private static void putStoredApkInfo(android.content.Context context, long timeStamp, long crc, int totalDexNumber) {
        android.content.SharedPreferences prefs = android.support.multidex.MultiDexExtractor.getMultiDexPreferences(context);
        android.content.SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(android.support.multidex.MultiDexExtractor.KEY_TIME_STAMP, timeStamp);
        edit.putLong(android.support.multidex.MultiDexExtractor.KEY_CRC, crc);
        edit.putInt(android.support.multidex.MultiDexExtractor.KEY_DEX_NUMBER, totalDexNumber);
        /* Use commit() and not apply() as advised by the doc because we need synchronous writing of
        the editor content and apply is doing an "asynchronous commit to disk".
         */
        edit.commit();
    }

    /**
     * Get the MuliDex {@link SharedPreferences} for the current application. Should be called only
     * while owning the lock on {@link #LOCK_FILENAME}.
     */
    private static android.content.SharedPreferences getMultiDexPreferences(android.content.Context context) {
        return /* Context.MODE_MULTI_PROCESS */
        context.getSharedPreferences(android.support.multidex.MultiDexExtractor.PREFS_FILE, android.os.Build.VERSION.SDK_INT < 11/* Build.VERSION_CODES.HONEYCOMB */
         ? android.content.Context.MODE_PRIVATE : android.content.Context.MODE_PRIVATE | 0x4);
    }

    /**
     * This removes old files.
     */
    private static void prepareDexDir(java.io.File dexDir, final java.lang.String extractedFilePrefix) {
        java.io.FileFilter filter = new java.io.FileFilter() {
            @java.lang.Override
            public boolean accept(java.io.File pathname) {
                java.lang.String name = pathname.getName();
                return !(name.startsWith(extractedFilePrefix) || name.equals(android.support.multidex.MultiDexExtractor.LOCK_FILENAME));
            }
        };
        java.io.File[] files = dexDir.listFiles(filter);
        if (files == null) {
            android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, ("Failed to list secondary dex dir content (" + dexDir.getPath()) + ").");
            return;
        }
        for (java.io.File oldFile : files) {
            android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, (("Trying to delete old file " + oldFile.getPath()) + " of size ") + oldFile.length());
            if (!oldFile.delete()) {
                android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, "Failed to delete old file " + oldFile.getPath());
            } else {
                android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, "Deleted old file " + oldFile.getPath());
            }
        }
    }

    private static void extract(java.util.zip.ZipFile apk, java.util.zip.ZipEntry dexFile, java.io.File extractTo, java.lang.String extractedFilePrefix) throws java.io.FileNotFoundException, java.io.IOException {
        java.io.InputStream in = apk.getInputStream(dexFile);
        java.util.zip.ZipOutputStream out = null;
        java.io.File tmp = java.io.File.createTempFile(extractedFilePrefix, android.support.multidex.MultiDexExtractor.EXTRACTED_SUFFIX, extractTo.getParentFile());
        android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, "Extracting " + tmp.getPath());
        try {
            out = new java.util.zip.ZipOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream(tmp)));
            try {
                java.util.zip.ZipEntry classesDex = new java.util.zip.ZipEntry("classes.dex");
                // keep zip entry time since it is the criteria used by Dalvik
                classesDex.setTime(dexFile.getTime());
                out.putNextEntry(classesDex);
                byte[] buffer = new byte[android.support.multidex.MultiDexExtractor.BUFFER_SIZE];
                int length = in.read(buffer);
                while (length != (-1)) {
                    out.write(buffer, 0, length);
                    length = in.read(buffer);
                } 
                out.closeEntry();
            } finally {
                out.close();
            }
            android.util.Log.i(android.support.multidex.MultiDexExtractor.TAG, "Renaming to " + extractTo.getPath());
            if (!tmp.renameTo(extractTo)) {
                throw new java.io.IOException(((("Failed to rename \"" + tmp.getAbsolutePath()) + "\" to \"") + extractTo.getAbsolutePath()) + "\"");
            }
        } finally {
            android.support.multidex.MultiDexExtractor.closeQuietly(in);
            tmp.delete();// return status ignored

        }
    }

    /**
     * Returns whether the file is a valid zip file.
     */
    static boolean verifyZipFile(java.io.File file) {
        try {
            java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file);
            try {
                zipFile.close();
                return true;
            } catch (java.io.IOException e) {
                android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, "Failed to close zip file: " + file.getAbsolutePath());
            }
        } catch (java.util.zip.ZipException ex) {
            android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, ("File " + file.getAbsolutePath()) + " is not a valid zip file.", ex);
        } catch (java.io.IOException ex) {
            android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, "Got an IOException trying to open zip file: " + file.getAbsolutePath(), ex);
        }
        return false;
    }

    /**
     * Closes the given {@code Closeable}. Suppresses any IO exceptions.
     */
    private static void closeQuietly(java.io.Closeable closeable) {
        try {
            closeable.close();
        } catch (java.io.IOException e) {
            android.util.Log.w(android.support.multidex.MultiDexExtractor.TAG, "Failed to close resource", e);
        }
    }
}

