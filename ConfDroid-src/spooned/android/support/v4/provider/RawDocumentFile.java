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


class RawDocumentFile extends android.support.v4.provider.DocumentFile {
    private java.io.File mFile;

    RawDocumentFile(android.support.v4.provider.DocumentFile parent, java.io.File file) {
        super(parent);
        mFile = file;
    }

    @java.lang.Override
    public android.support.v4.provider.DocumentFile createFile(java.lang.String mimeType, java.lang.String displayName) {
        // Tack on extension when valid MIME type provided
        final java.lang.String extension = android.webkit.MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        if (extension != null) {
            displayName += "." + extension;
        }
        final java.io.File target = new java.io.File(mFile, displayName);
        try {
            target.createNewFile();
            return new android.support.v4.provider.RawDocumentFile(this, target);
        } catch (java.io.IOException e) {
            android.util.Log.w(android.support.v4.provider.DocumentFile.TAG, "Failed to createFile: " + e);
            return null;
        }
    }

    @java.lang.Override
    public android.support.v4.provider.DocumentFile createDirectory(java.lang.String displayName) {
        final java.io.File target = new java.io.File(mFile, displayName);
        if (target.isDirectory() || target.mkdir()) {
            return new android.support.v4.provider.RawDocumentFile(this, target);
        } else {
            return null;
        }
    }

    @java.lang.Override
    public android.net.Uri getUri() {
        return android.net.Uri.fromFile(mFile);
    }

    @java.lang.Override
    public java.lang.String getName() {
        return mFile.getName();
    }

    @java.lang.Override
    public java.lang.String getType() {
        if (mFile.isDirectory()) {
            return null;
        } else {
            return android.support.v4.provider.RawDocumentFile.getTypeForName(mFile.getName());
        }
    }

    @java.lang.Override
    public boolean isDirectory() {
        return mFile.isDirectory();
    }

    @java.lang.Override
    public boolean isFile() {
        return mFile.isFile();
    }

    @java.lang.Override
    public long lastModified() {
        return mFile.lastModified();
    }

    @java.lang.Override
    public long length() {
        return mFile.length();
    }

    @java.lang.Override
    public boolean canRead() {
        return mFile.canRead();
    }

    @java.lang.Override
    public boolean canWrite() {
        return mFile.canWrite();
    }

    @java.lang.Override
    public boolean delete() {
        android.support.v4.provider.RawDocumentFile.deleteContents(mFile);
        return mFile.delete();
    }

    @java.lang.Override
    public boolean exists() {
        return mFile.exists();
    }

    @java.lang.Override
    public android.support.v4.provider.DocumentFile[] listFiles() {
        final java.util.ArrayList<android.support.v4.provider.DocumentFile> results = new java.util.ArrayList<android.support.v4.provider.DocumentFile>();
        final java.io.File[] files = mFile.listFiles();
        if (files != null) {
            for (java.io.File file : files) {
                results.add(new android.support.v4.provider.RawDocumentFile(this, file));
            }
        }
        return results.toArray(new android.support.v4.provider.DocumentFile[results.size()]);
    }

    @java.lang.Override
    public boolean renameTo(java.lang.String displayName) {
        final java.io.File target = new java.io.File(mFile.getParentFile(), displayName);
        if (mFile.renameTo(target)) {
            mFile = target;
            return true;
        } else {
            return false;
        }
    }

    private static java.lang.String getTypeForName(java.lang.String name) {
        final int lastDot = name.lastIndexOf('.');
        if (lastDot >= 0) {
            final java.lang.String extension = name.substring(lastDot + 1).toLowerCase();
            final java.lang.String mime = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mime != null) {
                return mime;
            }
        }
        return "application/octet-stream";
    }

    private static boolean deleteContents(java.io.File dir) {
        java.io.File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (java.io.File file : files) {
                if (file.isDirectory()) {
                    success &= android.support.v4.provider.RawDocumentFile.deleteContents(file);
                }
                if (!file.delete()) {
                    android.util.Log.w(android.support.v4.provider.DocumentFile.TAG, "Failed to delete " + file);
                    success = false;
                }
            }
        }
        return success;
    }
}

