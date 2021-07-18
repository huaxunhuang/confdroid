/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * Tools for managing files.  Not for public consumption.
 *
 * @unknown 
 */
public class FileUtils {
    private static final java.lang.String TAG = "FileUtils";

    public static final int S_IRWXU = 0700;

    public static final int S_IRUSR = 0400;

    public static final int S_IWUSR = 0200;

    public static final int S_IXUSR = 0100;

    public static final int S_IRWXG = 070;

    public static final int S_IRGRP = 040;

    public static final int S_IWGRP = 020;

    public static final int S_IXGRP = 010;

    public static final int S_IRWXO = 07;

    public static final int S_IROTH = 04;

    public static final int S_IWOTH = 02;

    public static final int S_IXOTH = 01;

    /**
     * Regular expression for safe filenames: no spaces or metacharacters.
     *
     * Use a preload holder so that FileUtils can be compile-time initialized.
     */
    private static class NoImagePreloadHolder {
        public static final java.util.regex.Pattern SAFE_FILENAME_PATTERN = java.util.regex.Pattern.compile("[\\w%+,./=_-]+");
    }

    private static final java.io.File[] EMPTY = new java.io.File[0];

    /**
     * Set owner and mode of of given {@link File}.
     *
     * @param mode
     * 		to apply through {@code chmod}
     * @param uid
     * 		to apply through {@code chown}, or -1 to leave unchanged
     * @param gid
     * 		to apply through {@code chown}, or -1 to leave unchanged
     * @return 0 on success, otherwise errno.
     */
    public static int setPermissions(java.io.File path, int mode, int uid, int gid) {
        return android.os.FileUtils.setPermissions(path.getAbsolutePath(), mode, uid, gid);
    }

    /**
     * Set owner and mode of of given path.
     *
     * @param mode
     * 		to apply through {@code chmod}
     * @param uid
     * 		to apply through {@code chown}, or -1 to leave unchanged
     * @param gid
     * 		to apply through {@code chown}, or -1 to leave unchanged
     * @return 0 on success, otherwise errno.
     */
    public static int setPermissions(java.lang.String path, int mode, int uid, int gid) {
        try {
            android.system.Os.chmod(path, mode);
        } catch (android.system.ErrnoException e) {
            android.util.Slog.w(android.os.FileUtils.TAG, (("Failed to chmod(" + path) + "): ") + e);
            return e.errno;
        }
        if ((uid >= 0) || (gid >= 0)) {
            try {
                android.system.Os.chown(path, uid, gid);
            } catch (android.system.ErrnoException e) {
                android.util.Slog.w(android.os.FileUtils.TAG, (("Failed to chown(" + path) + "): ") + e);
                return e.errno;
            }
        }
        return 0;
    }

    /**
     * Set owner and mode of of given {@link FileDescriptor}.
     *
     * @param mode
     * 		to apply through {@code chmod}
     * @param uid
     * 		to apply through {@code chown}, or -1 to leave unchanged
     * @param gid
     * 		to apply through {@code chown}, or -1 to leave unchanged
     * @return 0 on success, otherwise errno.
     */
    public static int setPermissions(java.io.FileDescriptor fd, int mode, int uid, int gid) {
        try {
            android.system.Os.fchmod(fd, mode);
        } catch (android.system.ErrnoException e) {
            android.util.Slog.w(android.os.FileUtils.TAG, "Failed to fchmod(): " + e);
            return e.errno;
        }
        if ((uid >= 0) || (gid >= 0)) {
            try {
                android.system.Os.fchown(fd, uid, gid);
            } catch (android.system.ErrnoException e) {
                android.util.Slog.w(android.os.FileUtils.TAG, "Failed to fchown(): " + e);
                return e.errno;
            }
        }
        return 0;
    }

    public static void copyPermissions(java.io.File from, java.io.File to) throws java.io.IOException {
        try {
            final android.system.StructStat stat = android.system.Os.stat(from.getAbsolutePath());
            android.system.Os.chmod(to.getAbsolutePath(), stat.st_mode);
            android.system.Os.chown(to.getAbsolutePath(), stat.st_uid, stat.st_gid);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Return owning UID of given path, otherwise -1.
     */
    public static int getUid(java.lang.String path) {
        try {
            return android.system.Os.stat(path).st_uid;
        } catch (android.system.ErrnoException e) {
            return -1;
        }
    }

    /**
     * Perform an fsync on the given FileOutputStream.  The stream at this
     * point must be flushed but not yet closed.
     */
    public static boolean sync(java.io.FileOutputStream stream) {
        try {
            if (stream != null) {
                stream.getFD().sync();
            }
            return true;
        } catch (java.io.IOException e) {
        }
        return false;
    }

    @java.lang.Deprecated
    public static boolean copyFile(java.io.File srcFile, java.io.File destFile) {
        try {
            android.os.FileUtils.copyFileOrThrow(srcFile, destFile);
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    // copy a file from srcFile to destFile, return true if succeed, return
    // false if fail
    public static void copyFileOrThrow(java.io.File srcFile, java.io.File destFile) throws java.io.IOException {
        try (java.io.InputStream in = new java.io.FileInputStream(srcFile)) {
            android.os.FileUtils.copyToFileOrThrow(in, destFile);
        }
    }

    @java.lang.Deprecated
    public static boolean copyToFile(java.io.InputStream inputStream, java.io.File destFile) {
        try {
            android.os.FileUtils.copyToFileOrThrow(inputStream, destFile);
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    public static void copyToFileOrThrow(java.io.InputStream inputStream, java.io.File destFile) throws java.io.IOException {
        if (destFile.exists()) {
            destFile.delete();
        }
        java.io.FileOutputStream out = new java.io.FileOutputStream(destFile);
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) >= 0) {
                out.write(buffer, 0, bytesRead);
            } 
        } finally {
            out.flush();
            try {
                out.getFD().sync();
            } catch (java.io.IOException e) {
            }
            out.close();
        }
    }

    /**
     * Check if a filename is "safe" (no metacharacters or spaces).
     *
     * @param file
     * 		The file to check
     */
    public static boolean isFilenameSafe(java.io.File file) {
        // Note, we check whether it matches what's known to be safe,
        // rather than what's known to be unsafe.  Non-ASCII, control
        // characters, etc. are all unsafe by default.
        return android.os.FileUtils.NoImagePreloadHolder.SAFE_FILENAME_PATTERN.matcher(file.getPath()).matches();
    }

    /**
     * Read a text file into a String, optionally limiting the length.
     *
     * @param file
     * 		to read (will not seek, so things like /proc files are OK)
     * @param max
     * 		length (positive for head, negative of tail, 0 for no limit)
     * @param ellipsis
     * 		to add of the file was truncated (can be null)
     * @return the contents of the file, possibly truncated
     * @throws IOException
     * 		if something goes wrong reading the file
     */
    public static java.lang.String readTextFile(java.io.File file, int max, java.lang.String ellipsis) throws java.io.IOException {
        java.io.InputStream input = new java.io.FileInputStream(file);
        // wrapping a BufferedInputStream around it because when reading /proc with unbuffered
        // input stream, bytes read not equal to buffer size is not necessarily the correct
        // indication for EOF; but it is true for BufferedInputStream due to its implementation.
        java.io.BufferedInputStream bis = new java.io.BufferedInputStream(input);
        try {
            long size = file.length();
            if ((max > 0) || ((size > 0) && (max == 0))) {
                // "head" mode: read the first N bytes
                if ((size > 0) && ((max == 0) || (size < max)))
                    max = ((int) (size));

                byte[] data = new byte[max + 1];
                int length = bis.read(data);
                if (length <= 0)
                    return "";

                if (length <= max)
                    return new java.lang.String(data, 0, length);

                if (ellipsis == null)
                    return new java.lang.String(data, 0, max);

                return new java.lang.String(data, 0, max) + ellipsis;
            } else
                if (max < 0) {
                    // "tail" mode: keep the last N
                    int len;
                    boolean rolled = false;
                    byte[] last = null;
                    byte[] data = null;
                    do {
                        if (last != null)
                            rolled = true;

                        byte[] tmp = last;
                        last = data;
                        data = tmp;
                        if (data == null)
                            data = new byte[-max];

                        len = bis.read(data);
                    } while (len == data.length );
                    if ((last == null) && (len <= 0))
                        return "";

                    if (last == null)
                        return new java.lang.String(data, 0, len);

                    if (len > 0) {
                        rolled = true;
                        java.lang.System.arraycopy(last, len, last, 0, last.length - len);
                        java.lang.System.arraycopy(data, 0, last, last.length - len, len);
                    }
                    if ((ellipsis == null) || (!rolled))
                        return new java.lang.String(last);

                    return ellipsis + new java.lang.String(last);
                } else {
                    // "cat" mode: size unknown, read it all in streaming fashion
                    java.io.ByteArrayOutputStream contents = new java.io.ByteArrayOutputStream();
                    int len;
                    byte[] data = new byte[1024];
                    do {
                        len = bis.read(data);
                        if (len > 0)
                            contents.write(data, 0, len);

                    } while (len == data.length );
                    return contents.toString();
                }

        } finally {
            bis.close();
            input.close();
        }
    }

    public static void stringToFile(java.io.File file, java.lang.String string) throws java.io.IOException {
        android.os.FileUtils.stringToFile(file.getAbsolutePath(), string);
    }

    /**
     * Writes string to file. Basically same as "echo -n $string > $filename"
     *
     * @param filename
     * 		
     * @param string
     * 		
     * @throws IOException
     * 		
     */
    public static void stringToFile(java.lang.String filename, java.lang.String string) throws java.io.IOException {
        java.io.FileWriter out = new java.io.FileWriter(filename);
        try {
            out.write(string);
        } finally {
            out.close();
        }
    }

    /**
     * Computes the checksum of a file using the CRC32 checksum routine.
     * The value of the checksum is returned.
     *
     * @param file
     * 		the file to checksum, must not be null
     * @return the checksum value or an exception is thrown.
     */
    public static long checksumCrc32(java.io.File file) throws java.io.FileNotFoundException, java.io.IOException {
        java.util.zip.CRC32 checkSummer = new java.util.zip.CRC32();
        java.util.zip.CheckedInputStream cis = null;
        try {
            cis = new java.util.zip.CheckedInputStream(new java.io.FileInputStream(file), checkSummer);
            byte[] buf = new byte[128];
            while (cis.read(buf) >= 0) {
                // Just read for checksum to get calculated.
            } 
            return checkSummer.getValue();
        } finally {
            if (cis != null) {
                try {
                    cis.close();
                } catch (java.io.IOException e) {
                }
            }
        }
    }

    /**
     * Delete older files in a directory until only those matching the given
     * constraints remain.
     *
     * @param minCount
     * 		Always keep at least this many files.
     * @param minAge
     * 		Always keep files younger than this age.
     * @return if any files were deleted.
     */
    public static boolean deleteOlderFiles(java.io.File dir, int minCount, long minAge) {
        if ((minCount < 0) || (minAge < 0)) {
            throw new java.lang.IllegalArgumentException("Constraints must be positive or 0");
        }
        final java.io.File[] files = dir.listFiles();
        if (files == null)
            return false;

        // Sort with newest files first
        java.util.Arrays.sort(files, new java.util.Comparator<java.io.File>() {
            @java.lang.Override
            public int compare(java.io.File lhs, java.io.File rhs) {
                return ((int) (rhs.lastModified() - lhs.lastModified()));
            }
        });
        // Keep at least minCount files
        boolean deleted = false;
        for (int i = minCount; i < files.length; i++) {
            final java.io.File file = files[i];
            // Keep files newer than minAge
            final long age = java.lang.System.currentTimeMillis() - file.lastModified();
            if (age > minAge) {
                if (file.delete()) {
                    android.util.Log.d(android.os.FileUtils.TAG, "Deleted old file " + file);
                    deleted = true;
                }
            }
        }
        return deleted;
    }

    /**
     * Test if a file lives under the given directory, either as a direct child
     * or a distant grandchild.
     * <p>
     * Both files <em>must</em> have been resolved using
     * {@link File#getCanonicalFile()} to avoid symlink or path traversal
     * attacks.
     */
    public static boolean contains(java.io.File[] dirs, java.io.File file) {
        for (java.io.File dir : dirs) {
            if (android.os.FileUtils.contains(dir, file)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test if a file lives under the given directory, either as a direct child
     * or a distant grandchild.
     * <p>
     * Both files <em>must</em> have been resolved using
     * {@link File#getCanonicalFile()} to avoid symlink or path traversal
     * attacks.
     */
    public static boolean contains(java.io.File dir, java.io.File file) {
        if ((dir == null) || (file == null))
            return false;

        java.lang.String dirPath = dir.getAbsolutePath();
        java.lang.String filePath = file.getAbsolutePath();
        if (dirPath.equals(filePath)) {
            return true;
        }
        if (!dirPath.endsWith("/")) {
            dirPath += "/";
        }
        return filePath.startsWith(dirPath);
    }

    public static boolean deleteContentsAndDir(java.io.File dir) {
        if (android.os.FileUtils.deleteContents(dir)) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static boolean deleteContents(java.io.File dir) {
        java.io.File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (java.io.File file : files) {
                if (file.isDirectory()) {
                    success &= android.os.FileUtils.deleteContents(file);
                }
                if (!file.delete()) {
                    android.util.Log.w(android.os.FileUtils.TAG, "Failed to delete " + file);
                    success = false;
                }
            }
        }
        return success;
    }

    private static boolean isValidExtFilenameChar(char c) {
        switch (c) {
            case '\u0000' :
            case '/' :
                return false;
            default :
                return true;
        }
    }

    /**
     * Check if given filename is valid for an ext4 filesystem.
     */
    public static boolean isValidExtFilename(java.lang.String name) {
        return (name != null) && name.equals(android.os.FileUtils.buildValidExtFilename(name));
    }

    /**
     * Mutate the given filename to make it valid for an ext4 filesystem,
     * replacing any invalid characters with "_".
     */
    public static java.lang.String buildValidExtFilename(java.lang.String name) {
        if ((android.text.TextUtils.isEmpty(name) || ".".equals(name)) || "..".equals(name)) {
            return "(invalid)";
        }
        final java.lang.StringBuilder res = new java.lang.StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (android.os.FileUtils.isValidExtFilenameChar(c)) {
                res.append(c);
            } else {
                res.append('_');
            }
        }
        android.os.FileUtils.trimFilename(res, 255);
        return res.toString();
    }

    private static boolean isValidFatFilenameChar(char c) {
        if ((0x0 <= c) && (c <= 0x1f)) {
            return false;
        }
        switch (c) {
            case '"' :
            case '*' :
            case '/' :
            case ':' :
            case '<' :
            case '>' :
            case '?' :
            case '\\' :
            case '|' :
            case 0x7f :
                return false;
            default :
                return true;
        }
    }

    /**
     * Check if given filename is valid for a FAT filesystem.
     */
    public static boolean isValidFatFilename(java.lang.String name) {
        return (name != null) && name.equals(android.os.FileUtils.buildValidFatFilename(name));
    }

    /**
     * Mutate the given filename to make it valid for a FAT filesystem,
     * replacing any invalid characters with "_".
     */
    public static java.lang.String buildValidFatFilename(java.lang.String name) {
        if ((android.text.TextUtils.isEmpty(name) || ".".equals(name)) || "..".equals(name)) {
            return "(invalid)";
        }
        final java.lang.StringBuilder res = new java.lang.StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (android.os.FileUtils.isValidFatFilenameChar(c)) {
                res.append(c);
            } else {
                res.append('_');
            }
        }
        // Even though vfat allows 255 UCS-2 chars, we might eventually write to
        // ext4 through a FUSE layer, so use that limit.
        android.os.FileUtils.trimFilename(res, 255);
        return res.toString();
    }

    @com.android.internal.annotations.VisibleForTesting
    public static java.lang.String trimFilename(java.lang.String str, int maxBytes) {
        final java.lang.StringBuilder res = new java.lang.StringBuilder(str);
        android.os.FileUtils.trimFilename(res, maxBytes);
        return res.toString();
    }

    private static void trimFilename(java.lang.StringBuilder res, int maxBytes) {
        byte[] raw = res.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (raw.length > maxBytes) {
            maxBytes -= 3;
            while (raw.length > maxBytes) {
                res.deleteCharAt(res.length() / 2);
                raw = res.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
            } 
            res.insert(res.length() / 2, "...");
        }
    }

    public static java.lang.String rewriteAfterRename(java.io.File beforeDir, java.io.File afterDir, java.lang.String path) {
        if (path == null)
            return null;

        final java.io.File result = android.os.FileUtils.rewriteAfterRename(beforeDir, afterDir, new java.io.File(path));
        return result != null ? result.getAbsolutePath() : null;
    }

    public static java.lang.String[] rewriteAfterRename(java.io.File beforeDir, java.io.File afterDir, java.lang.String[] paths) {
        if (paths == null)
            return null;

        final java.lang.String[] result = new java.lang.String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            result[i] = android.os.FileUtils.rewriteAfterRename(beforeDir, afterDir, paths[i]);
        }
        return result;
    }

    /**
     * Given a path under the "before" directory, rewrite it to live under the
     * "after" directory. For example, {@code /before/foo/bar.txt} would become
     * {@code /after/foo/bar.txt}.
     */
    public static java.io.File rewriteAfterRename(java.io.File beforeDir, java.io.File afterDir, java.io.File file) {
        if (((file == null) || (beforeDir == null)) || (afterDir == null))
            return null;

        if (android.os.FileUtils.contains(beforeDir, file)) {
            final java.lang.String splice = file.getAbsolutePath().substring(beforeDir.getAbsolutePath().length());
            return new java.io.File(afterDir, splice);
        }
        return null;
    }

    private static java.io.File buildUniqueFileWithExtension(java.io.File parent, java.lang.String name, java.lang.String ext) throws java.io.FileNotFoundException {
        java.io.File file = android.os.FileUtils.buildFile(parent, name, ext);
        // If conflicting file, try adding counter suffix
        int n = 0;
        while (file.exists()) {
            if ((n++) >= 32) {
                throw new java.io.FileNotFoundException("Failed to create unique file");
            }
            file = android.os.FileUtils.buildFile(parent, ((name + " (") + n) + ")", ext);
        } 
        return file;
    }

    /**
     * Generates a unique file name under the given parent directory. If the display name doesn't
     * have an extension that matches the requested MIME type, the default extension for that MIME
     * type is appended. If a file already exists, the name is appended with a numerical value to
     * make it unique.
     *
     * For example, the display name 'example' with 'text/plain' MIME might produce
     * 'example.txt' or 'example (1).txt', etc.
     *
     * @throws FileNotFoundException
     * 		
     */
    public static java.io.File buildUniqueFile(java.io.File parent, java.lang.String mimeType, java.lang.String displayName) throws java.io.FileNotFoundException {
        final java.lang.String[] parts = android.os.FileUtils.splitFileName(mimeType, displayName);
        return android.os.FileUtils.buildUniqueFileWithExtension(parent, parts[0], parts[1]);
    }

    /**
     * Generates a unique file name under the given parent directory, keeping
     * any extension intact.
     */
    public static java.io.File buildUniqueFile(java.io.File parent, java.lang.String displayName) throws java.io.FileNotFoundException {
        final java.lang.String name;
        final java.lang.String ext;
        // Extract requested extension from display name
        final int lastDot = displayName.lastIndexOf('.');
        if (lastDot >= 0) {
            name = displayName.substring(0, lastDot);
            ext = displayName.substring(lastDot + 1);
        } else {
            name = displayName;
            ext = null;
        }
        return android.os.FileUtils.buildUniqueFileWithExtension(parent, name, ext);
    }

    /**
     * Splits file name into base name and extension.
     * If the display name doesn't have an extension that matches the requested MIME type, the
     * extension is regarded as a part of filename and default extension for that MIME type is
     * appended.
     */
    public static java.lang.String[] splitFileName(java.lang.String mimeType, java.lang.String displayName) {
        java.lang.String name;
        java.lang.String ext;
        if (android.provider.DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType)) {
            name = displayName;
            ext = null;
        } else {
            java.lang.String mimeTypeFromExt;
            // Extract requested extension from display name
            final int lastDot = displayName.lastIndexOf('.');
            if (lastDot >= 0) {
                name = displayName.substring(0, lastDot);
                ext = displayName.substring(lastDot + 1);
                mimeTypeFromExt = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
            } else {
                name = displayName;
                ext = null;
                mimeTypeFromExt = null;
            }
            if (mimeTypeFromExt == null) {
                mimeTypeFromExt = "application/octet-stream";
            }
            final java.lang.String extFromMimeType = android.webkit.MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            if (java.util.Objects.equals(mimeType, mimeTypeFromExt) || java.util.Objects.equals(ext, extFromMimeType)) {
                // Extension maps back to requested MIME type; allow it
            } else {
                // No match; insist that create file matches requested MIME
                name = displayName;
                ext = extFromMimeType;
            }
        }
        if (ext == null) {
            ext = "";
        }
        return new java.lang.String[]{ name, ext };
    }

    private static java.io.File buildFile(java.io.File parent, java.lang.String name, java.lang.String ext) {
        if (android.text.TextUtils.isEmpty(ext)) {
            return new java.io.File(parent, name);
        } else {
            return new java.io.File(parent, (name + ".") + ext);
        }
    }

    @android.annotation.NonNull
    public static java.lang.String[] listOrEmpty(@android.annotation.Nullable
    java.io.File dir) {
        if (dir == null)
            return libcore.util.EmptyArray.STRING;

        final java.lang.String[] res = dir.list();
        if (res != null) {
            return res;
        } else {
            return libcore.util.EmptyArray.STRING;
        }
    }

    @android.annotation.NonNull
    public static java.io.File[] listFilesOrEmpty(@android.annotation.Nullable
    java.io.File dir) {
        if (dir == null)
            return android.os.FileUtils.EMPTY;

        final java.io.File[] res = dir.listFiles();
        if (res != null) {
            return res;
        } else {
            return android.os.FileUtils.EMPTY;
        }
    }

    @android.annotation.NonNull
    public static java.io.File[] listFilesOrEmpty(@android.annotation.Nullable
    java.io.File dir, java.io.FilenameFilter filter) {
        if (dir == null)
            return android.os.FileUtils.EMPTY;

        final java.io.File[] res = dir.listFiles(filter);
        if (res != null) {
            return res;
        } else {
            return android.os.FileUtils.EMPTY;
        }
    }

    @android.annotation.Nullable
    public static java.io.File newFileOrNull(@android.annotation.Nullable
    java.lang.String path) {
        return path != null ? new java.io.File(path) : null;
    }
}

