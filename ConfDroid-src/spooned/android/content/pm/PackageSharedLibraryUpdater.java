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
 * Base for classes that update a {@link PackageParser.Package}'s shared libraries.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting
public abstract class PackageSharedLibraryUpdater {
    /**
     * Update the package's shared libraries.
     *
     * @param pkg
     * 		the package to update.
     */
    public abstract void updatePackage(android.content.pm.PackageParser.Package pkg);

    static void removeLibrary(android.content.pm.PackageParser.Package pkg, java.lang.String libraryName) {
        pkg.usesLibraries = com.android.internal.util.ArrayUtils.remove(pkg.usesLibraries, libraryName);
        pkg.usesOptionalLibraries = com.android.internal.util.ArrayUtils.remove(pkg.usesOptionalLibraries, libraryName);
    }

    @android.annotation.NonNull
    static <T> java.util.ArrayList<T> prefix(@android.annotation.Nullable
    java.util.ArrayList<T> cur, T val) {
        if (cur == null) {
            cur = new java.util.ArrayList<>();
        }
        cur.add(0, val);
        return cur;
    }

    private static boolean isLibraryPresent(java.util.ArrayList<java.lang.String> usesLibraries, java.util.ArrayList<java.lang.String> usesOptionalLibraries, java.lang.String apacheHttpLegacy) {
        return com.android.internal.util.ArrayUtils.contains(usesLibraries, apacheHttpLegacy) || com.android.internal.util.ArrayUtils.contains(usesOptionalLibraries, apacheHttpLegacy);
    }

    /**
     * Add an implicit dependency.
     *
     * <p>If the package has an existing dependency on {@code existingLibrary} then prefix it with
     * the {@code implicitDependency} if it is not already in the list of libraries.
     *
     * @param pkg
     * 		the {@link PackageParser.Package} to update.
     * @param existingLibrary
     * 		the existing library.
     * @param implicitDependency
     * 		the implicit dependency to add
     */
    void prefixImplicitDependency(android.content.pm.PackageParser.Package pkg, java.lang.String existingLibrary, java.lang.String implicitDependency) {
        java.util.ArrayList<java.lang.String> usesLibraries = pkg.usesLibraries;
        java.util.ArrayList<java.lang.String> usesOptionalLibraries = pkg.usesOptionalLibraries;
        if (!android.content.pm.PackageSharedLibraryUpdater.isLibraryPresent(usesLibraries, usesOptionalLibraries, implicitDependency)) {
            if (com.android.internal.util.ArrayUtils.contains(usesLibraries, existingLibrary)) {
                android.content.pm.PackageSharedLibraryUpdater.prefix(usesLibraries, implicitDependency);
            } else
                if (com.android.internal.util.ArrayUtils.contains(usesOptionalLibraries, existingLibrary)) {
                    android.content.pm.PackageSharedLibraryUpdater.prefix(usesOptionalLibraries, implicitDependency);
                }

            pkg.usesLibraries = usesLibraries;
            pkg.usesOptionalLibraries = usesOptionalLibraries;
        }
    }

    void prefixRequiredLibrary(android.content.pm.PackageParser.Package pkg, java.lang.String libraryName) {
        java.util.ArrayList<java.lang.String> usesLibraries = pkg.usesLibraries;
        java.util.ArrayList<java.lang.String> usesOptionalLibraries = pkg.usesOptionalLibraries;
        boolean alreadyPresent = android.content.pm.PackageSharedLibraryUpdater.isLibraryPresent(usesLibraries, usesOptionalLibraries, libraryName);
        if (!alreadyPresent) {
            usesLibraries = android.content.pm.PackageSharedLibraryUpdater.prefix(usesLibraries, libraryName);
            pkg.usesLibraries = usesLibraries;
        }
    }
}

