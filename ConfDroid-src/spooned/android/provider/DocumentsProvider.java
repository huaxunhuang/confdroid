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
package android.provider;


/**
 * Base class for a document provider. A document provider offers read and write
 * access to durable files, such as files stored on a local disk, or files in a
 * cloud storage service. To create a document provider, extend this class,
 * implement the abstract methods, and add it to your manifest like this:
 *
 * <pre class="prettyprint">&lt;manifest&gt;
 *    ...
 *    &lt;application&gt;
 *        ...
 *        &lt;provider
 *            android:name="com.example.MyCloudProvider"
 *            android:authorities="com.example.mycloudprovider"
 *            android:exported="true"
 *            android:grantUriPermissions="true"
 *            android:permission="android.permission.MANAGE_DOCUMENTS"
 *            android:enabled="@bool/isAtLeastKitKat"&gt;
 *            &lt;intent-filter&gt;
 *                &lt;action android:name="android.content.action.DOCUMENTS_PROVIDER" /&gt;
 *            &lt;/intent-filter&gt;
 *        &lt;/provider&gt;
 *        ...
 *    &lt;/application&gt;
 * &lt;/manifest&gt;</pre>
 * <p>
 * When defining your provider, you must protect it with
 * {@link android.Manifest.permission#MANAGE_DOCUMENTS}, which is a permission
 * only the system can obtain. Applications cannot use a documents provider
 * directly; they must go through {@link Intent#ACTION_OPEN_DOCUMENT} or
 * {@link Intent#ACTION_CREATE_DOCUMENT} which requires a user to actively
 * navigate and select documents. When a user selects documents through that UI,
 * the system issues narrow URI permission grants to the requesting application.
 * </p>
 * <h3>Documents</h3>
 * <p>
 * A document can be either an openable stream (with a specific MIME type), or a
 * directory containing additional documents (with the
 * {@link Document#MIME_TYPE_DIR} MIME type). Each directory represents the top
 * of a subtree containing zero or more documents, which can recursively contain
 * even more documents and directories.
 * </p>
 * <p>
 * Each document can have different capabilities, as described by
 * {@link Document#COLUMN_FLAGS}. For example, if a document can be represented
 * as a thumbnail, your provider can set
 * {@link Document#FLAG_SUPPORTS_THUMBNAIL} and implement
 * {@link #openDocumentThumbnail(String, Point, CancellationSignal)} to return
 * that thumbnail.
 * </p>
 * <p>
 * Each document under a provider is uniquely referenced by its
 * {@link Document#COLUMN_DOCUMENT_ID}, which must not change once returned. A
 * single document can be included in multiple directories when responding to
 * {@link #queryChildDocuments(String, String[], String)}. For example, a
 * provider might surface a single photo in multiple locations: once in a
 * directory of geographic locations, and again in a directory of dates.
 * </p>
 * <h3>Roots</h3>
 * <p>
 * All documents are surfaced through one or more "roots." Each root represents
 * the top of a document tree that a user can navigate. For example, a root
 * could represent an account or a physical storage device. Similar to
 * documents, each root can have capabilities expressed through
 * {@link Root#COLUMN_FLAGS}.
 * </p>
 *
 * @see Intent#ACTION_OPEN_DOCUMENT
 * @see Intent#ACTION_OPEN_DOCUMENT_TREE
 * @see Intent#ACTION_CREATE_DOCUMENT
 */
public abstract class DocumentsProvider extends android.content.ContentProvider {
    private static final java.lang.String TAG = "DocumentsProvider";

    private static final int MATCH_ROOTS = 1;

    private static final int MATCH_ROOT = 2;

    private static final int MATCH_RECENT = 3;

    private static final int MATCH_SEARCH = 4;

    private static final int MATCH_DOCUMENT = 5;

    private static final int MATCH_CHILDREN = 6;

    private static final int MATCH_DOCUMENT_TREE = 7;

    private static final int MATCH_CHILDREN_TREE = 8;

    private java.lang.String mAuthority;

    private android.content.UriMatcher mMatcher;

    /**
     * Implementation is provided by the parent class.
     */
    @java.lang.Override
    public void attachInfo(android.content.Context context, android.content.pm.ProviderInfo info) {
        mAuthority = info.authority;
        mMatcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);
        mMatcher.addURI(mAuthority, "root", android.provider.DocumentsProvider.MATCH_ROOTS);
        mMatcher.addURI(mAuthority, "root/*", android.provider.DocumentsProvider.MATCH_ROOT);
        mMatcher.addURI(mAuthority, "root/*/recent", android.provider.DocumentsProvider.MATCH_RECENT);
        mMatcher.addURI(mAuthority, "root/*/search", android.provider.DocumentsProvider.MATCH_SEARCH);
        mMatcher.addURI(mAuthority, "document/*", android.provider.DocumentsProvider.MATCH_DOCUMENT);
        mMatcher.addURI(mAuthority, "document/*/children", android.provider.DocumentsProvider.MATCH_CHILDREN);
        mMatcher.addURI(mAuthority, "tree/*/document/*", android.provider.DocumentsProvider.MATCH_DOCUMENT_TREE);
        mMatcher.addURI(mAuthority, "tree/*/document/*/children", android.provider.DocumentsProvider.MATCH_CHILDREN_TREE);
        // Sanity check our setup
        if (!info.exported) {
            throw new java.lang.SecurityException("Provider must be exported");
        }
        if (!info.grantUriPermissions) {
            throw new java.lang.SecurityException("Provider must grantUriPermissions");
        }
        if ((!android.Manifest.permission.MANAGE_DOCUMENTS.equals(info.readPermission)) || (!android.Manifest.permission.MANAGE_DOCUMENTS.equals(info.writePermission))) {
            throw new java.lang.SecurityException("Provider must be protected by MANAGE_DOCUMENTS");
        }
        super.attachInfo(context, info);
    }

    /**
     * Test if a document is descendant (child, grandchild, etc) from the given
     * parent. For example, providers must implement this to support
     * {@link Intent#ACTION_OPEN_DOCUMENT_TREE}. You should avoid making network
     * requests to keep this request fast.
     *
     * @param parentDocumentId
     * 		parent to verify against.
     * @param documentId
     * 		child to verify.
     * @return if given document is a descendant of the given parent.
     * @see DocumentsContract.Root#FLAG_SUPPORTS_IS_CHILD
     */
    public boolean isChildDocument(java.lang.String parentDocumentId, java.lang.String documentId) {
        return false;
    }

    /**
     * {@hide }
     */
    private void enforceTree(android.net.Uri documentUri) {
        if (android.provider.DocumentsContract.isTreeUri(documentUri)) {
            final java.lang.String parent = android.provider.DocumentsContract.getTreeDocumentId(documentUri);
            final java.lang.String child = android.provider.DocumentsContract.getDocumentId(documentUri);
            if (java.util.Objects.equals(parent, child)) {
                return;
            }
            if (!isChildDocument(parent, child)) {
                throw new java.lang.SecurityException((("Document " + child) + " is not a descendant of ") + parent);
            }
        }
    }

    /**
     * Create a new document and return its newly generated
     * {@link Document#COLUMN_DOCUMENT_ID}. You must allocate a new
     * {@link Document#COLUMN_DOCUMENT_ID} to represent the document, which must
     * not change once returned.
     *
     * @param parentDocumentId
     * 		the parent directory to create the new document
     * 		under.
     * @param mimeType
     * 		the concrete MIME type associated with the new document.
     * 		If the MIME type is not supported, the provider must throw.
     * @param displayName
     * 		the display name of the new document. The provider may
     * 		alter this name to meet any internal constraints, such as
     * 		avoiding conflicting names.
     */
    @java.lang.SuppressWarnings("unused")
    public java.lang.String createDocument(java.lang.String parentDocumentId, java.lang.String mimeType, java.lang.String displayName) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Create not supported");
    }

    /**
     * Rename an existing document.
     * <p>
     * If a different {@link Document#COLUMN_DOCUMENT_ID} must be used to
     * represent the renamed document, generate and return it. Any outstanding
     * URI permission grants will be updated to point at the new document. If
     * the original {@link Document#COLUMN_DOCUMENT_ID} is still valid after the
     * rename, return {@code null}.
     *
     * @param documentId
     * 		the document to rename.
     * @param displayName
     * 		the updated display name of the document. The provider
     * 		may alter this name to meet any internal constraints, such as
     * 		avoiding conflicting names.
     */
    @java.lang.SuppressWarnings("unused")
    public java.lang.String renameDocument(java.lang.String documentId, java.lang.String displayName) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Rename not supported");
    }

    /**
     * Delete the requested document.
     * <p>
     * Upon returning, any URI permission grants for the given document will be
     * revoked. If additional documents were deleted as a side effect of this
     * call (such as documents inside a directory) the implementor is
     * responsible for revoking those permissions using
     * {@link #revokeDocumentPermission(String)}.
     *
     * @param documentId
     * 		the document to delete.
     */
    @java.lang.SuppressWarnings("unused")
    public void deleteDocument(java.lang.String documentId) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Delete not supported");
    }

    /**
     * Copy the requested document or a document tree.
     * <p>
     * Copies a document including all child documents to another location within
     * the same document provider. Upon completion returns the document id of
     * the copied document at the target destination. {@code null} must never
     * be returned.
     *
     * @param sourceDocumentId
     * 		the document to copy.
     * @param targetParentDocumentId
     * 		the target document to be copied into as a child.
     */
    @java.lang.SuppressWarnings("unused")
    public java.lang.String copyDocument(java.lang.String sourceDocumentId, java.lang.String targetParentDocumentId) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Copy not supported");
    }

    /**
     * Move the requested document or a document tree.
     *
     * <p>Moves a document including all child documents to another location within
     * the same document provider. Upon completion returns the document id of
     * the copied document at the target destination. {@code null} must never
     * be returned.
     *
     * <p>It's the responsibility of the provider to revoke grants if the document
     * is no longer accessible using <code>sourceDocumentId</code>.
     *
     * @param sourceDocumentId
     * 		the document to move.
     * @param sourceParentDocumentId
     * 		the parent of the document to move.
     * @param targetParentDocumentId
     * 		the target document to be a new parent of the
     * 		source document.
     */
    @java.lang.SuppressWarnings("unused")
    public java.lang.String moveDocument(java.lang.String sourceDocumentId, java.lang.String sourceParentDocumentId, java.lang.String targetParentDocumentId) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Move not supported");
    }

    /**
     * Removes the requested document or a document tree.
     *
     * <p>In contrast to {@link #deleteDocument} it requires specifying the parent.
     * This method is especially useful if the document can be in multiple parents.
     *
     * <p>It's the responsibility of the provider to revoke grants if the document is
     * removed from the last parent, and effectively the document is deleted.
     *
     * @param documentId
     * 		the document to remove.
     * @param parentDocumentId
     * 		the parent of the document to move.
     */
    @java.lang.SuppressWarnings("unused")
    public void removeDocument(java.lang.String documentId, java.lang.String parentDocumentId) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Remove not supported");
    }

    /**
     * Return all roots currently provided. To display to users, you must define
     * at least one root. You should avoid making network requests to keep this
     * request fast.
     * <p>
     * Each root is defined by the metadata columns described in {@link Root},
     * including {@link Root#COLUMN_DOCUMENT_ID} which points to a directory
     * representing a tree of documents to display under that root.
     * <p>
     * If this set of roots changes, you must call {@link ContentResolver#notifyChange(Uri,
     * android.database.ContentObserver, boolean)} with
     * {@link DocumentsContract#buildRootsUri(String)} to notify the system.
     *
     * @param projection
     * 		list of {@link Root} columns to put into the cursor. If
     * 		{@code null} all supported columns should be included.
     */
    public abstract android.database.Cursor queryRoots(java.lang.String[] projection) throws java.io.FileNotFoundException;

    /**
     * Return recently modified documents under the requested root. This will
     * only be called for roots that advertise
     * {@link Root#FLAG_SUPPORTS_RECENTS}. The returned documents should be
     * sorted by {@link Document#COLUMN_LAST_MODIFIED} in descending order, and
     * limited to only return the 64 most recently modified documents.
     * <p>
     * Recent documents do not support change notifications.
     *
     * @param projection
     * 		list of {@link Document} columns to put into the
     * 		cursor. If {@code null} all supported columns should be
     * 		included.
     * @see DocumentsContract#EXTRA_LOADING
     */
    @java.lang.SuppressWarnings("unused")
    public android.database.Cursor queryRecentDocuments(java.lang.String rootId, java.lang.String[] projection) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Recent not supported");
    }

    /**
     * Return metadata for the single requested document. You should avoid
     * making network requests to keep this request fast.
     *
     * @param documentId
     * 		the document to return.
     * @param projection
     * 		list of {@link Document} columns to put into the
     * 		cursor. If {@code null} all supported columns should be
     * 		included.
     */
    public abstract android.database.Cursor queryDocument(java.lang.String documentId, java.lang.String[] projection) throws java.io.FileNotFoundException;

    /**
     * Return the children documents contained in the requested directory. This
     * must only return immediate descendants, as additional queries will be
     * issued to recursively explore the tree.
     * <p>
     * If your provider is cloud-based, and you have some data cached or pinned
     * locally, you may return the local data immediately, setting
     * {@link DocumentsContract#EXTRA_LOADING} on the Cursor to indicate that
     * you are still fetching additional data. Then, when the network data is
     * available, you can send a change notification to trigger a requery and
     * return the complete contents. To return a Cursor with extras, you need to
     * extend and override {@link Cursor#getExtras()}.
     * <p>
     * To support change notifications, you must
     * {@link Cursor#setNotificationUri(ContentResolver, Uri)} with a relevant
     * Uri, such as
     * {@link DocumentsContract#buildChildDocumentsUri(String, String)}. Then
     * you can call {@link ContentResolver#notifyChange(Uri,
     * android.database.ContentObserver, boolean)} with that Uri to send change
     * notifications.
     *
     * @param parentDocumentId
     * 		the directory to return children for.
     * @param projection
     * 		list of {@link Document} columns to put into the
     * 		cursor. If {@code null} all supported columns should be
     * 		included.
     * @param sortOrder
     * 		how to order the rows, formatted as an SQL
     * 		{@code ORDER BY} clause (excluding the ORDER BY itself).
     * 		Passing {@code null} will use the default sort order, which
     * 		may be unordered. This ordering is a hint that can be used to
     * 		prioritize how data is fetched from the network, but UI may
     * 		always enforce a specific ordering.
     * @see DocumentsContract#EXTRA_LOADING
     * @see DocumentsContract#EXTRA_INFO
     * @see DocumentsContract#EXTRA_ERROR
     */
    public abstract android.database.Cursor queryChildDocuments(java.lang.String parentDocumentId, java.lang.String[] projection, java.lang.String sortOrder) throws java.io.FileNotFoundException;

    /**
     * {@hide }
     */
    @java.lang.SuppressWarnings("unused")
    public android.database.Cursor queryChildDocumentsForManage(java.lang.String parentDocumentId, java.lang.String[] projection, java.lang.String sortOrder) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Manage not supported");
    }

    /**
     * Return documents that match the given query under the requested
     * root. The returned documents should be sorted by relevance in descending
     * order. How documents are matched against the query string is an
     * implementation detail left to each provider, but it's suggested that at
     * least {@link Document#COLUMN_DISPLAY_NAME} be matched in a
     * case-insensitive fashion.
     * <p>
     * Only documents may be returned; directories are not supported in search
     * results.
     * <p>
     * If your provider is cloud-based, and you have some data cached or pinned
     * locally, you may return the local data immediately, setting
     * {@link DocumentsContract#EXTRA_LOADING} on the Cursor to indicate that
     * you are still fetching additional data. Then, when the network data is
     * available, you can send a change notification to trigger a requery and
     * return the complete contents.
     * <p>
     * To support change notifications, you must
     * {@link Cursor#setNotificationUri(ContentResolver, Uri)} with a relevant
     * Uri, such as {@link DocumentsContract#buildSearchDocumentsUri(String,
     * String, String)}. Then you can call {@link ContentResolver#notifyChange(Uri,
     * android.database.ContentObserver, boolean)} with that Uri to send change
     * notifications.
     *
     * @param rootId
     * 		the root to search under.
     * @param query
     * 		string to match documents against.
     * @param projection
     * 		list of {@link Document} columns to put into the
     * 		cursor. If {@code null} all supported columns should be
     * 		included.
     * @see DocumentsContract#EXTRA_LOADING
     * @see DocumentsContract#EXTRA_INFO
     * @see DocumentsContract#EXTRA_ERROR
     */
    @java.lang.SuppressWarnings("unused")
    public android.database.Cursor querySearchDocuments(java.lang.String rootId, java.lang.String query, java.lang.String[] projection) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Search not supported");
    }

    /**
     * Return concrete MIME type of the requested document. Must match the value
     * of {@link Document#COLUMN_MIME_TYPE} for this document. The default
     * implementation queries {@link #queryDocument(String, String[])}, so
     * providers may choose to override this as an optimization.
     */
    public java.lang.String getDocumentType(java.lang.String documentId) throws java.io.FileNotFoundException {
        final android.database.Cursor cursor = queryDocument(documentId, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(android.provider.DocumentsContract.Document.COLUMN_MIME_TYPE));
            } else {
                return null;
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(cursor);
        }
    }

    /**
     * Open and return the requested document.
     * <p>
     * Your provider should return a reliable {@link ParcelFileDescriptor} to
     * detect when the remote caller has finished reading or writing the
     * document. You may return a pipe or socket pair if the mode is exclusively
     * "r" or "w", but complex modes like "rw" imply a normal file on disk that
     * supports seeking.
     * <p>
     * If you block while downloading content, you should periodically check
     * {@link CancellationSignal#isCanceled()} to abort abandoned open requests.
     *
     * @param documentId
     * 		the document to return.
     * @param mode
     * 		the mode to open with, such as 'r', 'w', or 'rw'.
     * @param signal
     * 		used by the caller to signal if the request should be
     * 		cancelled. May be null.
     * @see ParcelFileDescriptor#open(java.io.File, int, android.os.Handler,
    OnCloseListener)
     * @see ParcelFileDescriptor#createReliablePipe()
     * @see ParcelFileDescriptor#createReliableSocketPair()
     * @see ParcelFileDescriptor#parseMode(String)
     */
    public abstract android.os.ParcelFileDescriptor openDocument(java.lang.String documentId, java.lang.String mode, android.os.CancellationSignal signal) throws java.io.FileNotFoundException;

    /**
     * Open and return a thumbnail of the requested document.
     * <p>
     * A provider should return a thumbnail closely matching the hinted size,
     * attempting to serve from a local cache if possible. A provider should
     * never return images more than double the hinted size.
     * <p>
     * If you perform expensive operations to download or generate a thumbnail,
     * you should periodically check {@link CancellationSignal#isCanceled()} to
     * abort abandoned thumbnail requests.
     *
     * @param documentId
     * 		the document to return.
     * @param sizeHint
     * 		hint of the optimal thumbnail dimensions.
     * @param signal
     * 		used by the caller to signal if the request should be
     * 		cancelled. May be null.
     * @see Document#FLAG_SUPPORTS_THUMBNAIL
     */
    @java.lang.SuppressWarnings("unused")
    public android.content.res.AssetFileDescriptor openDocumentThumbnail(java.lang.String documentId, android.graphics.Point sizeHint, android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("Thumbnails not supported");
    }

    /**
     * Open and return the document in a format matching the specified MIME
     * type filter.
     * <p>
     * A provider may perform a conversion if the documents's MIME type is not
     * matching the specified MIME type filter.
     *
     * @param documentId
     * 		the document to return.
     * @param mimeTypeFilter
     * 		the MIME type filter for the requested format. May
     * 		be *\/*, which matches any MIME type.
     * @param opts
     * 		extra options from the client. Specific to the content
     * 		provider.
     * @param signal
     * 		used by the caller to signal if the request should be
     * 		cancelled. May be null.
     * @see #getDocumentStreamTypes(String, String)
     */
    @java.lang.SuppressWarnings("unused")
    public android.content.res.AssetFileDescriptor openTypedDocument(java.lang.String documentId, java.lang.String mimeTypeFilter, android.os.Bundle opts, android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        throw new java.io.FileNotFoundException("The requested MIME type is not supported.");
    }

    /**
     * Implementation is provided by the parent class. Cannot be overriden.
     *
     * @see #queryRoots(String[])
     * @see #queryRecentDocuments(String, String[])
     * @see #queryDocument(String, String[])
     * @see #queryChildDocuments(String, String[], String)
     * @see #querySearchDocuments(String, String, String[])
     */
    @java.lang.Override
    public final android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        try {
            switch (mMatcher.match(uri)) {
                case android.provider.DocumentsProvider.MATCH_ROOTS :
                    return queryRoots(projection);
                case android.provider.DocumentsProvider.MATCH_RECENT :
                    return queryRecentDocuments(android.provider.DocumentsContract.getRootId(uri), projection);
                case android.provider.DocumentsProvider.MATCH_SEARCH :
                    return querySearchDocuments(android.provider.DocumentsContract.getRootId(uri), android.provider.DocumentsContract.getSearchDocumentsQuery(uri), projection);
                case android.provider.DocumentsProvider.MATCH_DOCUMENT :
                case android.provider.DocumentsProvider.MATCH_DOCUMENT_TREE :
                    enforceTree(uri);
                    return queryDocument(android.provider.DocumentsContract.getDocumentId(uri), projection);
                case android.provider.DocumentsProvider.MATCH_CHILDREN :
                case android.provider.DocumentsProvider.MATCH_CHILDREN_TREE :
                    enforceTree(uri);
                    if (android.provider.DocumentsContract.isManageMode(uri)) {
                        return queryChildDocumentsForManage(android.provider.DocumentsContract.getDocumentId(uri), projection, sortOrder);
                    } else {
                        return queryChildDocuments(android.provider.DocumentsContract.getDocumentId(uri), projection, sortOrder);
                    }
                default :
                    throw new java.lang.UnsupportedOperationException("Unsupported Uri " + uri);
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Log.w(android.provider.DocumentsProvider.TAG, "Failed during query", e);
            return null;
        }
    }

    /**
     * Implementation is provided by the parent class. Cannot be overriden.
     *
     * @see #getDocumentType(String)
     */
    @java.lang.Override
    public final java.lang.String getType(android.net.Uri uri) {
        try {
            switch (mMatcher.match(uri)) {
                case android.provider.DocumentsProvider.MATCH_ROOT :
                    return android.provider.DocumentsContract.Root.MIME_TYPE_ITEM;
                case android.provider.DocumentsProvider.MATCH_DOCUMENT :
                case android.provider.DocumentsProvider.MATCH_DOCUMENT_TREE :
                    enforceTree(uri);
                    return getDocumentType(android.provider.DocumentsContract.getDocumentId(uri));
                default :
                    return null;
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Log.w(android.provider.DocumentsProvider.TAG, "Failed during getType", e);
            return null;
        }
    }

    /**
     * Implementation is provided by the parent class. Can be overridden to
     * provide additional functionality, but subclasses <em>must</em> always
     * call the superclass. If the superclass returns {@code null}, the subclass
     * may implement custom behavior.
     * <p>
     * This is typically used to resolve a subtree URI into a concrete document
     * reference, issuing a narrower single-document URI permission grant along
     * the way.
     *
     * @see DocumentsContract#buildDocumentUriUsingTree(Uri, String)
     */
    @android.annotation.CallSuper
    @java.lang.Override
    public android.net.Uri canonicalize(android.net.Uri uri) {
        final android.content.Context context = getContext();
        switch (mMatcher.match(uri)) {
            case android.provider.DocumentsProvider.MATCH_DOCUMENT_TREE :
                enforceTree(uri);
                final android.net.Uri narrowUri = android.provider.DocumentsContract.buildDocumentUri(uri.getAuthority(), android.provider.DocumentsContract.getDocumentId(uri));
                // Caller may only have prefix grant, so extend them a grant to
                // the narrow URI.
                final int modeFlags = android.provider.DocumentsProvider.getCallingOrSelfUriPermissionModeFlags(context, uri);
                context.grantUriPermission(getCallingPackage(), narrowUri, modeFlags);
                return narrowUri;
        }
        return null;
    }

    private static int getCallingOrSelfUriPermissionModeFlags(android.content.Context context, android.net.Uri uri) {
        // TODO: move this to a direct AMS call
        int modeFlags = 0;
        if (context.checkCallingOrSelfUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            modeFlags |= android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
        }
        if (context.checkCallingOrSelfUriPermission(uri, android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            modeFlags |= android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        if (context.checkCallingOrSelfUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION | android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            modeFlags |= android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
        }
        return modeFlags;
    }

    /**
     * Implementation is provided by the parent class. Throws by default, and
     * cannot be overriden.
     *
     * @see #createDocument(String, String, String)
     */
    @java.lang.Override
    public final android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
        throw new java.lang.UnsupportedOperationException("Insert not supported");
    }

    /**
     * Implementation is provided by the parent class. Throws by default, and
     * cannot be overriden.
     *
     * @see #deleteDocument(String)
     */
    @java.lang.Override
    public final int delete(android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
        throw new java.lang.UnsupportedOperationException("Delete not supported");
    }

    /**
     * Implementation is provided by the parent class. Throws by default, and
     * cannot be overriden.
     */
    @java.lang.Override
    public final int update(android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
        throw new java.lang.UnsupportedOperationException("Update not supported");
    }

    /**
     * Implementation is provided by the parent class. Can be overridden to
     * provide additional functionality, but subclasses <em>must</em> always
     * call the superclass. If the superclass returns {@code null}, the subclass
     * may implement custom behavior.
     */
    @android.annotation.CallSuper
    @java.lang.Override
    public android.os.Bundle call(java.lang.String method, java.lang.String arg, android.os.Bundle extras) {
        if (!method.startsWith("android:")) {
            // Ignore non-platform methods
            return super.call(method, arg, extras);
        }
        try {
            return callUnchecked(method, arg, extras);
        } catch (java.io.FileNotFoundException e) {
            throw new java.lang.IllegalStateException("Failed call " + method, e);
        }
    }

    private android.os.Bundle callUnchecked(java.lang.String method, java.lang.String arg, android.os.Bundle extras) throws java.io.FileNotFoundException {
        final android.content.Context context = getContext();
        final android.net.Uri documentUri = extras.getParcelable(android.provider.DocumentsContract.EXTRA_URI);
        final java.lang.String authority = documentUri.getAuthority();
        final java.lang.String documentId = android.provider.DocumentsContract.getDocumentId(documentUri);
        if (!mAuthority.equals(authority)) {
            throw new java.lang.SecurityException((("Requested authority " + authority) + " doesn't match provider ") + mAuthority);
        }
        final android.os.Bundle out = new android.os.Bundle();
        // If the URI is a tree URI performs some validation.
        enforceTree(documentUri);
        if (android.provider.DocumentsContract.METHOD_IS_CHILD_DOCUMENT.equals(method)) {
            enforceReadPermissionInner(documentUri, getCallingPackage(), null);
            final android.net.Uri childUri = extras.getParcelable(android.provider.DocumentsContract.EXTRA_TARGET_URI);
            final java.lang.String childAuthority = childUri.getAuthority();
            final java.lang.String childId = android.provider.DocumentsContract.getDocumentId(childUri);
            out.putBoolean(android.provider.DocumentsContract.EXTRA_RESULT, mAuthority.equals(childAuthority) && isChildDocument(documentId, childId));
        } else
            if (android.provider.DocumentsContract.METHOD_CREATE_DOCUMENT.equals(method)) {
                enforceWritePermissionInner(documentUri, getCallingPackage(), null);
                final java.lang.String mimeType = extras.getString(android.provider.DocumentsContract.Document.COLUMN_MIME_TYPE);
                final java.lang.String displayName = extras.getString(android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME);
                final java.lang.String newDocumentId = createDocument(documentId, mimeType, displayName);
                // No need to issue new grants here, since caller either has
                // manage permission or a prefix grant. We might generate a
                // tree style URI if that's how they called us.
                final android.net.Uri newDocumentUri = android.provider.DocumentsContract.buildDocumentUriMaybeUsingTree(documentUri, newDocumentId);
                out.putParcelable(android.provider.DocumentsContract.EXTRA_URI, newDocumentUri);
            } else
                if (android.provider.DocumentsContract.METHOD_RENAME_DOCUMENT.equals(method)) {
                    enforceWritePermissionInner(documentUri, getCallingPackage(), null);
                    final java.lang.String displayName = extras.getString(android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME);
                    final java.lang.String newDocumentId = renameDocument(documentId, displayName);
                    if (newDocumentId != null) {
                        final android.net.Uri newDocumentUri = android.provider.DocumentsContract.buildDocumentUriMaybeUsingTree(documentUri, newDocumentId);
                        // If caller came in with a narrow grant, issue them a
                        // narrow grant for the newly renamed document.
                        if (!android.provider.DocumentsContract.isTreeUri(newDocumentUri)) {
                            final int modeFlags = android.provider.DocumentsProvider.getCallingOrSelfUriPermissionModeFlags(context, documentUri);
                            context.grantUriPermission(getCallingPackage(), newDocumentUri, modeFlags);
                        }
                        out.putParcelable(android.provider.DocumentsContract.EXTRA_URI, newDocumentUri);
                        // Original document no longer exists, clean up any grants.
                        revokeDocumentPermission(documentId);
                    }
                } else
                    if (android.provider.DocumentsContract.METHOD_DELETE_DOCUMENT.equals(method)) {
                        enforceWritePermissionInner(documentUri, getCallingPackage(), null);
                        deleteDocument(documentId);
                        // Document no longer exists, clean up any grants.
                        revokeDocumentPermission(documentId);
                    } else
                        if (android.provider.DocumentsContract.METHOD_COPY_DOCUMENT.equals(method)) {
                            final android.net.Uri targetUri = extras.getParcelable(android.provider.DocumentsContract.EXTRA_TARGET_URI);
                            final java.lang.String targetId = android.provider.DocumentsContract.getDocumentId(targetUri);
                            enforceReadPermissionInner(documentUri, getCallingPackage(), null);
                            enforceWritePermissionInner(targetUri, getCallingPackage(), null);
                            final java.lang.String newDocumentId = copyDocument(documentId, targetId);
                            if (newDocumentId != null) {
                                final android.net.Uri newDocumentUri = android.provider.DocumentsContract.buildDocumentUriMaybeUsingTree(documentUri, newDocumentId);
                                if (!android.provider.DocumentsContract.isTreeUri(newDocumentUri)) {
                                    final int modeFlags = android.provider.DocumentsProvider.getCallingOrSelfUriPermissionModeFlags(context, documentUri);
                                    context.grantUriPermission(getCallingPackage(), newDocumentUri, modeFlags);
                                }
                                out.putParcelable(android.provider.DocumentsContract.EXTRA_URI, newDocumentUri);
                            }
                        } else
                            if (android.provider.DocumentsContract.METHOD_MOVE_DOCUMENT.equals(method)) {
                                final android.net.Uri parentSourceUri = extras.getParcelable(android.provider.DocumentsContract.EXTRA_PARENT_URI);
                                final java.lang.String parentSourceId = android.provider.DocumentsContract.getDocumentId(parentSourceUri);
                                final android.net.Uri targetUri = extras.getParcelable(android.provider.DocumentsContract.EXTRA_TARGET_URI);
                                final java.lang.String targetId = android.provider.DocumentsContract.getDocumentId(targetUri);
                                enforceWritePermissionInner(documentUri, getCallingPackage(), null);
                                enforceReadPermissionInner(parentSourceUri, getCallingPackage(), null);
                                enforceWritePermissionInner(targetUri, getCallingPackage(), null);
                                final java.lang.String newDocumentId = moveDocument(documentId, parentSourceId, targetId);
                                if (newDocumentId != null) {
                                    final android.net.Uri newDocumentUri = android.provider.DocumentsContract.buildDocumentUriMaybeUsingTree(documentUri, newDocumentId);
                                    if (!android.provider.DocumentsContract.isTreeUri(newDocumentUri)) {
                                        final int modeFlags = android.provider.DocumentsProvider.getCallingOrSelfUriPermissionModeFlags(context, documentUri);
                                        context.grantUriPermission(getCallingPackage(), newDocumentUri, modeFlags);
                                    }
                                    out.putParcelable(android.provider.DocumentsContract.EXTRA_URI, newDocumentUri);
                                }
                            } else
                                if (android.provider.DocumentsContract.METHOD_REMOVE_DOCUMENT.equals(method)) {
                                    final android.net.Uri parentSourceUri = extras.getParcelable(android.provider.DocumentsContract.EXTRA_PARENT_URI);
                                    final java.lang.String parentSourceId = android.provider.DocumentsContract.getDocumentId(parentSourceUri);
                                    enforceReadPermissionInner(parentSourceUri, getCallingPackage(), null);
                                    enforceWritePermissionInner(documentUri, getCallingPackage(), null);
                                    removeDocument(documentId, parentSourceId);
                                    // It's responsibility of the provider to revoke any grants, as the document may be
                                    // still attached to another parents.
                                } else {
                                    throw new java.lang.UnsupportedOperationException("Method not supported " + method);
                                }






        return out;
    }

    /**
     * Revoke any active permission grants for the given
     * {@link Document#COLUMN_DOCUMENT_ID}, usually called when a document
     * becomes invalid. Follows the same semantics as
     * {@link Context#revokeUriPermission(Uri, int)}.
     */
    public final void revokeDocumentPermission(java.lang.String documentId) {
        final android.content.Context context = getContext();
        context.revokeUriPermission(android.provider.DocumentsContract.buildDocumentUri(mAuthority, documentId), ~0);
        context.revokeUriPermission(android.provider.DocumentsContract.buildTreeDocumentUri(mAuthority, documentId), ~0);
    }

    /**
     * Implementation is provided by the parent class. Cannot be overriden.
     *
     * @see #openDocument(String, String, CancellationSignal)
     */
    @java.lang.Override
    public final android.os.ParcelFileDescriptor openFile(android.net.Uri uri, java.lang.String mode) throws java.io.FileNotFoundException {
        enforceTree(uri);
        return openDocument(android.provider.DocumentsContract.getDocumentId(uri), mode, null);
    }

    /**
     * Implementation is provided by the parent class. Cannot be overriden.
     *
     * @see #openDocument(String, String, CancellationSignal)
     */
    @java.lang.Override
    public final android.os.ParcelFileDescriptor openFile(android.net.Uri uri, java.lang.String mode, android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        enforceTree(uri);
        return openDocument(android.provider.DocumentsContract.getDocumentId(uri), mode, signal);
    }

    /**
     * Implementation is provided by the parent class. Cannot be overriden.
     *
     * @see #openDocument(String, String, CancellationSignal)
     */
    @java.lang.Override
    @java.lang.SuppressWarnings("resource")
    public final android.content.res.AssetFileDescriptor openAssetFile(android.net.Uri uri, java.lang.String mode) throws java.io.FileNotFoundException {
        enforceTree(uri);
        final android.os.ParcelFileDescriptor fd = openDocument(android.provider.DocumentsContract.getDocumentId(uri), mode, null);
        return fd != null ? new android.content.res.AssetFileDescriptor(fd, 0, -1) : null;
    }

    /**
     * Implementation is provided by the parent class. Cannot be overriden.
     *
     * @see #openDocument(String, String, CancellationSignal)
     */
    @java.lang.Override
    @java.lang.SuppressWarnings("resource")
    public final android.content.res.AssetFileDescriptor openAssetFile(android.net.Uri uri, java.lang.String mode, android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        enforceTree(uri);
        final android.os.ParcelFileDescriptor fd = openDocument(android.provider.DocumentsContract.getDocumentId(uri), mode, signal);
        return fd != null ? new android.content.res.AssetFileDescriptor(fd, 0, -1) : null;
    }

    /**
     * Implementation is provided by the parent class. Cannot be overriden.
     *
     * @see #openDocumentThumbnail(String, Point, CancellationSignal)
     * @see #openTypedDocument(String, String, Bundle, CancellationSignal)
     * @see #getDocumentStreamTypes(String, String)
     */
    @java.lang.Override
    public final android.content.res.AssetFileDescriptor openTypedAssetFile(android.net.Uri uri, java.lang.String mimeTypeFilter, android.os.Bundle opts) throws java.io.FileNotFoundException {
        return openTypedAssetFileImpl(uri, mimeTypeFilter, opts, null);
    }

    /**
     * Implementation is provided by the parent class. Cannot be overriden.
     *
     * @see #openDocumentThumbnail(String, Point, CancellationSignal)
     * @see #openTypedDocument(String, String, Bundle, CancellationSignal)
     * @see #getDocumentStreamTypes(String, String)
     */
    @java.lang.Override
    public final android.content.res.AssetFileDescriptor openTypedAssetFile(android.net.Uri uri, java.lang.String mimeTypeFilter, android.os.Bundle opts, android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        return openTypedAssetFileImpl(uri, mimeTypeFilter, opts, signal);
    }

    /**
     * Return a list of streamable MIME types matching the filter, which can be passed to
     * {@link #openTypedDocument(String, String, Bundle, CancellationSignal)}.
     *
     * <p>The default implementation returns a MIME type provided by
     * {@link #queryDocument(String, String[])} as long as it matches the filter and the document
     * does not have the {@link Document#FLAG_VIRTUAL_DOCUMENT} flag set.
     *
     * @see #getStreamTypes(Uri, String)
     * @see #openTypedDocument(String, String, Bundle, CancellationSignal)
     */
    public java.lang.String[] getDocumentStreamTypes(java.lang.String documentId, java.lang.String mimeTypeFilter) {
        android.database.Cursor cursor = null;
        try {
            cursor = queryDocument(documentId, null);
            if (cursor.moveToFirst()) {
                final java.lang.String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.DocumentsContract.Document.COLUMN_MIME_TYPE));
                final long flags = cursor.getLong(cursor.getColumnIndexOrThrow(android.provider.DocumentsContract.Document.COLUMN_FLAGS));
                if ((((flags & android.provider.DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) == 0) && (mimeType != null)) && android.provider.DocumentsProvider.mimeTypeMatches(mimeTypeFilter, mimeType)) {
                    return new java.lang.String[]{ mimeType };
                }
            }
        } catch (java.io.FileNotFoundException e) {
            return null;
        } finally {
            libcore.io.IoUtils.closeQuietly(cursor);
        }
        // No streamable MIME types.
        return null;
    }

    /**
     * Called by a client to determine the types of data streams that this content provider
     * support for the given URI.
     *
     * <p>Overriding this method is deprecated. Override {@link #openTypedDocument} instead.
     *
     * @see #getDocumentStreamTypes(String, String)
     */
    @java.lang.Override
    public java.lang.String[] getStreamTypes(android.net.Uri uri, java.lang.String mimeTypeFilter) {
        enforceTree(uri);
        return getDocumentStreamTypes(android.provider.DocumentsContract.getDocumentId(uri), mimeTypeFilter);
    }

    /**
     *
     *
     * @unknown 
     */
    private final android.content.res.AssetFileDescriptor openTypedAssetFileImpl(android.net.Uri uri, java.lang.String mimeTypeFilter, android.os.Bundle opts, android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        enforceTree(uri);
        final java.lang.String documentId = android.provider.DocumentsContract.getDocumentId(uri);
        if ((opts != null) && opts.containsKey(android.content.ContentResolver.EXTRA_SIZE)) {
            final android.graphics.Point sizeHint = opts.getParcelable(android.content.ContentResolver.EXTRA_SIZE);
            return openDocumentThumbnail(documentId, sizeHint, signal);
        }
        if ("*/*".equals(mimeTypeFilter)) {
            // If they can take anything, the untyped open call is good enough.
            return openAssetFile(uri, "r");
        }
        final java.lang.String baseType = getType(uri);
        if ((baseType != null) && android.content.ClipDescription.compareMimeTypes(baseType, mimeTypeFilter)) {
            // Use old untyped open call if this provider has a type for this
            // URI and it matches the request.
            return openAssetFile(uri, "r");
        }
        // For any other yet unhandled case, let the provider subclass handle it.
        return openTypedDocument(documentId, mimeTypeFilter, opts, signal);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean mimeTypeMatches(java.lang.String filter, java.lang.String test) {
        if (test == null) {
            return false;
        } else
            if ((filter == null) || "*/*".equals(filter)) {
                return true;
            } else
                if (filter.equals(test)) {
                    return true;
                } else
                    if (filter.endsWith("/*")) {
                        return filter.regionMatches(0, test, 0, filter.indexOf('/'));
                    } else {
                        return false;
                    }



    }
}

