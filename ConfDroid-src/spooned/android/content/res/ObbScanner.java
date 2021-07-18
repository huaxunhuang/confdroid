/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.content.res;


/**
 * Class to scan Opaque Binary Blob (OBB) files. Use this to get information
 * about an OBB file for use in a program via {@link ObbInfo}.
 */
public class ObbScanner {
    // Don't allow others to instantiate this class
    private ObbScanner() {
    }

    /**
     * Scan a file for OBB information.
     *
     * @param filePath
     * 		path to the OBB file to be scanned.
     * @return ObbInfo object information corresponding to the file path
     * @throws IllegalArgumentException
     * 		if the OBB file couldn't be found
     * @throws IOException
     * 		if the OBB file couldn't be read
     */
    public static android.content.res.ObbInfo getObbInfo(java.lang.String filePath) throws java.io.IOException {
        if (filePath == null) {
            throw new java.lang.IllegalArgumentException("file path cannot be null");
        }
        final java.io.File obbFile = new java.io.File(filePath);
        if (!obbFile.exists()) {
            throw new java.lang.IllegalArgumentException("OBB file does not exist: " + filePath);
        }
        /* XXX This will fail to find the real canonical path if bind mounts are
        used, but we don't use any bind mounts right now.
         */
        final java.lang.String canonicalFilePath = obbFile.getCanonicalPath();
        android.content.res.ObbInfo obbInfo = new android.content.res.ObbInfo();
        obbInfo.filename = canonicalFilePath;
        android.content.res.ObbScanner.getObbInfo_native(canonicalFilePath, obbInfo);
        return obbInfo;
    }

    private static native void getObbInfo_native(java.lang.String filePath, android.content.res.ObbInfo obbInfo) throws java.io.IOException;
}

