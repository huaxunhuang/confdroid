/**
 * Copyright (C) 2018 The Android Open Source Project
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
 * Updates a package to ensure that if it targets <= P that the android.hidl.base-V1.0-java
 * and android.hidl.manager-V1.0-java libraries are included by default.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting
public class AndroidHidlUpdater extends android.content.pm.PackageSharedLibraryUpdater {
    @java.lang.Override
    public void updatePackage(android.content.pm.PackageParser.Package pkg) {
        android.content.pm.ApplicationInfo info = pkg.applicationInfo;
        // This was the default <= P and is maintained for backwards compatibility.
        boolean isLegacy = info.targetSdkVersion <= Build.VERSION_CODES.P;
        // Only system apps use these libraries
        boolean isSystem = info.isSystemApp() || info.isUpdatedSystemApp();
        if (isLegacy && isSystem) {
            prefixRequiredLibrary(pkg, android.content.pm.SharedLibraryNames.ANDROID_HIDL_BASE);
            prefixRequiredLibrary(pkg, android.content.pm.SharedLibraryNames.ANDROID_HIDL_MANAGER);
        } else {
            android.content.pm.PackageSharedLibraryUpdater.removeLibrary(pkg, android.content.pm.SharedLibraryNames.ANDROID_HIDL_BASE);
            android.content.pm.PackageSharedLibraryUpdater.removeLibrary(pkg, android.content.pm.SharedLibraryNames.ANDROID_HIDL_MANAGER);
        }
    }
}

