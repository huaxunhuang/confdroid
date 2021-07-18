/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.app;


class ApplicationLoaders {
    public static android.app.ApplicationLoaders getDefault() {
        return android.app.ApplicationLoaders.gApplicationLoaders;
    }

    public java.lang.ClassLoader getClassLoader(java.lang.String zip, int targetSdkVersion, boolean isBundled, java.lang.String librarySearchPath, java.lang.String libraryPermittedPath, java.lang.ClassLoader parent) {
        /* This is the parent we use if they pass "null" in.  In theory
        this should be the "system" class loader; in practice we
        don't use that and can happily (and more efficiently) use the
        bootstrap class loader.
         */
        java.lang.ClassLoader baseParent = java.lang.ClassLoader.getSystemClassLoader().getParent();
        synchronized(mLoaders) {
            if (parent == null) {
                parent = baseParent;
            }
            /* If we're one step up from the base class loader, find
            something in our cache.  Otherwise, we create a whole
            new ClassLoader for the zip archive.
             */
            if (parent == baseParent) {
                java.lang.ClassLoader loader = mLoaders.get(zip);
                if (loader != null) {
                    return loader;
                }
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, zip);
                dalvik.system.PathClassLoader pathClassloader = com.android.internal.os.PathClassLoaderFactory.createClassLoader(zip, librarySearchPath, libraryPermittedPath, parent, targetSdkVersion, isBundled);
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "setupVulkanLayerPath");
                android.app.ApplicationLoaders.setupVulkanLayerPath(pathClassloader, librarySearchPath);
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                mLoaders.put(zip, pathClassloader);
                return pathClassloader;
            }
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, zip);
            dalvik.system.PathClassLoader pathClassloader = new dalvik.system.PathClassLoader(zip, parent);
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
            return pathClassloader;
        }
    }

    private static native void setupVulkanLayerPath(java.lang.ClassLoader classLoader, java.lang.String librarySearchPath);

    /**
     * Adds a new path the classpath of the given loader.
     *
     * @throws IllegalStateException
     * 		if the provided class loader is not a {@link PathClassLoader}.
     */
    void addPath(java.lang.ClassLoader classLoader, java.lang.String dexPath) {
        if (!(classLoader instanceof dalvik.system.PathClassLoader)) {
            throw new java.lang.IllegalStateException("class loader is not a PathClassLoader");
        }
        final dalvik.system.PathClassLoader baseDexClassLoader = ((dalvik.system.PathClassLoader) (classLoader));
        baseDexClassLoader.addDexPath(dexPath);
    }

    private final android.util.ArrayMap<java.lang.String, java.lang.ClassLoader> mLoaders = new android.util.ArrayMap<java.lang.String, java.lang.ClassLoader>();

    private static final android.app.ApplicationLoaders gApplicationLoaders = new android.app.ApplicationLoaders();
}

