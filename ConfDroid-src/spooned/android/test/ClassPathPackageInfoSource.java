/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.test;


/**
 * Generate {@link ClassPathPackageInfo}s by scanning apk paths.
 *
 * {@hide } Not needed for 1.0 SDK.
 */
@java.lang.Deprecated
public class ClassPathPackageInfoSource {
    private static final java.lang.String CLASS_EXTENSION = ".class";

    private static final java.lang.ClassLoader CLASS_LOADER = android.test.ClassPathPackageInfoSource.class.getClassLoader();

    private final android.test.SimpleCache<java.lang.String, android.test.ClassPathPackageInfo> cache = new android.test.SimpleCache<java.lang.String, android.test.ClassPathPackageInfo>() {
        @java.lang.Override
        protected android.test.ClassPathPackageInfo load(java.lang.String pkgName) {
            return createPackageInfo(pkgName);
        }
    };

    // The class path of the running application
    private final java.lang.String[] classPath;

    private static java.lang.String[] apkPaths;

    // A cache of jar file contents
    private final java.util.Map<java.io.File, java.util.Set<java.lang.String>> jarFiles = com.google.android.collect.Maps.newHashMap();

    private java.lang.ClassLoader classLoader;

    ClassPathPackageInfoSource() {
        classPath = android.test.ClassPathPackageInfoSource.getClassPath();
    }

    public static void setApkPaths(java.lang.String[] apkPaths) {
        android.test.ClassPathPackageInfoSource.apkPaths = apkPaths;
    }

    public android.test.ClassPathPackageInfo getPackageInfo(java.lang.String pkgName) {
        return cache.get(pkgName);
    }

    private android.test.ClassPathPackageInfo createPackageInfo(java.lang.String packageName) {
        java.util.Set<java.lang.String> subpackageNames = new java.util.TreeSet<java.lang.String>();
        java.util.Set<java.lang.String> classNames = new java.util.TreeSet<java.lang.String>();
        java.util.Set<java.lang.Class<?>> topLevelClasses = com.google.android.collect.Sets.newHashSet();
        findClasses(packageName, classNames, subpackageNames);
        for (java.lang.String className : classNames) {
            if (className.endsWith(".R") || className.endsWith(".Manifest")) {
                // Don't try to load classes that are generated. They usually aren't in test apks.
                continue;
            }
            try {
                // We get errors in the emulator if we don't use the caller's class loader.
                topLevelClasses.add(java.lang.Class.forName(className, false, classLoader != null ? classLoader : android.test.ClassPathPackageInfoSource.CLASS_LOADER));
            } catch (java.lang.ClassNotFoundException | java.lang.NoClassDefFoundError e) {
                // Should not happen unless there is a generated class that is not included in
                // the .apk.
                android.util.Log.w("ClassPathPackageInfoSource", ((("Cannot load class. " + "Make sure it is in your apk. Class name: '") + className) + "'. Message: ") + e.getMessage(), e);
            }
        }
        return new android.test.ClassPathPackageInfo(this, packageName, subpackageNames, topLevelClasses);
    }

    /**
     * Finds all classes and sub packages that are below the packageName and
     * add them to the respective sets. Searches the package on the whole class
     * path.
     */
    private void findClasses(java.lang.String packageName, java.util.Set<java.lang.String> classNames, java.util.Set<java.lang.String> subpackageNames) {
        java.lang.String packagePrefix = packageName + '.';
        java.lang.String pathPrefix = packagePrefix.replace('.', '/');
        for (java.lang.String entryName : classPath) {
            java.io.File classPathEntry = new java.io.File(entryName);
            // Forge may not have brought over every item in the classpath. Be
            // polite and ignore missing entries.
            if (classPathEntry.exists()) {
                try {
                    if (entryName.endsWith(".apk")) {
                        findClassesInApk(entryName, packageName, classNames, subpackageNames);
                    } else {
                        // scan the directories that contain apk files.
                        for (java.lang.String apkPath : android.test.ClassPathPackageInfoSource.apkPaths) {
                            java.io.File file = new java.io.File(apkPath);
                            scanForApkFiles(file, packageName, classNames, subpackageNames);
                        }
                    }
                } catch (java.io.IOException e) {
                    throw new java.lang.AssertionError((("Can't read classpath entry " + entryName) + ": ") + e.getMessage());
                }
            }
        }
    }

    private void scanForApkFiles(java.io.File source, java.lang.String packageName, java.util.Set<java.lang.String> classNames, java.util.Set<java.lang.String> subpackageNames) throws java.io.IOException {
        if (source.getPath().endsWith(".apk")) {
            findClassesInApk(source.getPath(), packageName, classNames, subpackageNames);
        } else {
            java.io.File[] files = source.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    scanForApkFiles(file, packageName, classNames, subpackageNames);
                }
            }
        }
    }

    /**
     * Finds all classes and sub packages that are below the packageName and
     * add them to the respective sets. Searches the package in a class directory.
     */
    private void findClassesInDirectory(java.io.File classDir, java.lang.String packagePrefix, java.lang.String pathPrefix, java.util.Set<java.lang.String> classNames, java.util.Set<java.lang.String> subpackageNames) throws java.io.IOException {
        java.io.File directory = new java.io.File(classDir, pathPrefix);
        if (directory.exists()) {
            for (java.io.File f : directory.listFiles()) {
                java.lang.String name = f.getName();
                if (name.endsWith(android.test.ClassPathPackageInfoSource.CLASS_EXTENSION) && android.test.ClassPathPackageInfoSource.isToplevelClass(name)) {
                    classNames.add(packagePrefix + android.test.ClassPathPackageInfoSource.getClassName(name));
                } else
                    if (f.isDirectory()) {
                        subpackageNames.add(packagePrefix + name);
                    }

            }
        }
    }

    /**
     * Finds all classes and sub packages that are below the packageName and
     * add them to the respective sets. Searches the package in a single jar file.
     */
    private void findClassesInJar(java.io.File jarFile, java.lang.String pathPrefix, java.util.Set<java.lang.String> classNames, java.util.Set<java.lang.String> subpackageNames) throws java.io.IOException {
        java.util.Set<java.lang.String> entryNames = getJarEntries(jarFile);
        // check if the Jar contains the package.
        if (!entryNames.contains(pathPrefix)) {
            return;
        }
        int prefixLength = pathPrefix.length();
        for (java.lang.String entryName : entryNames) {
            if (entryName.startsWith(pathPrefix)) {
                if (entryName.endsWith(android.test.ClassPathPackageInfoSource.CLASS_EXTENSION)) {
                    // check if the class is in the package itself or in one of its
                    // subpackages.
                    int index = entryName.indexOf('/', prefixLength);
                    if (index >= 0) {
                        java.lang.String p = entryName.substring(0, index).replace('/', '.');
                        subpackageNames.add(p);
                    } else
                        if (android.test.ClassPathPackageInfoSource.isToplevelClass(entryName)) {
                            classNames.add(android.test.ClassPathPackageInfoSource.getClassName(entryName).replace('/', '.'));
                        }

                }
            }
        }
    }

    /**
     * Finds all classes and sub packages that are below the packageName and
     * add them to the respective sets. Searches the package in a single apk file.
     */
    private void findClassesInApk(java.lang.String apkPath, java.lang.String packageName, java.util.Set<java.lang.String> classNames, java.util.Set<java.lang.String> subpackageNames) throws java.io.IOException {
        dalvik.system.DexFile dexFile = null;
        try {
            dexFile = new dalvik.system.DexFile(apkPath);
            java.util.Enumeration<java.lang.String> apkClassNames = dexFile.entries();
            while (apkClassNames.hasMoreElements()) {
                java.lang.String className = apkClassNames.nextElement();
                if (className.startsWith(packageName)) {
                    java.lang.String subPackageName = packageName;
                    int lastPackageSeparator = className.lastIndexOf('.');
                    if (lastPackageSeparator > 0) {
                        subPackageName = className.substring(0, lastPackageSeparator);
                    }
                    if (subPackageName.length() > packageName.length()) {
                        subpackageNames.add(subPackageName);
                    } else
                        if (android.test.ClassPathPackageInfoSource.isToplevelClass(className)) {
                            classNames.add(className);
                        }

                }
            } 
        } catch (java.io.IOException e) {
            if (false) {
                android.util.Log.w("ClassPathPackageInfoSource", "Error finding classes at apk path: " + apkPath, e);
            }
        } finally {
            if (dexFile != null) {
                // Todo: figure out why closing causes a dalvik error resulting in vm shutdown.
                // dexFile.close();
            }
        }
    }

    /**
     * Gets the class and package entries from a Jar.
     */
    private java.util.Set<java.lang.String> getJarEntries(java.io.File jarFile) throws java.io.IOException {
        java.util.Set<java.lang.String> entryNames = jarFiles.get(jarFile);
        if (entryNames == null) {
            entryNames = com.google.android.collect.Sets.newHashSet();
            java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(jarFile);
            java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                java.lang.String entryName = entries.nextElement().getName();
                if (entryName.endsWith(android.test.ClassPathPackageInfoSource.CLASS_EXTENSION)) {
                    // add the entry name of the class
                    entryNames.add(entryName);
                    // add the entry name of the classes package, i.e. the entry name of
                    // the directory that the class is in. Used to quickly skip jar files
                    // if they do not contain a certain package.
                    // 
                    // Also add parent packages so that a JAR that contains
                    // pkg1/pkg2/Foo.class will be marked as containing pkg1/ in addition
                    // to pkg1/pkg2/ and pkg1/pkg2/Foo.class.  We're still interested in
                    // JAR files that contains subpackages of a given package, even if
                    // an intermediate package contains no direct classes.
                    // 
                    // Classes in the default package will cause a single package named
                    // "" to be added instead.
                    int lastIndex = entryName.lastIndexOf('/');
                    do {
                        java.lang.String packageName = entryName.substring(0, lastIndex + 1);
                        entryNames.add(packageName);
                        lastIndex = entryName.lastIndexOf('/', lastIndex - 1);
                    } while (lastIndex > 0 );
                }
            } 
            jarFiles.put(jarFile, entryNames);
        }
        return entryNames;
    }

    /**
     * Checks if a given file name represents a toplevel class.
     */
    private static boolean isToplevelClass(java.lang.String fileName) {
        return fileName.indexOf('$') < 0;
    }

    /**
     * Given the absolute path of a class file, return the class name.
     */
    private static java.lang.String getClassName(java.lang.String className) {
        int classNameEnd = className.length() - android.test.ClassPathPackageInfoSource.CLASS_EXTENSION.length();
        return className.substring(0, classNameEnd);
    }

    /**
     * Gets the class path from the System Property "java.class.path" and splits
     * it up into the individual elements.
     */
    private static java.lang.String[] getClassPath() {
        java.lang.String classPath = java.lang.System.getProperty("java.class.path");
        java.lang.String separator = java.lang.System.getProperty("path.separator", ":");
        return classPath.split(java.util.regex.Pattern.quote(separator));
    }

    public void setClassLoader(java.lang.ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}

