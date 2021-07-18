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
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
class ParsedDocumentId {
    public final java.lang.String mArchiveId;

    public final java.lang.String mPath;

    public ParsedDocumentId(java.lang.String archiveId, java.lang.String path) {
        mArchiveId = archiveId;
        mPath = path;
    }

    public static android.support.provider.ParsedDocumentId fromDocumentId(java.lang.String documentId, char idDelimiter) {
        final int delimiterPosition = documentId.indexOf(idDelimiter);
        if (delimiterPosition == (-1)) {
            return new android.support.provider.ParsedDocumentId(documentId, null);
        } else {
            return new android.support.provider.ParsedDocumentId(documentId.substring(0, delimiterPosition), documentId.substring(delimiterPosition + 1));
        }
    }

    public static boolean hasPath(java.lang.String documentId, char idDelimiter) {
        return documentId.indexOf(idDelimiter) != (-1);
    }

    public java.lang.String toDocumentId(char idDelimiter) {
        if (mPath == null) {
            return mArchiveId;
        } else {
            return (mArchiveId + idDelimiter) + mPath;
        }
    }
}

