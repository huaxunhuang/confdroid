/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content;


/**
 * Provides information about the environment for a particular APEX.
 *
 * @unknown 
 */
@android.annotation.SystemApi
@android.annotation.TestApi
public class ApexEnvironment {
    private static final java.lang.String APEX_DATA = "apexdata";

    /**
     * Returns an ApexEnvironment instance for the APEX with the provided {@code apexModuleName}.
     *
     * <p>To preserve the safety and integrity of APEX modules, you must only obtain the
     * ApexEnvironment for your specific APEX, and you <em>must never</em> attempt to obtain an
     * ApexEnvironment for another APEX.  Any coordination between APEXs must be performed through
     * well-defined interfaces; attempting to directly read or write raw files belonging to another
     * APEX will violate the hermetic storage requirements placed upon each module.
     */
    @android.annotation.NonNull
    public static android.content.ApexEnvironment getApexEnvironment(@android.annotation.NonNull
    java.lang.String apexModuleName) {
        java.util.Objects.requireNonNull(apexModuleName, "apexModuleName cannot be null");
        // TODO(b/141148175): Check that apexModuleName is an actual APEX name
        return new android.content.ApexEnvironment(apexModuleName);
    }

    private final java.lang.String mApexModuleName;

    private ApexEnvironment(java.lang.String apexModuleName) {
        mApexModuleName = apexModuleName;
    }

    /**
     * Returns the data directory for the APEX in device-encrypted, non-user-specific storage.
     *
     * <p>This directory is automatically created by the system for installed APEXes, and its
     * contents will be rolled back if the APEX is rolled back.
     */
    @android.annotation.NonNull
    public java.io.File getDeviceProtectedDataDir() {
        return android.os.Environment.buildPath(android.os.Environment.getDataMiscDirectory(), android.content.ApexEnvironment.APEX_DATA, mApexModuleName);
    }

    /**
     * Returns the data directory for the APEX in device-encrypted, user-specific storage for the
     * specified {@code user}.
     *
     * <p>This directory is automatically created by the system for each user and for each installed
     * APEX, and its contents will be rolled back if the APEX is rolled back.
     */
    @android.annotation.NonNull
    public java.io.File getDeviceProtectedDataDirForUser(@android.annotation.NonNull
    android.os.UserHandle user) {
        return android.os.Environment.buildPath(android.os.Environment.getDataMiscDeDirectory(user.getIdentifier()), android.content.ApexEnvironment.APEX_DATA, mApexModuleName);
    }

    /**
     * Returns the data directory for the APEX in credential-encrypted, user-specific storage for
     * the specified {@code user}.
     *
     * <p>This directory is automatically created by the system for each user and for each installed
     * APEX, and its contents will be rolled back if the APEX is rolled back.
     */
    @android.annotation.NonNull
    public java.io.File getCredentialProtectedDataDirForUser(@android.annotation.NonNull
    android.os.UserHandle user) {
        return android.os.Environment.buildPath(android.os.Environment.getDataMiscCeDirectory(user.getIdentifier()), android.content.ApexEnvironment.APEX_DATA, mApexModuleName);
    }
}

