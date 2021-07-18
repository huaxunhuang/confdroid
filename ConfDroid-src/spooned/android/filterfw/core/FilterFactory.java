/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *            http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class FilterFactory {
    private static android.filterfw.core.FilterFactory mSharedFactory;

    private java.util.HashSet<java.lang.String> mPackages = new java.util.HashSet<java.lang.String>();

    private static java.lang.ClassLoader mCurrentClassLoader;

    private static java.util.HashSet<java.lang.String> mLibraries;

    private static java.lang.Object mClassLoaderGuard;

    static {
        android.filterfw.core.FilterFactory.mCurrentClassLoader = java.lang.Thread.currentThread().getContextClassLoader();
        android.filterfw.core.FilterFactory.mLibraries = new java.util.HashSet<java.lang.String>();
        android.filterfw.core.FilterFactory.mClassLoaderGuard = new java.lang.Object();
    }

    private static final java.lang.String TAG = "FilterFactory";

    private static boolean mLogVerbose = android.util.Log.isLoggable(android.filterfw.core.FilterFactory.TAG, android.util.Log.VERBOSE);

    public static android.filterfw.core.FilterFactory sharedFactory() {
        if (android.filterfw.core.FilterFactory.mSharedFactory == null) {
            android.filterfw.core.FilterFactory.mSharedFactory = new android.filterfw.core.FilterFactory();
        }
        return android.filterfw.core.FilterFactory.mSharedFactory;
    }

    /**
     * Adds a new Java library to the list to be scanned for filters.
     * libraryPath must be an absolute path of the jar file.  This needs to be
     * static because only one classloader per process can open a shared native
     * library, which a filter may well have.
     */
    public static void addFilterLibrary(java.lang.String libraryPath) {
        if (android.filterfw.core.FilterFactory.mLogVerbose)
            android.util.Log.v(android.filterfw.core.FilterFactory.TAG, "Adding filter library " + libraryPath);

        synchronized(android.filterfw.core.FilterFactory.mClassLoaderGuard) {
            if (android.filterfw.core.FilterFactory.mLibraries.contains(libraryPath)) {
                if (android.filterfw.core.FilterFactory.mLogVerbose)
                    android.util.Log.v(android.filterfw.core.FilterFactory.TAG, "Library already added");

                return;
            }
            android.filterfw.core.FilterFactory.mLibraries.add(libraryPath);
            // Chain another path loader to the current chain
            android.filterfw.core.FilterFactory.mCurrentClassLoader = new dalvik.system.PathClassLoader(libraryPath, android.filterfw.core.FilterFactory.mCurrentClassLoader);
        }
    }

    public void addPackage(java.lang.String packageName) {
        if (android.filterfw.core.FilterFactory.mLogVerbose)
            android.util.Log.v(android.filterfw.core.FilterFactory.TAG, "Adding package " + packageName);

        /* TODO: This should use a getPackage call in the caller's context, but no such method exists.
        Package pkg = Package.getPackage(packageName);
        if (pkg == null) {
        throw new IllegalArgumentException("Unknown filter package '" + packageName + "'!");
        }
         */
        mPackages.add(packageName);
    }

    public android.filterfw.core.Filter createFilterByClassName(java.lang.String className, java.lang.String filterName) {
        if (android.filterfw.core.FilterFactory.mLogVerbose)
            android.util.Log.v(android.filterfw.core.FilterFactory.TAG, "Looking up class " + className);

        java.lang.Class filterClass = null;
        // Look for the class in the imported packages
        for (java.lang.String packageName : mPackages) {
            try {
                if (android.filterfw.core.FilterFactory.mLogVerbose)
                    android.util.Log.v(android.filterfw.core.FilterFactory.TAG, (("Trying " + packageName) + ".") + className);

                synchronized(android.filterfw.core.FilterFactory.mClassLoaderGuard) {
                    filterClass = android.filterfw.core.FilterFactory.mCurrentClassLoader.loadClass((packageName + ".") + className);
                }
            } catch (java.lang.ClassNotFoundException e) {
                continue;
            }
            // Exit loop if class was found.
            if (filterClass != null) {
                break;
            }
        }
        if (filterClass == null) {
            throw new java.lang.IllegalArgumentException(("Unknown filter class '" + className) + "'!");
        }
        return createFilterByClass(filterClass, filterName);
    }

    public android.filterfw.core.Filter createFilterByClass(java.lang.Class filterClass, java.lang.String filterName) {
        // Make sure this is a Filter subclass
        try {
            filterClass.asSubclass(android.filterfw.core.Filter.class);
        } catch (java.lang.ClassCastException e) {
            throw new java.lang.IllegalArgumentException(("Attempting to allocate class '" + filterClass) + "' which is not a subclass of Filter!");
        }
        // Look for the correct constructor
        java.lang.reflect.Constructor filterConstructor = null;
        try {
            filterConstructor = filterClass.getConstructor(java.lang.String.class);
        } catch (java.lang.NoSuchMethodException e) {
            throw new java.lang.IllegalArgumentException(("The filter class '" + filterClass) + "' does not have a constructor of the form <init>(String name)!");
        }
        // Construct the filter
        android.filterfw.core.Filter filter = null;
        try {
            filter = ((android.filterfw.core.Filter) (filterConstructor.newInstance(filterName)));
        } catch (java.lang.Throwable t) {
            // Condition checked below
        }
        if (filter == null) {
            throw new java.lang.IllegalArgumentException(("Could not construct the filter '" + filterName) + "'!");
        }
        return filter;
    }
}

