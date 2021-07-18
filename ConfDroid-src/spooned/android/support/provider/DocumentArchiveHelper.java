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
 * files within archives exposed by a document provider.
 *
 * <p>This class is thread safe. All methods can be called on any thread without
 * synchronization.
 *
 * TODO: Update the documentation. b/26047732
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class DocumentArchiveHelper implements java.io.Closeable {
    /**
     * Cursor column to be used for passing the local file path for documents.
     * If it's not specified, then a snapshot will be created, which is slower
     * and consumes more resources.
     *
     * <p>Type: STRING
     */
    public static final java.lang.String COLUMN_LOCAL_FILE_PATH = "local_file_path";

    private static final java.lang.String TAG = "DocumentArchiveHelper";

    private static final int OPENED_ARCHIVES_CACHE_SIZE = 4;

    private static final java.lang.String[] ZIP_MIME_TYPES = new java.lang.String[]{ "application/zip", "application/x-zip", "application/x-zip-compressed" };

    private final android.provider.DocumentsProvider mProvider;

    private final char mIdDelimiter;

    // @GuardedBy("mArchives")
    private final android.util.LruCache<java.lang.String, android.support.provider.DocumentArchiveHelper.Loader> mArchives = new android.util.LruCache<java.lang.String, android.support.provider.DocumentArchiveHelper.Loader>(android.support.provider.DocumentArchiveHelper.OPENED_ARCHIVES_CACHE_SIZE) {
        @java.lang.Override
        public void entryRemoved(boolean evicted, java.lang.String key, android.support.provider.DocumentArchiveHelper.Loader oldValue, android.support.provider.DocumentArchiveHelper.Loader newValue) {
            oldValue.getWriteLock().lock();
            try {
                oldValue.get().close();
            } catch (java.io.FileNotFoundException e) {
                android.util.Log.e(android.support.provider.DocumentArchiveHelper.TAG, "Failed to close an archive as it no longer exists.");
            } finally {
                oldValue.getWriteLock().unlock();
            }
        }
    };

    /**
     * Creates a helper for handling archived documents.
     *
     * @param provider
     * 		Instance of a documents provider which provides archived documents.
     * @param idDelimiter
     * 		A character used to create document IDs within archives. Can be any
     * 		character which is not used in any other document ID. If your provider uses
     * 		numbers as document IDs, the delimiter can be eg. a colon. However if your
     * 		provider uses paths, then a delimiter can be any character not allowed in the
     * 		path, which is often \0.
     */
    public DocumentArchiveHelper(android.provider.DocumentsProvider provider, char idDelimiter) {
        mProvider = provider;
        mIdDelimiter = idDelimiter;
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
        android.support.provider.DocumentArchiveHelper.Loader loader = null;
        try {
            loader = obtainInstance(documentId);
            return loader.get().queryChildDocuments(documentId, projection, sortOrder);
        } finally {
            releaseInstance(loader);
        }
    }

    /**
     * Returns a MIME type of a document within an archive.
     *
     * @see DocumentsProvider.getDocumentType(String)
     */
    public java.lang.String getDocumentType(java.lang.String documentId) throws java.io.FileNotFoundException {
        android.support.provider.DocumentArchiveHelper.Loader loader = null;
        try {
            loader = obtainInstance(documentId);
            return loader.get().getDocumentType(documentId);
        } finally {
            releaseInstance(loader);
        }
    }

    /**
     * Returns true if a document within an archive is a child or any descendant of the archive
     * document or another document within the archive.
     *
     * @see DocumentsProvider.isChildDocument(String, String)
     */
    public boolean isChildDocument(java.lang.String parentDocumentId, java.lang.String documentId) {
        android.support.provider.DocumentArchiveHelper.Loader loader = null;
        try {
            loader = obtainInstance(documentId);
            return loader.get().isChildDocument(parentDocumentId, documentId);
        } catch (java.io.FileNotFoundException e) {
            throw new java.lang.IllegalStateException(e);
        } finally {
            releaseInstance(loader);
        }
    }

    /**
     * Returns metadata of a document within an archive.
     *
     * @see DocumentsProvider.queryDocument(String, String[])
     */
    public android.database.Cursor queryDocument(java.lang.String documentId, @android.support.annotation.Nullable
    java.lang.String[] projection) throws java.io.FileNotFoundException {
        android.support.provider.DocumentArchiveHelper.Loader loader = null;
        try {
            loader = obtainInstance(documentId);
            return loader.get().queryDocument(documentId, projection);
        } finally {
            releaseInstance(loader);
        }
    }

    /**
     * Opens a file within an archive.
     *
     * @see DocumentsProvider.openDocument(String, String, CancellationSignal))
     */
    public android.os.ParcelFileDescriptor openDocument(java.lang.String documentId, java.lang.String mode, final android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        android.support.provider.DocumentArchiveHelper.Loader loader = null;
        try {
            loader = obtainInstance(documentId);
            return loader.get().openDocument(documentId, mode, signal);
        } finally {
            releaseInstance(loader);
        }
    }

    /**
     * Opens a thumbnail of a file within an archive.
     *
     * @see DocumentsProvider.openDocumentThumbnail(String, Point, CancellationSignal))
     */
    public android.content.res.AssetFileDescriptor openDocumentThumbnail(java.lang.String documentId, android.graphics.Point sizeHint, final android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        android.support.provider.DocumentArchiveHelper.Loader loader = null;
        try {
            loader = obtainInstance(documentId);
            return loader.get().openDocumentThumbnail(documentId, sizeHint, signal);
        } finally {
            releaseInstance(loader);
        }
    }

    /**
     * Returns true if the passed document ID is for a document within an archive.
     */
    public boolean isArchivedDocument(java.lang.String documentId) {
        return android.support.provider.ParsedDocumentId.hasPath(documentId, mIdDelimiter);
    }

    /**
     * Returns true if the passed mime type is supported by the helper.
     */
    public boolean isSupportedArchiveType(java.lang.String mimeType) {
        for (final java.lang.String zipMimeType : android.support.provider.DocumentArchiveHelper.ZIP_MIME_TYPES) {
            if (zipMimeType.equals(mimeType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Closes the helper and disposes all existing archives. It will block until all ongoing
     * operations on each opened archive are finished.
     */
    @java.lang.Override
    public void close() {
        synchronized(mArchives) {
            mArchives.evictAll();
        }
    }

    /**
     * Releases resources for an archive with the specified document ID. It will block until all
     * operations on the archive are finished. If not opened, the method does nothing.
     *
     * <p>Calling this method is optional. The helper automatically closes the least recently used
     * archives if too many archives are opened.
     *
     * @param archiveDocumentId
     * 		ID of the archive file.
     */
    public void closeArchive(java.lang.String documentId) {
        synchronized(mArchives) {
            mArchives.remove(documentId);
        }
    }

    private android.support.provider.DocumentArchiveHelper.Loader obtainInstance(java.lang.String documentId) throws java.io.FileNotFoundException {
        android.support.provider.DocumentArchiveHelper.Loader loader;
        synchronized(mArchives) {
            loader = getInstanceUncheckedLocked(documentId);
            loader.getReadLock().lock();
        }
        return loader;
    }

    private void releaseInstance(@android.support.annotation.Nullable
    android.support.provider.DocumentArchiveHelper.Loader loader) {
        if (loader != null) {
            loader.getReadLock().unlock();
        }
    }

    private android.support.provider.DocumentArchiveHelper.Loader getInstanceUncheckedLocked(java.lang.String documentId) throws java.io.FileNotFoundException {
        try {
            final android.support.provider.ParsedDocumentId id = android.support.provider.ParsedDocumentId.fromDocumentId(documentId, mIdDelimiter);
            if (mArchives.get(id.mArchiveId) != null) {
                return mArchives.get(id.mArchiveId);
            }
            final android.database.Cursor cursor = mProvider.queryDocument(id.mArchiveId, new java.lang.String[]{ android.provider.DocumentsContract.Document.COLUMN_MIME_TYPE, android.support.provider.DocumentArchiveHelper.COLUMN_LOCAL_FILE_PATH });
            cursor.moveToFirst();
            final java.lang.String mimeType = cursor.getString(cursor.getColumnIndex(android.provider.DocumentsContract.Document.COLUMN_MIME_TYPE));
            android.support.provider.Preconditions.checkArgument(isSupportedArchiveType(mimeType), "Unsupported archive type.");
            final int columnIndex = cursor.getColumnIndex(android.support.provider.DocumentArchiveHelper.COLUMN_LOCAL_FILE_PATH);
            final java.lang.String localFilePath = (columnIndex != (-1)) ? cursor.getString(columnIndex) : null;
            final java.io.File localFile = (localFilePath != null) ? new java.io.File(localFilePath) : null;
            final android.net.Uri notificationUri = cursor.getNotificationUri();
            final android.support.provider.DocumentArchiveHelper.Loader loader = new android.support.provider.DocumentArchiveHelper.Loader(mProvider, localFile, id, mIdDelimiter, notificationUri);
            // Remove the instance from mArchives collection once the archive file changes.
            if (notificationUri != null) {
                final android.util.LruCache<java.lang.String, android.support.provider.DocumentArchiveHelper.Loader> finalArchives = mArchives;
                mProvider.getContext().getContentResolver().registerContentObserver(notificationUri, false, new android.database.ContentObserver(null) {
                    @java.lang.Override
                    public void onChange(boolean selfChange, android.net.Uri uri) {
                        synchronized(mArchives) {
                            final android.support.provider.DocumentArchiveHelper.Loader currentLoader = mArchives.get(id.mArchiveId);
                            if (currentLoader == loader) {
                                mArchives.remove(id.mArchiveId);
                            }
                        }
                    }
                });
            }
            mArchives.put(id.mArchiveId, loader);
            return loader;
        } catch (java.io.IOException e) {
            // DocumentsProvider doesn't use IOException. For consistency convert it to
            // IllegalStateException.
            throw new java.lang.IllegalStateException(e);
        }
    }

    /**
     * Loads an instance of DocumentArchive lazily.
     */
    private static final class Loader {
        private final android.provider.DocumentsProvider mProvider;

        private final java.io.File mLocalFile;

        private final android.support.provider.ParsedDocumentId mId;

        private final char mIdDelimiter;

        private final android.net.Uri mNotificationUri;

        private final java.util.concurrent.locks.ReentrantReadWriteLock mLock = new java.util.concurrent.locks.ReentrantReadWriteLock();

        private android.support.provider.DocumentArchive mArchive = null;

        Loader(android.provider.DocumentsProvider provider, @android.support.annotation.Nullable
        java.io.File localFile, android.support.provider.ParsedDocumentId id, char idDelimiter, android.net.Uri notificationUri) {
            this.mProvider = provider;
            this.mLocalFile = localFile;
            this.mId = id;
            this.mIdDelimiter = idDelimiter;
            this.mNotificationUri = notificationUri;
        }

        synchronized android.support.provider.DocumentArchive get() throws java.io.FileNotFoundException {
            if (mArchive != null) {
                return mArchive;
            }
            try {
                if (mLocalFile != null) {
                    mArchive = android.support.provider.DocumentArchive.createForLocalFile(mProvider.getContext(), mLocalFile, mId.mArchiveId, mIdDelimiter, mNotificationUri);
                } else {
                    mArchive = android.support.provider.DocumentArchive.createForParcelFileDescriptor(mProvider.getContext(), /* signal */
                    mProvider.openDocument(mId.mArchiveId, "r", null), mId.mArchiveId, mIdDelimiter, mNotificationUri);
                }
            } catch (java.io.IOException e) {
                throw new java.lang.IllegalStateException(e);
            }
            return mArchive;
        }

        java.util.concurrent.locks.Lock getReadLock() {
            return mLock.readLock();
        }

        java.util.concurrent.locks.Lock getWriteLock() {
            return mLock.writeLock();
        }
    }
}

