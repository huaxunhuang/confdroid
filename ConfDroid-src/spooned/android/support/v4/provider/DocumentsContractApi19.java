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


class DocumentsContractApi19 {
    private static final java.lang.String TAG = "DocumentFile";

    public static boolean isDocumentUri(android.content.Context context, android.net.Uri self) {
        return android.provider.DocumentsContract.isDocumentUri(context, self);
    }

    public static java.lang.String getName(android.content.Context context, android.net.Uri self) {
        return android.support.v4.provider.DocumentsContractApi19.queryForString(context, self, android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME, null);
    }

    private static java.lang.String getRawType(android.content.Context context, android.net.Uri self) {
        return android.support.v4.provider.DocumentsContractApi19.queryForString(context, self, android.provider.DocumentsContract.Document.COLUMN_MIME_TYPE, null);
    }

    public static java.lang.String getType(android.content.Context context, android.net.Uri self) {
        final java.lang.String rawType = android.support.v4.provider.DocumentsContractApi19.getRawType(context, self);
        if (android.provider.DocumentsContract.Document.MIME_TYPE_DIR.equals(rawType)) {
            return null;
        } else {
            return rawType;
        }
    }

    public static boolean isDirectory(android.content.Context context, android.net.Uri self) {
        return android.provider.DocumentsContract.Document.MIME_TYPE_DIR.equals(android.support.v4.provider.DocumentsContractApi19.getRawType(context, self));
    }

    public static boolean isFile(android.content.Context context, android.net.Uri self) {
        final java.lang.String type = android.support.v4.provider.DocumentsContractApi19.getRawType(context, self);
        if (android.provider.DocumentsContract.Document.MIME_TYPE_DIR.equals(type) || android.text.TextUtils.isEmpty(type)) {
            return false;
        } else {
            return true;
        }
    }

    public static long lastModified(android.content.Context context, android.net.Uri self) {
        return android.support.v4.provider.DocumentsContractApi19.queryForLong(context, self, android.provider.DocumentsContract.Document.COLUMN_LAST_MODIFIED, 0);
    }

    public static long length(android.content.Context context, android.net.Uri self) {
        return android.support.v4.provider.DocumentsContractApi19.queryForLong(context, self, android.provider.DocumentsContract.Document.COLUMN_SIZE, 0);
    }

    public static boolean canRead(android.content.Context context, android.net.Uri self) {
        // Ignore if grant doesn't allow read
        if (context.checkCallingOrSelfUriPermission(self, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        // Ignore documents without MIME
        if (android.text.TextUtils.isEmpty(android.support.v4.provider.DocumentsContractApi19.getRawType(context, self))) {
            return false;
        }
        return true;
    }

    public static boolean canWrite(android.content.Context context, android.net.Uri self) {
        // Ignore if grant doesn't allow write
        if (context.checkCallingOrSelfUriPermission(self, android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        final java.lang.String type = android.support.v4.provider.DocumentsContractApi19.getRawType(context, self);
        final int flags = android.support.v4.provider.DocumentsContractApi19.queryForInt(context, self, android.provider.DocumentsContract.Document.COLUMN_FLAGS, 0);
        // Ignore documents without MIME
        if (android.text.TextUtils.isEmpty(type)) {
            return false;
        }
        // Deletable documents considered writable
        if ((flags & android.provider.DocumentsContract.Document.FLAG_SUPPORTS_DELETE) != 0) {
            return true;
        }
        if (android.provider.DocumentsContract.Document.MIME_TYPE_DIR.equals(type) && ((flags & android.provider.DocumentsContract.Document.FLAG_DIR_SUPPORTS_CREATE) != 0)) {
            // Directories that allow create considered writable
            return true;
        } else
            if ((!android.text.TextUtils.isEmpty(type)) && ((flags & android.provider.DocumentsContract.Document.FLAG_SUPPORTS_WRITE) != 0)) {
                // Writable normal files considered writable
                return true;
            }

        return false;
    }

    public static boolean delete(android.content.Context context, android.net.Uri self) {
        return android.provider.DocumentsContract.deleteDocument(context.getContentResolver(), self);
    }

    public static boolean exists(android.content.Context context, android.net.Uri self) {
        final android.content.ContentResolver resolver = context.getContentResolver();
        android.database.Cursor c = null;
        try {
            c = resolver.query(self, new java.lang.String[]{ android.provider.DocumentsContract.Document.COLUMN_DOCUMENT_ID }, null, null, null);
            return c.getCount() > 0;
        } catch (java.lang.Exception e) {
            android.util.Log.w(android.support.v4.provider.DocumentsContractApi19.TAG, "Failed query: " + e);
            return false;
        } finally {
            android.support.v4.provider.DocumentsContractApi19.closeQuietly(c);
        }
    }

    private static java.lang.String queryForString(android.content.Context context, android.net.Uri self, java.lang.String column, java.lang.String defaultValue) {
        final android.content.ContentResolver resolver = context.getContentResolver();
        android.database.Cursor c = null;
        try {
            c = resolver.query(self, new java.lang.String[]{ column }, null, null, null);
            if (c.moveToFirst() && (!c.isNull(0))) {
                return c.getString(0);
            } else {
                return defaultValue;
            }
        } catch (java.lang.Exception e) {
            android.util.Log.w(android.support.v4.provider.DocumentsContractApi19.TAG, "Failed query: " + e);
            return defaultValue;
        } finally {
            android.support.v4.provider.DocumentsContractApi19.closeQuietly(c);
        }
    }

    private static int queryForInt(android.content.Context context, android.net.Uri self, java.lang.String column, int defaultValue) {
        return ((int) (android.support.v4.provider.DocumentsContractApi19.queryForLong(context, self, column, defaultValue)));
    }

    private static long queryForLong(android.content.Context context, android.net.Uri self, java.lang.String column, long defaultValue) {
        final android.content.ContentResolver resolver = context.getContentResolver();
        android.database.Cursor c = null;
        try {
            c = resolver.query(self, new java.lang.String[]{ column }, null, null, null);
            if (c.moveToFirst() && (!c.isNull(0))) {
                return c.getLong(0);
            } else {
                return defaultValue;
            }
        } catch (java.lang.Exception e) {
            android.util.Log.w(android.support.v4.provider.DocumentsContractApi19.TAG, "Failed query: " + e);
            return defaultValue;
        } finally {
            android.support.v4.provider.DocumentsContractApi19.closeQuietly(c);
        }
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

