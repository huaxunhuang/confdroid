/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.provider;


/**
 * Provides basic implementation for creating, extracting and accessing
 * files within archives exposed by a document provider. The id delimiter
 * must be a character which is not used in document ids generated by the
 * document provider.
 *
 * <p>This class is thread safe.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class DocumentArchive implements java.io.Closeable {
    private static final java.lang.String TAG = "DocumentArchive";

    private static final java.lang.String[] DEFAULT_PROJECTION = new java.lang.String[]{ android.provider.DocumentsContract.Document.COLUMN_DOCUMENT_ID, android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME, android.provider.DocumentsContract.Document.COLUMN_MIME_TYPE, android.provider.DocumentsContract.Document.COLUMN_SIZE, android.provider.DocumentsContract.Document.COLUMN_FLAGS };

    private final android.content.Context mContext;

    private final java.lang.String mDocumentId;

    private final char mIdDelimiter;

    private final android.net.Uri mNotificationUri;

    private final java.util.zip.ZipFile mZipFile;

    private final java.util.concurrent.ExecutorService mExecutor;

    private final java.util.Map<java.lang.String, java.util.zip.ZipEntry> mEntries;

    private final java.util.Map<java.lang.String, java.util.List<java.util.zip.ZipEntry>> mTree;

    private DocumentArchive(android.content.Context context, java.io.File file, java.lang.String documentId, char idDelimiter, @android.support.annotation.Nullable
    android.net.Uri notificationUri) throws java.io.IOException {
        mContext = context;
        mDocumentId = documentId;
        mIdDelimiter = idDelimiter;
        mNotificationUri = notificationUri;
        mZipFile = new java.util.zip.ZipFile(file);
        mExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
        // Build the tree structure in memory.
        mTree = new java.util.HashMap<java.lang.String, java.util.List<java.util.zip.ZipEntry>>();
        mTree.put("/", new java.util.ArrayList<java.util.zip.ZipEntry>());
        mEntries = new java.util.HashMap<java.lang.String, java.util.zip.ZipEntry>();
        java.util.zip.ZipEntry entry;
        final java.util.List<? extends java.util.zip.ZipEntry> entries = java.util.Collections.list(mZipFile.entries());
        final java.util.Stack<java.util.zip.ZipEntry> stack = new java.util.Stack<>();
        for (int i = entries.size() - 1; i >= 0; i--) {
            entry = entries.get(i);
            if (entry.isDirectory() != entry.getName().endsWith("/")) {
                throw new java.io.IOException("Directories must have a trailing slash, and files must not.");
            }
            if (mEntries.containsKey(entry.getName())) {
                throw new java.io.IOException("Multiple entries with the same name are not supported.");
            }
            mEntries.put(entry.getName(), entry);
            if (entry.isDirectory()) {
                mTree.put(entry.getName(), new java.util.ArrayList<java.util.zip.ZipEntry>());
            }
            stack.push(entry);
        }
        int delimiterIndex;
        java.lang.String parentPath;
        java.util.zip.ZipEntry parentEntry;
        java.util.List<java.util.zip.ZipEntry> parentList;
        while (stack.size() > 0) {
            entry = stack.pop();
            delimiterIndex = entry.getName().lastIndexOf('/', entry.isDirectory() ? entry.getName().length() - 2 : entry.getName().length() - 1);
            parentPath = (delimiterIndex != (-1)) ? entry.getName().substring(0, delimiterIndex) + "/" : "/";
            parentList = mTree.get(parentPath);
            if (parentList == null) {
                parentEntry = mEntries.get(parentPath);
                if (parentEntry == null) {
                    // The ZIP file doesn't contain all directories leading to the entry.
                    // It's rare, but can happen in a valid ZIP archive. In such case create a
                    // fake ZipEntry and add it on top of the stack to process it next.
                    parentEntry = new java.util.zip.ZipEntry(parentPath);
                    parentEntry.setSize(0);
                    parentEntry.setTime(entry.getTime());
                    mEntries.put(parentPath, parentEntry);
                    stack.push(parentEntry);
                }
                parentList = new java.util.ArrayList<java.util.zip.ZipEntry>();
                mTree.put(parentPath, parentList);
            }
            parentList.add(entry);
        } 
    }

    /**
     * Creates a DocumentsArchive instance for opening, browsing and accessing
     * documents within the archive passed as a local file.
     *
     * @param context
     * 		Context of the provider.
     * @param File
     * 		Local file containing the archive.
     * @param documentId
     * 		ID of the archive document.
     * @param idDelimiter
     * 		Delimiter for constructing IDs of documents within the archive.
     * 		The delimiter must never be used for IDs of other documents.
     * @param Uri
     * 		notificationUri Uri for notifying that the archive file has changed.
     * @see createForParcelFileDescriptor(DocumentsProvider, ParcelFileDescriptor, String, char,
    Uri)
     */
    public static android.support.provider.DocumentArchive createForLocalFile(android.content.Context context, java.io.File file, java.lang.String documentId, char idDelimiter, @android.support.annotation.Nullable
    android.net.Uri notificationUri) throws java.io.IOException {
        return new android.support.provider.DocumentArchive(context, file, documentId, idDelimiter, notificationUri);
    }

    /**
     * Creates a DocumentsArchive instance for opening, browsing and accessing
     * documents within the archive passed as a file descriptor.
     *
     * <p>Note, that this method should be used only if the document does not exist
     * on the local storage. A snapshot file will be created, which may be slower
     * and consume significant resources, in contrast to using
     * {@see createForLocalFile(Context, File, String, char, Uri}.
     *
     * @param context
     * 		Context of the provider.
     * @param descriptor
     * 		File descriptor for the archive's contents.
     * @param documentId
     * 		ID of the archive document.
     * @param idDelimiter
     * 		Delimiter for constructing IDs of documents within the archive.
     * 		The delimiter must never be used for IDs of other documents.
     * @param Uri
     * 		notificationUri Uri for notifying that the archive file has changed.
     * @see createForLocalFile(Context, File, String, char, Uri)
     */
    public static android.support.provider.DocumentArchive createForParcelFileDescriptor(android.content.Context context, android.os.ParcelFileDescriptor descriptor, java.lang.String documentId, char idDelimiter, @android.support.annotation.Nullable
    android.net.Uri notificationUri) throws java.io.IOException {
        java.io.File snapshotFile = null;
        try {
            // Create a copy of the archive, as ZipFile doesn't operate on streams.
            // Moreover, ZipInputStream would be inefficient for large files on
            // pipes.
            snapshotFile = java.io.File.createTempFile("android.support.provider.snapshot{", "}.zip", context.getCacheDir());
            try (final java.io.FileOutputStream outputStream = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(android.os.ParcelFileDescriptor.open(snapshotFile, android.os.ParcelFileDescriptor.MODE_WRITE_ONLY));final android.os.ParcelFileDescriptor.AutoCloseInputStream inputStream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(descriptor)) {
                final byte[] buffer = new byte[32 * 1024];
                int bytes;
                while ((bytes = inputStream.read(buffer)) != (-1)) {
                    outputStream.write(buffer, 0, bytes);
                } 
                outputStream.flush();
                return new android.support.provider.DocumentArchive(context, snapshotFile, documentId, idDelimiter, notificationUri);
            }
        } finally {
            // On UNIX the file will be still available for processes which opened it, even
            // after deleting it. Remove it ASAP, as it won't be used by anyone else.
            if (snapshotFile != null) {
                snapshotFile.delete();
            }
        }
    }

    /**
     * Lists child documents of an archive or a directory within an
     * archive. Must be called only for archives with supported mime type,
     * or for documents within archives.
     *
     * @see DocumentsProvider.queryChildDocuments(String, String[], String)
     */
    public android.database.Cursor queryChildDocuments(java.lang.String documentId, @android.support.annotation.Nullable
    java.lang.String[] projection, @android.support.annotation.Nullable
    java.lang.String sortOrder) throws java.io.FileNotFoundException {
        final android.support.provider.ParsedDocumentId parsedParentId = android.support.provider.ParsedDocumentId.fromDocumentId(documentId, mIdDelimiter);
        android.support.provider.Preconditions.checkArgumentEquals(mDocumentId, parsedParentId.mArchiveId, "Mismatching document ID. Expected: %s, actual: %s.");
        final java.lang.String parentPath = (parsedParentId.mPath != null) ? parsedParentId.mPath : "/";
        final android.database.MatrixCursor result = new android.database.MatrixCursor(projection != null ? projection : android.support.provider.DocumentArchive.DEFAULT_PROJECTION);
        if (mNotificationUri != null) {
            result.setNotificationUri(mContext.getContentResolver(), mNotificationUri);
        }
        final java.util.List<java.util.zip.ZipEntry> parentList = mTree.get(parentPath);
        if (parentList == null) {
            throw new java.io.FileNotFoundException();
        }
        for (final java.util.zip.ZipEntry entry : parentList) {
            addCursorRow(result, entry);
        }
        return result;
    }

    /**
     * Returns a MIME type of a document within an archive.
     *
     * @see DocumentsProvider.getDocumentType(String)
     */
    public java.lang.String getDocumentType(java.lang.String documentId) throws java.io.FileNotFoundException {
        final android.support.provider.ParsedDocumentId parsedId = android.support.provider.ParsedDocumentId.fromDocumentId(documentId, mIdDelimiter);
        android.support.provider.Preconditions.checkArgumentEquals(mDocumentId, parsedId.mArchiveId, "Mismatching document ID. Expected: %s, actual: %s.");
        android.support.provider.Preconditions.checkArgumentNotNull(parsedId.mPath, "Not a document within an archive.");
        final java.util.zip.ZipEntry entry = mEntries.get(parsedId.mPath);
        if (entry == null) {
            throw new java.io.FileNotFoundException();
        }
        return getMimeTypeForEntry(entry);
    }

    /**
     * Returns true if a document within an archive is a child or any descendant of the archive
     * document or another document within the archive.
     *
     * @see DocumentsProvider.isChildDocument(String, String)
     */
    public boolean isChildDocument(java.lang.String parentDocumentId, java.lang.String documentId) {
        final android.support.provider.ParsedDocumentId parsedParentId = android.support.provider.ParsedDocumentId.fromDocumentId(parentDocumentId, mIdDelimiter);
        final android.support.provider.ParsedDocumentId parsedId = android.support.provider.ParsedDocumentId.fromDocumentId(documentId, mIdDelimiter);
        android.support.provider.Preconditions.checkArgumentEquals(mDocumentId, parsedParentId.mArchiveId, "Mismatching document ID. Expected: %s, actual: %s.");
        android.support.provider.Preconditions.checkArgumentNotNull(parsedId.mPath, "Not a document within an archive.");
        final java.util.zip.ZipEntry entry = mEntries.get(parsedId.mPath);
        if (entry == null) {
            return false;
        }
        if (parsedParentId.mPath == null) {
            // No need to compare paths. Every file in the archive is a child of the archive
            // file.
            return true;
        }
        final java.util.zip.ZipEntry parentEntry = mEntries.get(parsedParentId.mPath);
        if ((parentEntry == null) || (!parentEntry.isDirectory())) {
            return false;
        }
        final java.lang.String parentPath = entry.getName();
        // Add a trailing slash even if it's not a directory, so it's easy to check if the
        // entry is a descendant.
        final java.lang.String pathWithSlash = (entry.isDirectory()) ? entry.getName() : entry.getName() + "/";
        return pathWithSlash.startsWith(parentPath) && (!parentPath.equals(pathWithSlash));
    }

    /**
     * Returns metadata of a document within an archive.
     *
     * @see DocumentsProvider.queryDocument(String, String[])
     */
    public android.database.Cursor queryDocument(java.lang.String documentId, @android.support.annotation.Nullable
    java.lang.String[] projection) throws java.io.FileNotFoundException {
        final android.support.provider.ParsedDocumentId parsedId = android.support.provider.ParsedDocumentId.fromDocumentId(documentId, mIdDelimiter);
        android.support.provider.Preconditions.checkArgumentEquals(mDocumentId, parsedId.mArchiveId, "Mismatching document ID. Expected: %s, actual: %s.");
        android.support.provider.Preconditions.checkArgumentNotNull(parsedId.mPath, "Not a document within an archive.");
        final java.util.zip.ZipEntry entry = mEntries.get(parsedId.mPath);
        if (entry == null) {
            throw new java.io.FileNotFoundException();
        }
        final android.database.MatrixCursor result = new android.database.MatrixCursor(projection != null ? projection : android.support.provider.DocumentArchive.DEFAULT_PROJECTION);
        if (mNotificationUri != null) {
            result.setNotificationUri(mContext.getContentResolver(), mNotificationUri);
        }
        addCursorRow(result, entry);
        return result;
    }

    /**
     * Opens a file within an archive.
     *
     * @see DocumentsProvider.openDocument(String, String, CancellationSignal))
     */
    public android.os.ParcelFileDescriptor openDocument(java.lang.String documentId, java.lang.String mode, @android.support.annotation.Nullable
    final android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        android.support.provider.Preconditions.checkArgumentEquals("r", mode, "Invalid mode. Only reading \"r\" supported, but got: \"%s\".");
        final android.support.provider.ParsedDocumentId parsedId = android.support.provider.ParsedDocumentId.fromDocumentId(documentId, mIdDelimiter);
        android.support.provider.Preconditions.checkArgumentEquals(mDocumentId, parsedId.mArchiveId, "Mismatching document ID. Expected: %s, actual: %s.");
        android.support.provider.Preconditions.checkArgumentNotNull(parsedId.mPath, "Not a document within an archive.");
        final java.util.zip.ZipEntry entry = mEntries.get(parsedId.mPath);
        if (entry == null) {
            throw new java.io.FileNotFoundException();
        }
        android.os.ParcelFileDescriptor[] pipe;
        java.io.InputStream inputStream = null;
        try {
            pipe = android.os.ParcelFileDescriptor.createReliablePipe();
            inputStream = mZipFile.getInputStream(entry);
        } catch (java.io.IOException e) {
            if (inputStream != null) {
                android.support.provider.IoUtils.closeQuietly(inputStream);
            }
            // Ideally we'd simply throw IOException to the caller, but for consistency
            // with DocumentsProvider::openDocument, converting it to IllegalStateException.
            throw new java.lang.IllegalStateException("Failed to open the document.", e);
        }
        final android.os.ParcelFileDescriptor outputPipe = pipe[1];
        final java.io.InputStream finalInputStream = inputStream;
        mExecutor.execute(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                try (final android.os.ParcelFileDescriptor.AutoCloseOutputStream outputStream = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(outputPipe)) {
                    try {
                        final byte[] buffer = new byte[32 * 1024];
                        int bytes;
                        while ((bytes = finalInputStream.read(buffer)) != (-1)) {
                            if (java.lang.Thread.interrupted()) {
                                throw new java.lang.InterruptedException();
                            }
                            if (signal != null) {
                                signal.throwIfCanceled();
                            }
                            outputStream.write(buffer, 0, bytes);
                        } 
                    } catch (java.io.IOException | java.lang.InterruptedException e) {
                        // Catch the exception before the outer try-with-resource closes the
                        // pipe with close() instead of closeWithError().
                        try {
                            outputPipe.closeWithError(e.getMessage());
                        } catch (java.io.IOException e2) {
                            android.util.Log.e(android.support.provider.DocumentArchive.TAG, "Failed to close the pipe after an error.", e2);
                        }
                    }
                } catch (android.os.OperationCanceledException e) {
                    // Cancelled gracefully.
                } catch (java.io.IOException e) {
                    android.util.Log.e(android.support.provider.DocumentArchive.TAG, "Failed to close the output stream gracefully.", e);
                } finally {
                    android.support.provider.IoUtils.closeQuietly(finalInputStream);
                }
            }
        });
        return pipe[0];
    }

    /**
     * Opens a thumbnail of a file within an archive.
     *
     * @see DocumentsProvider.openDocumentThumbnail(String, Point, CancellationSignal))
     */
    public android.content.res.AssetFileDescriptor openDocumentThumbnail(java.lang.String documentId, android.graphics.Point sizeHint, final android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        final android.support.provider.ParsedDocumentId parsedId = android.support.provider.ParsedDocumentId.fromDocumentId(documentId, mIdDelimiter);
        android.support.provider.Preconditions.checkArgumentEquals(mDocumentId, parsedId.mArchiveId, "Mismatching document ID. Expected: %s, actual: %s.");
        android.support.provider.Preconditions.checkArgumentNotNull(parsedId.mPath, "Not a document within an archive.");
        android.support.provider.Preconditions.checkArgument(getDocumentType(documentId).startsWith("image/"), "Thumbnails only supported for image/* MIME type.");
        final java.util.zip.ZipEntry entry = mEntries.get(parsedId.mPath);
        if (entry == null) {
            throw new java.io.FileNotFoundException();
        }
        java.io.InputStream inputStream = null;
        try {
            inputStream = mZipFile.getInputStream(entry);
            final android.media.ExifInterface exif = new android.media.ExifInterface(inputStream);
            if (exif.hasThumbnail()) {
                android.os.Bundle extras = null;
                switch (exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, -1)) {
                    case android.media.ExifInterface.ORIENTATION_ROTATE_90 :
                        extras = new android.os.Bundle(1);
                        extras.putInt(android.provider.DocumentsContract.EXTRA_ORIENTATION, 90);
                        break;
                    case android.media.ExifInterface.ORIENTATION_ROTATE_180 :
                        extras = new android.os.Bundle(1);
                        extras.putInt(android.provider.DocumentsContract.EXTRA_ORIENTATION, 180);
                        break;
                    case android.media.ExifInterface.ORIENTATION_ROTATE_270 :
                        extras = new android.os.Bundle(1);
                        extras.putInt(android.provider.DocumentsContract.EXTRA_ORIENTATION, 270);
                        break;
                }
                final long[] range = exif.getThumbnailRange();
                return new android.content.res.AssetFileDescriptor(openDocument(documentId, "r", signal), range[0], range[1], extras);
            }
        } catch (java.io.IOException e) {
            // Ignore the exception, as reading the EXIF may legally fail.
            android.util.Log.e(android.support.provider.DocumentArchive.TAG, "Failed to obtain thumbnail from EXIF.", e);
        } finally {
            android.support.provider.IoUtils.closeQuietly(inputStream);
        }
        return new android.content.res.AssetFileDescriptor(openDocument(documentId, "r", signal), 0, entry.getSize(), null);
    }

    /**
     * Schedules a gracefully close of the archive after any opened files are closed.
     *
     * <p>This method does not block until shutdown. Once called, other methods should not be
     * called.
     */
    @java.lang.Override
    public void close() {
        mExecutor.execute(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.support.provider.IoUtils.closeQuietly(mZipFile);
            }
        });
        mExecutor.shutdown();
    }

    private void addCursorRow(android.database.MatrixCursor cursor, java.util.zip.ZipEntry entry) {
        final android.database.MatrixCursor.RowBuilder row = cursor.newRow();
        final android.support.provider.ParsedDocumentId parsedId = new android.support.provider.ParsedDocumentId(mDocumentId, entry.getName());
        row.add(android.provider.DocumentsContract.Document.COLUMN_DOCUMENT_ID, parsedId.toDocumentId(mIdDelimiter));
        final java.io.File file = new java.io.File(entry.getName());
        row.add(android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME, file.getName());
        row.add(android.provider.DocumentsContract.Document.COLUMN_SIZE, entry.getSize());
        final java.lang.String mimeType = getMimeTypeForEntry(entry);
        row.add(android.provider.DocumentsContract.Document.COLUMN_MIME_TYPE, mimeType);
        final int flags = (mimeType.startsWith("image/")) ? android.provider.DocumentsContract.Document.FLAG_SUPPORTS_THUMBNAIL : 0;
        row.add(android.provider.DocumentsContract.Document.COLUMN_FLAGS, flags);
    }

    private java.lang.String getMimeTypeForEntry(java.util.zip.ZipEntry entry) {
        if (entry.isDirectory()) {
            return android.provider.DocumentsContract.Document.MIME_TYPE_DIR;
        }
        final int lastDot = entry.getName().lastIndexOf('.');
        if (lastDot >= 0) {
            final java.lang.String extension = entry.getName().substring(lastDot + 1).toLowerCase(java.util.Locale.US);
            final java.lang.String mimeType = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mimeType != null) {
                return mimeType;
            }
        }
        return "application/octet-stream";
    }
}

