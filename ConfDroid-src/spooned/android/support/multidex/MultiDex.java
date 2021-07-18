/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.multidex;


/**
 * MultiDex patches {@link Context#getClassLoader() the application context class
 * loader} in order to load classes from more than one dex file. The primary
 * {@code classes.dex} must contain the classes necessary for calling this
 * class methods. Secondary dex files named classes2.dex, classes3.dex... found
 * in the application apk will be added to the classloader after first call to
 * {@link #install(Context)}.
 *
 * <p/>
 * This library provides compatibility for platforms with API level 4 through 20. This library does
 * nothing on newer versions of the platform which provide built-in support for secondary dex files.
 */
public final class MultiDex {
    static final java.lang.String TAG = "MultiDex";

    private static final java.lang.String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";

    private static final java.lang.String CODE_CACHE_NAME = "code_cache";

    private static final java.lang.String CODE_CACHE_SECONDARY_FOLDER_NAME = "secondary-dexes";

    private static final int MAX_SUPPORTED_SDK_VERSION = 20;

    private static final int MIN_SDK_VERSION = 4;

    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;

    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;

    private static final java.util.Set<java.lang.String> installedApk = new java.util.HashSet<java.lang.String>();

    private static final boolean IS_VM_MULTIDEX_CAPABLE = android.support.multidex.MultiDex.isVMMultidexCapable(java.lang.System.getProperty("java.vm.version"));

    private MultiDex() {
    }

    /**
     * Patches the application context class loader by appending extra dex files
     * loaded from the application apk. This method should be called in the
     * attachBaseContext of your {@link Application}, see
     * {@link MultiDexApplication} for more explanation and an example.
     *
     * @param context
     * 		application context.
     * @throws RuntimeException
     * 		if an error occurred preventing the classloader
     * 		extension.
     */
    public static void install(android.content.Context context) {
        android.util.Log.i(android.support.multidex.MultiDex.TAG, "install");
        if (android.support.multidex.MultiDex.IS_VM_MULTIDEX_CAPABLE) {
            android.util.Log.i(android.support.multidex.MultiDex.TAG, "VM has multidex support, MultiDex support library is disabled.");
            return;
        }
        if (android.os.Build.VERSION.SDK_INT < android.support.multidex.MultiDex.MIN_SDK_VERSION) {
            throw new java.lang.RuntimeException(((("Multi dex installation failed. SDK " + android.os.Build.VERSION.SDK_INT) + " is unsupported. Min SDK version is ") + android.support.multidex.MultiDex.MIN_SDK_VERSION) + ".");
        }
        try {
            android.content.pm.ApplicationInfo applicationInfo = android.support.multidex.MultiDex.getApplicationInfo(context);
            if (applicationInfo == null) {
                // Looks like running on a test Context, so just return without patching.
                return;
            }
            synchronized(android.support.multidex.MultiDex.installedApk) {
                java.lang.String apkPath = applicationInfo.sourceDir;
                if (android.support.multidex.MultiDex.installedApk.contains(apkPath)) {
                    return;
                }
                android.support.multidex.MultiDex.installedApk.add(apkPath);
                if (android.os.Build.VERSION.SDK_INT > android.support.multidex.MultiDex.MAX_SUPPORTED_SDK_VERSION) {
                    android.util.Log.w(android.support.multidex.MultiDex.TAG, ((((((("MultiDex is not guaranteed to work in SDK version " + android.os.Build.VERSION.SDK_INT) + ": SDK version higher than ") + android.support.multidex.MultiDex.MAX_SUPPORTED_SDK_VERSION) + " should be backed by ") + "runtime with built-in multidex capabilty but it's not the ") + "case here: java.vm.version=\"") + java.lang.System.getProperty("java.vm.version")) + "\"");
                }
                /* The patched class loader is expected to be a descendant of
                dalvik.system.BaseDexClassLoader. We modify its
                dalvik.system.DexPathList pathList field to append additional DEX
                file entries.
                 */
                java.lang.ClassLoader loader;
                try {
                    loader = context.getClassLoader();
                } catch (java.lang.RuntimeException e) {
                    /* Ignore those exceptions so that we don't break tests relying on Context like
                    a android.test.mock.MockContext or a android.content.ContextWrapper with a
                    null base Context.
                     */
                    android.util.Log.w(android.support.multidex.MultiDex.TAG, "Failure while trying to obtain Context class loader. " + "Must be running in test mode. Skip patching.", e);
                    return;
                }
                if (loader == null) {
                    // Note, the context class loader is null when running Robolectric tests.
                    android.util.Log.e(android.support.multidex.MultiDex.TAG, "Context class loader is null. Must be running in test mode. " + "Skip patching.");
                    return;
                }
                try {
                    android.support.multidex.MultiDex.clearOldDexDir(context);
                } catch (java.lang.Throwable t) {
                    android.util.Log.w(android.support.multidex.MultiDex.TAG, "Something went wrong when trying to clear old MultiDex extraction, " + "continuing without cleaning.", t);
                }
                java.io.File dexDir = android.support.multidex.MultiDex.getDexDir(context, applicationInfo);
                java.util.List<java.io.File> files = android.support.multidex.MultiDexExtractor.load(context, applicationInfo, dexDir, false);
                if (android.support.multidex.MultiDex.checkValidZipFiles(files)) {
                    android.support.multidex.MultiDex.installSecondaryDexes(loader, dexDir, files);
                } else {
                    android.util.Log.w(android.support.multidex.MultiDex.TAG, "Files were not valid zip files.  Forcing a reload.");
                    // Try again, but this time force a reload of the zip file.
                    files = android.support.multidex.MultiDexExtractor.load(context, applicationInfo, dexDir, true);
                    if (android.support.multidex.MultiDex.checkValidZipFiles(files)) {
                        android.support.multidex.MultiDex.installSecondaryDexes(loader, dexDir, files);
                    } else {
                        // Second time didn't work, give up
                        throw new java.lang.RuntimeException("Zip files were not valid.");
                    }
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.support.multidex.MultiDex.TAG, "Multidex installation failure", e);
            throw new java.lang.RuntimeException(("Multi dex installation failed (" + e.getMessage()) + ").");
        }
        android.util.Log.i(android.support.multidex.MultiDex.TAG, "install done");
    }

    private static android.content.pm.ApplicationInfo getApplicationInfo(android.content.Context context) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.PackageManager pm;
        java.lang.String packageName;
        try {
            pm = context.getPackageManager();
            packageName = context.getPackageName();
        } catch (java.lang.RuntimeException e) {
            /* Ignore those exceptions so that we don't break tests relying on Context like
            a android.test.mock.MockContext or a android.content.ContextWrapper with a null
            base Context.
             */
            android.util.Log.w(android.support.multidex.MultiDex.TAG, "Failure while trying to obtain ApplicationInfo from Context. " + "Must be running in test mode. Skip patching.", e);
            return null;
        }
        if ((pm == null) || (packageName == null)) {
            // This is most likely a mock context, so just return without patching.
            return null;
        }
        android.content.pm.ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, android.content.pm.PackageManager.GET_META_DATA);
        return applicationInfo;
    }

    /**
     * Identifies if the current VM has a native support for multidex, meaning there is no need for
     * additional installation by this library.
     *
     * @return true if the VM handles multidex
     */
    /* package visible for test */
    static boolean isVMMultidexCapable(java.lang.String versionString) {
        boolean isMultidexCapable = false;
        if (versionString != null) {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
            if (matcher.matches()) {
                try {
                    int major = java.lang.Integer.parseInt(matcher.group(1));
                    int minor = java.lang.Integer.parseInt(matcher.group(2));
                    isMultidexCapable = (major > android.support.multidex.MultiDex.VM_WITH_MULTIDEX_VERSION_MAJOR) || ((major == android.support.multidex.MultiDex.VM_WITH_MULTIDEX_VERSION_MAJOR) && (minor >= android.support.multidex.MultiDex.VM_WITH_MULTIDEX_VERSION_MINOR));
                } catch (java.lang.NumberFormatException e) {
                    // let isMultidexCapable be false
                }
            }
        }
        android.util.Log.i(android.support.multidex.MultiDex.TAG, ("VM with version " + versionString) + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
        return isMultidexCapable;
    }

    private static void installSecondaryDexes(java.lang.ClassLoader loader, java.io.File dexDir, java.util.List<java.io.File> files) throws java.io.IOException, java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.NoSuchFieldException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        if (!files.isEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                android.support.multidex.MultiDex.V19.install(loader, files, dexDir);
            } else
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    android.support.multidex.MultiDex.V14.install(loader, files, dexDir);
                } else {
                    android.support.multidex.MultiDex.V4.install(loader, files);
                }

        }
    }

    /**
     * Returns whether all files in the list are valid zip files.  If {@code files} is empty, then
     * returns true.
     */
    private static boolean checkValidZipFiles(java.util.List<java.io.File> files) {
        for (java.io.File file : files) {
            if (!android.support.multidex.MultiDexExtractor.verifyZipFile(file)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Locates a given field anywhere in the class inheritance hierarchy.
     *
     * @param instance
     * 		an object to search the field into.
     * @param name
     * 		field name
     * @return a field object
     * @throws NoSuchFieldException
     * 		if the field cannot be located
     */
    private static java.lang.reflect.Field findField(java.lang.Object instance, java.lang.String name) throws java.lang.NoSuchFieldException {
        for (java.lang.Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                java.lang.reflect.Field field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (java.lang.NoSuchFieldException e) {
                // ignore and search next
            }
        }
        throw new java.lang.NoSuchFieldException((("Field " + name) + " not found in ") + instance.getClass());
    }

    /**
     * Locates a given method anywhere in the class inheritance hierarchy.
     *
     * @param instance
     * 		an object to search the method into.
     * @param name
     * 		method name
     * @param parameterTypes
     * 		method parameter types
     * @return a method object
     * @throws NoSuchMethodException
     * 		if the method cannot be located
     */
    private static java.lang.reflect.Method findMethod(java.lang.Object instance, java.lang.String name, java.lang.Class<?>... parameterTypes) throws java.lang.NoSuchMethodException {
        for (java.lang.Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                java.lang.reflect.Method method = clazz.getDeclaredMethod(name, parameterTypes);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            } catch (java.lang.NoSuchMethodException e) {
                // ignore and search next
            }
        }
        throw new java.lang.NoSuchMethodException((((("Method " + name) + " with parameters ") + java.util.Arrays.asList(parameterTypes)) + " not found in ") + instance.getClass());
    }

    /**
     * Replace the value of a field containing a non null array, by a new array containing the
     * elements of the original array plus the elements of extraElements.
     *
     * @param instance
     * 		the instance whose field is to be modified.
     * @param fieldName
     * 		the field to modify.
     * @param extraElements
     * 		elements to append at the end of the array.
     */
    private static void expandFieldArray(java.lang.Object instance, java.lang.String fieldName, java.lang.Object[] extraElements) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.NoSuchFieldException {
        java.lang.reflect.Field jlrField = android.support.multidex.MultiDex.findField(instance, fieldName);
        java.lang.Object[] original = ((java.lang.Object[]) (jlrField.get(instance)));
        java.lang.Object[] combined = ((java.lang.Object[]) (java.lang.reflect.Array.newInstance(original.getClass().getComponentType(), original.length + extraElements.length)));
        java.lang.System.arraycopy(original, 0, combined, 0, original.length);
        java.lang.System.arraycopy(extraElements, 0, combined, original.length, extraElements.length);
        jlrField.set(instance, combined);
    }

    private static void clearOldDexDir(android.content.Context context) throws java.lang.Exception {
        java.io.File dexDir = new java.io.File(context.getFilesDir(), android.support.multidex.MultiDex.OLD_SECONDARY_FOLDER_NAME);
        if (dexDir.isDirectory()) {
            android.util.Log.i(android.support.multidex.MultiDex.TAG, ("Clearing old secondary dex dir (" + dexDir.getPath()) + ").");
            java.io.File[] files = dexDir.listFiles();
            if (files == null) {
                android.util.Log.w(android.support.multidex.MultiDex.TAG, ("Failed to list secondary dex dir content (" + dexDir.getPath()) + ").");
                return;
            }
            for (java.io.File oldFile : files) {
                android.util.Log.i(android.support.multidex.MultiDex.TAG, (("Trying to delete old file " + oldFile.getPath()) + " of size ") + oldFile.length());
                if (!oldFile.delete()) {
                    android.util.Log.w(android.support.multidex.MultiDex.TAG, "Failed to delete old file " + oldFile.getPath());
                } else {
                    android.util.Log.i(android.support.multidex.MultiDex.TAG, "Deleted old file " + oldFile.getPath());
                }
            }
            if (!dexDir.delete()) {
                android.util.Log.w(android.support.multidex.MultiDex.TAG, "Failed to delete secondary dex dir " + dexDir.getPath());
            } else {
                android.util.Log.i(android.support.multidex.MultiDex.TAG, "Deleted old secondary dex dir " + dexDir.getPath());
            }
        }
    }

    private static java.io.File getDexDir(android.content.Context context, android.content.pm.ApplicationInfo applicationInfo) throws java.io.IOException {
        java.io.File cache = new java.io.File(applicationInfo.dataDir, android.support.multidex.MultiDex.CODE_CACHE_NAME);
        try {
            android.support.multidex.MultiDex.mkdirChecked(cache);
        } catch (java.io.IOException e) {
            /* If we can't emulate code_cache, then store to filesDir. This means abandoning useless
            files on disk if the device ever updates to android 5+. But since this seems to
            happen only on some devices running android 2, this should cause no pollution.
             */
            cache = new java.io.File(context.getFilesDir(), android.support.multidex.MultiDex.CODE_CACHE_NAME);
            android.support.multidex.MultiDex.mkdirChecked(cache);
        }
        java.io.File dexDir = new java.io.File(cache, android.support.multidex.MultiDex.CODE_CACHE_SECONDARY_FOLDER_NAME);
        android.support.multidex.MultiDex.mkdirChecked(dexDir);
        return dexDir;
    }

    private static void mkdirChecked(java.io.File dir) throws java.io.IOException {
        dir.mkdir();
        if (!dir.isDirectory()) {
            java.io.File parent = dir.getParentFile();
            if (parent == null) {
                android.util.Log.e(android.support.multidex.MultiDex.TAG, ("Failed to create dir " + dir.getPath()) + ". Parent file is null.");
            } else {
                android.util.Log.e(android.support.multidex.MultiDex.TAG, (((((((((("Failed to create dir " + dir.getPath()) + ". parent file is a dir ") + parent.isDirectory()) + ", a file ") + parent.isFile()) + ", exists ") + parent.exists()) + ", readable ") + parent.canRead()) + ", writable ") + parent.canWrite());
            }
            throw new java.io.IOException("Failed to create directory " + dir.getPath());
        }
    }

    /**
     * Installer for platform versions 19.
     */
    private static final class V19 {
        private static void install(java.lang.ClassLoader loader, java.util.List<java.io.File> additionalClassPathEntries, java.io.File optimizedDirectory) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.NoSuchFieldException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
            /* The patched class loader is expected to be a descendant of
            dalvik.system.BaseDexClassLoader. We modify its
            dalvik.system.DexPathList pathList field to append additional DEX
            file entries.
             */
            java.lang.reflect.Field pathListField = android.support.multidex.MultiDex.findField(loader, "pathList");
            java.lang.Object dexPathList = pathListField.get(loader);
            java.util.ArrayList<java.io.IOException> suppressedExceptions = new java.util.ArrayList<java.io.IOException>();
            android.support.multidex.MultiDex.expandFieldArray(dexPathList, "dexElements", android.support.multidex.MultiDex.V19.makeDexElements(dexPathList, new java.util.ArrayList<java.io.File>(additionalClassPathEntries), optimizedDirectory, suppressedExceptions));
            if (suppressedExceptions.size() > 0) {
                for (java.io.IOException e : suppressedExceptions) {
                    android.util.Log.w(android.support.multidex.MultiDex.TAG, "Exception in makeDexElement", e);
                }
                java.lang.reflect.Field suppressedExceptionsField = android.support.multidex.MultiDex.findField(dexPathList, "dexElementsSuppressedExceptions");
                java.io.IOException[] dexElementsSuppressedExceptions = ((java.io.IOException[]) (suppressedExceptionsField.get(dexPathList)));
                if (dexElementsSuppressedExceptions == null) {
                    dexElementsSuppressedExceptions = suppressedExceptions.toArray(new java.io.IOException[suppressedExceptions.size()]);
                } else {
                    java.io.IOException[] combined = new java.io.IOException[suppressedExceptions.size() + dexElementsSuppressedExceptions.length];
                    suppressedExceptions.toArray(combined);
                    java.lang.System.arraycopy(dexElementsSuppressedExceptions, 0, combined, suppressedExceptions.size(), dexElementsSuppressedExceptions.length);
                    dexElementsSuppressedExceptions = combined;
                }
                suppressedExceptionsField.set(dexPathList, dexElementsSuppressedExceptions);
            }
        }

        /**
         * A wrapper around
         * {@code private static final dalvik.system.DexPathList#makeDexElements}.
         */
        private static java.lang.Object[] makeDexElements(java.lang.Object dexPathList, java.util.ArrayList<java.io.File> files, java.io.File optimizedDirectory, java.util.ArrayList<java.io.IOException> suppressedExceptions) throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
            java.lang.reflect.Method makeDexElements = android.support.multidex.MultiDex.findMethod(dexPathList, "makeDexElements", java.util.ArrayList.class, java.io.File.class, java.util.ArrayList.class);
            return ((java.lang.Object[]) (makeDexElements.invoke(dexPathList, files, optimizedDirectory, suppressedExceptions)));
        }
    }

    /**
     * Installer for platform versions 14, 15, 16, 17 and 18.
     */
    private static final class V14 {
        private static void install(java.lang.ClassLoader loader, java.util.List<java.io.File> additionalClassPathEntries, java.io.File optimizedDirectory) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.NoSuchFieldException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
            /* The patched class loader is expected to be a descendant of
            dalvik.system.BaseDexClassLoader. We modify its
            dalvik.system.DexPathList pathList field to append additional DEX
            file entries.
             */
            java.lang.reflect.Field pathListField = android.support.multidex.MultiDex.findField(loader, "pathList");
            java.lang.Object dexPathList = pathListField.get(loader);
            android.support.multidex.MultiDex.expandFieldArray(dexPathList, "dexElements", android.support.multidex.MultiDex.V14.makeDexElements(dexPathList, new java.util.ArrayList<java.io.File>(additionalClassPathEntries), optimizedDirectory));
        }

        /**
         * A wrapper around
         * {@code private static final dalvik.system.DexPathList#makeDexElements}.
         */
        private static java.lang.Object[] makeDexElements(java.lang.Object dexPathList, java.util.ArrayList<java.io.File> files, java.io.File optimizedDirectory) throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
            java.lang.reflect.Method makeDexElements = android.support.multidex.MultiDex.findMethod(dexPathList, "makeDexElements", java.util.ArrayList.class, java.io.File.class);
            return ((java.lang.Object[]) (makeDexElements.invoke(dexPathList, files, optimizedDirectory)));
        }
    }

    /**
     * Installer for platform versions 4 to 13.
     */
    private static final class V4 {
        private static void install(java.lang.ClassLoader loader, java.util.List<java.io.File> additionalClassPathEntries) throws java.io.IOException, java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.NoSuchFieldException {
            /* The patched class loader is expected to be a descendant of
            dalvik.system.DexClassLoader. We modify its
            fields mPaths, mFiles, mZips and mDexs to append additional DEX
            file entries.
             */
            int extraSize = additionalClassPathEntries.size();
            java.lang.reflect.Field pathField = android.support.multidex.MultiDex.findField(loader, "path");
            java.lang.StringBuilder path = new java.lang.StringBuilder(((java.lang.String) (pathField.get(loader))));
            java.lang.String[] extraPaths = new java.lang.String[extraSize];
            java.io.File[] extraFiles = new java.io.File[extraSize];
            java.util.zip.ZipFile[] extraZips = new java.util.zip.ZipFile[extraSize];
            dalvik.system.DexFile[] extraDexs = new dalvik.system.DexFile[extraSize];
            for (java.util.ListIterator<java.io.File> iterator = additionalClassPathEntries.listIterator(); iterator.hasNext();) {
                java.io.File additionalEntry = iterator.next();
                java.lang.String entryPath = additionalEntry.getAbsolutePath();
                path.append(':').append(entryPath);
                int index = iterator.previousIndex();
                extraPaths[index] = entryPath;
                extraFiles[index] = additionalEntry;
                extraZips[index] = new java.util.zip.ZipFile(additionalEntry);
                extraDexs[index] = dalvik.system.DexFile.loadDex(entryPath, entryPath + ".dex", 0);
            }
            pathField.set(loader, path.toString());
            android.support.multidex.MultiDex.expandFieldArray(loader, "mPaths", extraPaths);
            android.support.multidex.MultiDex.expandFieldArray(loader, "mFiles", extraFiles);
            android.support.multidex.MultiDex.expandFieldArray(loader, "mZips", extraZips);
            android.support.multidex.MultiDex.expandFieldArray(loader, "mDexs", extraDexs);
        }
    }
}

