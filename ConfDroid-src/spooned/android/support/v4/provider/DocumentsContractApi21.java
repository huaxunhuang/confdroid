/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v4.provider;


class DocumentsContractApi21 {
    private static final java.lang.String TAG = "DocumentFile";

    public static android.net.Uri createFile(android.content.Context context, android.net.Uri self, java.lang.String mimeType, java.lang.String displayName) {
        return android.provider.DocumentsContract.createDocument(context.getContentResolver(), self, mimeType, displayName);
    }

    public static android.net.Uri createDirectory(android.content.Context context, android.net.Uri self, java.lang.String displayName) {
        return android.support.v4.provider.DocumentsContractApi21.createFile(context, self, android.provider.DocumentsContract.Document.MIME_TYPE_DIR, displayName);
    }

    public static android.net.Uri prepareTreeUri(android.net.Uri treeUri) {
        return android.provider.DocumentsContract.buildDocumentUriUsingTree(treeUri, android.provider.DocumentsContract.getTreeDocumentId(treeUri));
    }

    public static android.net.Uri[] listFiles(android.content.Context context, android.net.Uri self) {
        final android.content.ContentResolver resolver = context.getContentResolver();
        final android.net.Uri childrenUri = android.provider.DocumentsContract.buildChildDocumentsUriUsingTree(self, android.provider.DocumentsContract.getDocumentId(self));
        final java.util.ArrayList<android.net.Uri> results = new java.util.ArrayList<android.net.Uri>();
        android.database.Cursor c = null;
        try {
            c = resolver.query(childrenUri, new java.lang.String[]{ android.provider.DocumentsContract.Document.COLUMN_DOCUMENT_ID }, null, null, null);
            while (c.moveToNext()) {
                final java.lang.String documentId = c.getString(0);
                final android.net.Uri documentUri = android.provider.DocumentsContract.buildDocumentUriUsingTree(self, documentId);
                results.add(documentUri);
            } 
        } catch (java.lang.Exception e) {
            android.util.Log.w(android.support.v4.provider.DocumentsContractApi21.TAG, "Failed query: " + e);
        } finally {
            android.support.v4.provider.DocumentsContractApi21.closeQuietly(c);
        }
        return results.toArray(new android.net.Uri[results.size()]);
    }

    public static android.net.Uri renameTo(android.content.Context context, android.net.Uri self, java.lang.String displayName) {
        return android.provider.DocumentsContract.renameDocument(context.getContentResolver(), self, displayName);
    }

    private static void closeQuietly(java.lang.AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (java.lang.RuntimeException rethrown) {
                throw rethrown;
            } catch (java.lang.Exception ignored) {
            }
        }
    }
}

