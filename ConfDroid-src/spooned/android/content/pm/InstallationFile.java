/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.content.pm;


/**
 * Definition of a file in a streaming installation session.
 * You can use this class to retrieve the information of such a file, such as its name, size and
 * metadata. These file attributes will be consistent with those used in:
 * {@code PackageInstaller.Session#addFile}, when the file was first added into the session.
 *
 * WARNING: This is a system API to aid internal development.
 * Use at your own risk. It will change or be removed without warning.
 *
 * @see android.content.pm.PackageInstaller.Session#addFile
 * @unknown 
 */
@android.annotation.SystemApi
public final class InstallationFile {
    @android.annotation.NonNull
    private final android.content.pm.InstallationFileParcel mParcel;

    /**
     * Constructor, internal use only
     *
     * @unknown 
     */
    public InstallationFile(@android.content.pm.PackageInstaller.FileLocation
    int location, @android.annotation.NonNull
    java.lang.String name, long lengthBytes, @android.annotation.Nullable
    byte[] metadata, @android.annotation.Nullable
    byte[] signature) {
        mParcel = new android.content.pm.InstallationFileParcel();
        mParcel.location = location;
        mParcel.name = name;
        mParcel.size = lengthBytes;
        mParcel.metadata = metadata;
        mParcel.signature = signature;
    }

    /**
     * Installation Location of this file. Can be one of the following three locations:
     * <ul>
     *     <li>(1) {@code PackageInstaller.LOCATION_DATA_APP}</li>
     *     <li>(2) {@code PackageInstaller.LOCATION_MEDIA_OBB}</li>
     *     <li>(3) {@code PackageInstaller.LOCATION_MEDIA_DATA}</li>
     * </ul>
     *
     * @see android.content.pm.PackageInstaller
     * @return Integer that denotes the installation location of the file.
     */
    @android.content.pm.PackageInstaller.FileLocation
    public int getLocation() {
        return mParcel.location;
    }

    /**
     *
     *
     * @return Name of the file.
     */
    @android.annotation.NonNull
    public java.lang.String getName() {
        return mParcel.name;
    }

    /**
     *
     *
     * @return File size in bytes.
     */
    public long getLengthBytes() {
        return mParcel.size;
    }

    /**
     *
     *
     * @return File metadata as a byte array
     */
    @android.annotation.Nullable
    public byte[] getMetadata() {
        return mParcel.metadata;
    }

    /**
     *
     *
     * @return File signature info as a byte array
     */
    @android.annotation.Nullable
    public byte[] getSignature() {
        return mParcel.signature;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.content.pm.InstallationFileParcel getData() {
        return mParcel;
    }
}

