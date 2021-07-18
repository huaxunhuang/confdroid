/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.webkit;


/**
 * Two-way map that maps MIME-types to file extensions and vice versa.
 *
 * <p>See also {@link java.net.URLConnection#guessContentTypeFromName}
 * and {@link java.net.URLConnection#guessContentTypeFromStream}. This
 * class and {@code URLConnection} share the same MIME-type database.
 */
public class MimeTypeMap {
    private static final android.webkit.MimeTypeMap sMimeTypeMap = new android.webkit.MimeTypeMap();

    private MimeTypeMap() {
    }

    /**
     * Returns the file extension or an empty string iff there is no
     * extension. This method is a convenience method for obtaining the
     * extension of a url and has undefined results for other Strings.
     *
     * @param url
     * 		
     * @return The file extension of the given url.
     */
    public static java.lang.String getFileExtensionFromUrl(java.lang.String url) {
        if (!android.text.TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }
            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }
            int filenamePos = url.lastIndexOf('/');
            java.lang.String filename = (0 <= filenamePos) ? url.substring(filenamePos + 1) : url;
            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if ((!filename.isEmpty()) && java.util.regex.Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }
        return "";
    }

    /**
     * Return true if the given MIME type has an entry in the map.
     *
     * @param mimeType
     * 		A MIME type (i.e. text/plain)
     * @return True iff there is a mimeType entry in the map.
     */
    public boolean hasMimeType(java.lang.String mimeType) {
        return libcore.net.MimeUtils.hasMimeType(mimeType);
    }

    /**
     * Return the MIME type for the given extension.
     *
     * @param extension
     * 		A file extension without the leading '.'
     * @return The MIME type for the given extension or null iff there is none.
     */
    public java.lang.String getMimeTypeFromExtension(java.lang.String extension) {
        return libcore.net.MimeUtils.guessMimeTypeFromExtension(extension);
    }

    // Static method called by jni.
    private static java.lang.String mimeTypeFromExtension(java.lang.String extension) {
        return libcore.net.MimeUtils.guessMimeTypeFromExtension(extension);
    }

    /**
     * Return true if the given extension has a registered MIME type.
     *
     * @param extension
     * 		A file extension without the leading '.'
     * @return True iff there is an extension entry in the map.
     */
    public boolean hasExtension(java.lang.String extension) {
        return libcore.net.MimeUtils.hasExtension(extension);
    }

    /**
     * Return the registered extension for the given MIME type. Note that some
     * MIME types map to multiple extensions. This call will return the most
     * common extension for the given MIME type.
     *
     * @param mimeType
     * 		A MIME type (i.e. text/plain)
     * @return The extension for the given MIME type or null iff there is none.
     */
    public java.lang.String getExtensionFromMimeType(java.lang.String mimeType) {
        return libcore.net.MimeUtils.guessExtensionFromMimeType(mimeType);
    }

    /**
     * If the given MIME type is null, or one of the "generic" types (text/plain
     * or application/octet-stream) map it to a type that Android can deal with.
     * If the given type is not generic, return it unchanged.
     *
     * @param mimeType
     * 		MIME type provided by the server.
     * @param url
     * 		URL of the data being loaded.
     * @param contentDisposition
     * 		Content-disposition header given by the server.
     * @return The MIME type that should be used for this data.
     */
    /* package */
    java.lang.String remapGenericMimeType(java.lang.String mimeType, java.lang.String url, java.lang.String contentDisposition) {
        // If we have one of "generic" MIME types, try to deduce
        // the right MIME type from the file extension (if any):
        if ("text/plain".equals(mimeType) || "application/octet-stream".equals(mimeType)) {
            // for attachment, use the filename in the Content-Disposition
            // to guess the mimetype
            java.lang.String filename = null;
            if (contentDisposition != null) {
                filename = android.webkit.URLUtil.parseContentDisposition(contentDisposition);
            }
            if (filename != null) {
                url = filename;
            }
            java.lang.String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(url);
            java.lang.String newMimeType = getMimeTypeFromExtension(extension);
            if (newMimeType != null) {
                mimeType = newMimeType;
            }
        } else
            if ("text/vnd.wap.wml".equals(mimeType)) {
                // As we don't support wml, render it as plain text
                mimeType = "text/plain";
            } else {
                // It seems that xhtml+xml and vnd.wap.xhtml+xml mime
                // subtypes are used interchangeably. So treat them the same.
                if ("application/vnd.wap.xhtml+xml".equals(mimeType)) {
                    mimeType = "application/xhtml+xml";
                }
            }

        return mimeType;
    }

    /**
     * Get the singleton instance of MimeTypeMap.
     *
     * @return The singleton instance of the MIME-type map.
     */
    public static android.webkit.MimeTypeMap getSingleton() {
        return android.webkit.MimeTypeMap.sMimeTypeMap;
    }
}

