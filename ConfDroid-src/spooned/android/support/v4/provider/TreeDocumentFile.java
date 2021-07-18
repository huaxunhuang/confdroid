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


class TreeDocumentFile extends android.support.v4.provider.DocumentFile {
    private android.content.Context mContext;

    private android.net.Uri mUri;

    TreeDocumentFile(android.support.v4.provider.DocumentFile parent, android.content.Context context, android.net.Uri uri) {
        super(parent);
        mContext = context;
        mUri = uri;
    }

    @java.lang.Override
    public android.support.v4.provider.DocumentFile createFile(java.lang.String mimeType, java.lang.String displayName) {
        final android.net.Uri result = android.support.v4.provider.DocumentsContractApi21.createFile(mContext, mUri, mimeType, displayName);
        return result != null ? new android.support.v4.provider.TreeDocumentFile(this, mContext, result) : null;
    }

    @java.lang.Override
    public android.support.v4.provider.DocumentFile createDirectory(java.lang.String displayName) {
        final android.net.Uri result = android.support.v4.provider.DocumentsContractApi21.createDirectory(mContext, mUri, displayName);
        return result != null ? new android.support.v4.provider.TreeDocumentFile(this, mContext, result) : null;
    }

    @java.lang.Override
    public android.net.Uri getUri() {
        return mUri;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return android.support.v4.provider.DocumentsContractApi19.getName(mContext, mUri);
    }

    @java.lang.Override
    public java.lang.String getType() {
        return android.support.v4.provider.DocumentsContractApi19.getType(mContext, mUri);
    }

    @java.lang.Override
    public boolean isDirectory() {
        return android.support.v4.provider.DocumentsContractApi19.isDirectory(mContext, mUri);
    }

    @java.lang.Override
    public boolean isFile() {
        return android.support.v4.provider.DocumentsContractApi19.isFile(mContext, mUri);
    }

    @java.lang.Override
    public long lastModified() {
        return android.support.v4.provider.DocumentsContractApi19.lastModified(mContext, mUri);
    }

    @java.lang.Override
    public long length() {
        return android.support.v4.provider.DocumentsContractApi19.length(mContext, mUri);
    }

    @java.lang.Override
    public boolean canRead() {
        return android.support.v4.provider.DocumentsContractApi19.canRead(mContext, mUri);
    }

    @java.lang.Override
    public boolean canWrite() {
        return android.support.v4.provider.DocumentsContractApi19.canWrite(mContext, mUri);
    }

    @java.lang.Override
    public boolean delete() {
        return android.support.v4.provider.DocumentsContractApi19.delete(mContext, mUri);
    }

    @java.lang.Override
    public boolean exists() {
        return android.support.v4.provider.DocumentsContractApi19.exists(mContext, mUri);
    }

    @java.lang.Override
    public android.support.v4.provider.DocumentFile[] listFiles() {
        final android.net.Uri[] result = android.support.v4.provider.DocumentsContractApi21.listFiles(mContext, mUri);
        final android.support.v4.provider.DocumentFile[] resultFiles = new android.support.v4.provider.DocumentFile[result.length];
        for (int i = 0; i < result.length; i++) {
            resultFiles[i] = new android.support.v4.provider.TreeDocumentFile(this, mContext, result[i]);
        }
        return resultFiles;
    }

    @java.lang.Override
    public boolean renameTo(java.lang.String displayName) {
        final android.net.Uri result = android.support.v4.provider.DocumentsContractApi21.renameTo(mContext, mUri, displayName);
        if (result != null) {
            mUri = result;
            return true;
        } else {
            return false;
        }
    }
}

