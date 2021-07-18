/**
 * Copyright (C) 2017 The Android Open Source Project
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
 * Modifies {@link Package} in order to maintain backwards compatibility.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting
public class PackageBackwardCompatibility extends android.content.pm.PackageSharedLibraryUpdater {
    private static final java.lang.String TAG = android.content.pm.PackageBackwardCompatibility.class.getSimpleName();

    private static final android.content.pm.PackageBackwardCompatibility INSTANCE;

    static {
        final java.util.List<android.content.pm.PackageSharedLibraryUpdater> packageUpdaters = new java.util.ArrayList<>();
        // Automatically add the org.apache.http.legacy library to the app classpath if the app
        // targets < P.
        packageUpdaters.add(new android.content.pm.OrgApacheHttpLegacyUpdater());
        packageUpdaters.add(new android.content.pm.AndroidHidlUpdater());
        // Add this before adding AndroidTestBaseUpdater so that android.test.base comes before
        // android.test.mock.
        packageUpdaters.add(new android.content.pm.PackageBackwardCompatibility.AndroidTestRunnerSplitUpdater());
        // Attempt to load and add the optional updater that will only be available when
        // REMOVE_ATB_FROM_BCP=true. If that could not be found then add the default updater that
        // will remove any references to org.apache.http.library from the package so that it does
        // not try and load the library when it is on the bootclasspath.
        boolean bootClassPathContainsATB = !android.content.pm.PackageBackwardCompatibility.addOptionalUpdater(packageUpdaters, "android.content.pm.AndroidTestBaseUpdater", android.content.pm.PackageBackwardCompatibility.RemoveUnnecessaryAndroidTestBaseLibrary::new);
        android.content.pm.PackageSharedLibraryUpdater[] updaterArray = packageUpdaters.toArray(new android.content.pm.PackageSharedLibraryUpdater[0]);
        INSTANCE = new android.content.pm.PackageBackwardCompatibility(bootClassPathContainsATB, updaterArray);
    }

    /**
     * Add an optional {@link PackageSharedLibraryUpdater} instance to the list, if it could not be
     * found then add a default instance instead.
     *
     * @param packageUpdaters
     * 		the list to update.
     * @param className
     * 		the name of the optional class.
     * @param defaultUpdater
     * 		the supplier of the default instance.
     * @return true if the optional updater was added false otherwise.
     */
    private static boolean addOptionalUpdater(java.util.List<android.content.pm.PackageSharedLibraryUpdater> packageUpdaters, java.lang.String className, java.util.function.Supplier<android.content.pm.PackageSharedLibraryUpdater> defaultUpdater) {
        java.lang.Class<? extends android.content.pm.PackageSharedLibraryUpdater> clazz;
        try {
            clazz = android.content.pm.PackageBackwardCompatibility.class.getClassLoader().loadClass(className).asSubclass(android.content.pm.PackageSharedLibraryUpdater.class);
            android.util.Log.i(android.content.pm.PackageBackwardCompatibility.TAG, "Loaded " + className);
        } catch (java.lang.ClassNotFoundException e) {
            android.util.Log.i(android.content.pm.PackageBackwardCompatibility.TAG, ("Could not find " + className) + ", ignoring");
            clazz = null;
        }
        boolean usedOptional = false;
        android.content.pm.PackageSharedLibraryUpdater updater;
        if (clazz == null) {
            updater = defaultUpdater.get();
        } else {
            try {
                updater = clazz.getConstructor().newInstance();
                usedOptional = true;
            } catch (java.lang.ReflectiveOperationException e) {
                throw new java.lang.IllegalStateException("Could not create instance of " + className, e);
            }
        }
        packageUpdaters.add(updater);
        return usedOptional;
    }

    @com.android.internal.annotations.VisibleForTesting
    public static android.content.pm.PackageSharedLibraryUpdater getInstance() {
        return android.content.pm.PackageBackwardCompatibility.INSTANCE;
    }

    private final boolean mBootClassPathContainsATB;

    private final android.content.pm.PackageSharedLibraryUpdater[] mPackageUpdaters;

    private PackageBackwardCompatibility(boolean bootClassPathContainsATB, android.content.pm.PackageSharedLibraryUpdater[] packageUpdaters) {
        this.mBootClassPathContainsATB = bootClassPathContainsATB;
        this.mPackageUpdaters = packageUpdaters;
    }

    /**
     * Modify the shared libraries in the supplied {@link Package} to maintain backwards
     * compatibility.
     *
     * @param pkg
     * 		the {@link Package} to modify.
     */
    @com.android.internal.annotations.VisibleForTesting
    public static void modifySharedLibraries(android.content.pm.PackageParser.Package pkg) {
        android.content.pm.PackageBackwardCompatibility.INSTANCE.updatePackage(pkg);
    }

    @java.lang.Override
    public void updatePackage(android.content.pm.PackageParser.Package pkg) {
        for (android.content.pm.PackageSharedLibraryUpdater packageUpdater : mPackageUpdaters) {
            packageUpdater.updatePackage(pkg);
        }
    }

    /**
     * True if the android.test.base is on the bootclasspath, false otherwise.
     */
    @com.android.internal.annotations.VisibleForTesting
    public static boolean bootClassPathContainsATB() {
        return android.content.pm.PackageBackwardCompatibility.INSTANCE.mBootClassPathContainsATB;
    }

    /**
     * Add android.test.mock dependency for any APK that depends on android.test.runner.
     *
     * <p>This is needed to maintain backwards compatibility as in previous versions of Android the
     * android.test.runner library included the classes from android.test.mock which have since
     * been split out into a separate library.
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static class AndroidTestRunnerSplitUpdater extends android.content.pm.PackageSharedLibraryUpdater {
        @java.lang.Override
        public void updatePackage(android.content.pm.PackageParser.Package pkg) {
            // android.test.runner has a dependency on android.test.mock so if android.test.runner
            // is present but android.test.mock is not then add android.test.mock.
            prefixImplicitDependency(pkg, android.content.pm.SharedLibraryNames.ANDROID_TEST_RUNNER, android.content.pm.SharedLibraryNames.ANDROID_TEST_MOCK);
        }
    }

    /**
     * Remove any usages of org.apache.http.legacy from the shared library as the library is on the
     * bootclasspath.
     */
    @com.android.internal.annotations.VisibleForTesting
    public static class RemoveUnnecessaryOrgApacheHttpLegacyLibrary extends android.content.pm.PackageSharedLibraryUpdater {
        @java.lang.Override
        public void updatePackage(android.content.pm.PackageParser.Package pkg) {
            android.content.pm.PackageSharedLibraryUpdater.removeLibrary(pkg, android.content.pm.SharedLibraryNames.ORG_APACHE_HTTP_LEGACY);
        }
    }

    /**
     * Remove any usages of android.test.base from the shared library as the library is on the
     * bootclasspath.
     */
    @com.android.internal.annotations.VisibleForTesting
    public static class RemoveUnnecessaryAndroidTestBaseLibrary extends android.content.pm.PackageSharedLibraryUpdater {
        @java.lang.Override
        public void updatePackage(android.content.pm.PackageParser.Package pkg) {
            android.content.pm.PackageSharedLibraryUpdater.removeLibrary(pkg, android.content.pm.SharedLibraryNames.ANDROID_TEST_BASE);
        }
    }
}

