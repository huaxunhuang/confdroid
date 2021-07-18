/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.webkit;


/**
 * Top level factory, used creating all the main WebView implementation classes.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class WebViewFactory {
    private static final java.lang.String CHROMIUM_WEBVIEW_FACTORY = "com.android.webview.chromium.WebViewChromiumFactoryProvider";

    private static final java.lang.String NULL_WEBVIEW_FACTORY = "com.android.webview.nullwebview.NullWebViewFactoryProvider";

    private static final java.lang.String CHROMIUM_WEBVIEW_NATIVE_RELRO_32 = "/data/misc/shared_relro/libwebviewchromium32.relro";

    private static final java.lang.String CHROMIUM_WEBVIEW_NATIVE_RELRO_64 = "/data/misc/shared_relro/libwebviewchromium64.relro";

    public static final java.lang.String CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY = "persist.sys.webview.vmsize";

    private static final long CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES = (100 * 1024) * 1024;

    private static final java.lang.String LOGTAG = "WebViewFactory";

    private static final boolean DEBUG = false;

    // Cache the factory both for efficiency, and ensure any one process gets all webviews from the
    // same provider.
    private static android.webkit.WebViewFactoryProvider sProviderInstance;

    private static final java.lang.Object sProviderLock = new java.lang.Object();

    private static boolean sAddressSpaceReserved = false;

    private static android.content.pm.PackageInfo sPackageInfo;

    // Error codes for loadWebViewNativeLibraryFromPackage
    public static final int LIBLOAD_SUCCESS = 0;

    public static final int LIBLOAD_WRONG_PACKAGE_NAME = 1;

    public static final int LIBLOAD_ADDRESS_SPACE_NOT_RESERVED = 2;

    // error codes for waiting for WebView preparation
    public static final int LIBLOAD_FAILED_WAITING_FOR_RELRO = 3;

    public static final int LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES = 4;

    // native relro loading error codes
    public static final int LIBLOAD_FAILED_TO_OPEN_RELRO_FILE = 5;

    public static final int LIBLOAD_FAILED_TO_LOAD_LIBRARY = 6;

    public static final int LIBLOAD_FAILED_JNI_CALL = 7;

    // more error codes for waiting for WebView preparation
    public static final int LIBLOAD_FAILED_WAITING_FOR_WEBVIEW_REASON_UNKNOWN = 8;

    // error for namespace lookup
    public static final int LIBLOAD_FAILED_TO_FIND_NAMESPACE = 10;

    private static java.lang.String getWebViewPreparationErrorReason(int error) {
        switch (error) {
            case android.webkit.WebViewFactory.LIBLOAD_FAILED_WAITING_FOR_RELRO :
                return "Time out waiting for Relro files being created";
            case android.webkit.WebViewFactory.LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES :
                return "No WebView installed";
            case android.webkit.WebViewFactory.LIBLOAD_FAILED_WAITING_FOR_WEBVIEW_REASON_UNKNOWN :
                return "Crashed for unknown reason";
        }
        return "Unknown";
    }

    /**
     *
     *
     * @unknown 
     */
    public static class MissingWebViewPackageException extends android.util.AndroidRuntimeException {
        public MissingWebViewPackageException(java.lang.String message) {
            super(message);
        }

        public MissingWebViewPackageException(java.lang.Exception e) {
            super(e);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String getWebViewLibrary(android.content.pm.ApplicationInfo ai) {
        if (ai.metaData != null)
            return ai.metaData.getString("com.android.webview.WebViewLibrary");

        return null;
    }

    public static android.content.pm.PackageInfo getLoadedPackageInfo() {
        return android.webkit.WebViewFactory.sPackageInfo;
    }

    /**
     * Load the native library for the given package name iff that package
     * name is the same as the one providing the webview.
     */
    public static int loadWebViewNativeLibraryFromPackage(java.lang.String packageName, java.lang.ClassLoader clazzLoader) {
        android.webkit.WebViewProviderResponse response = null;
        try {
            response = android.webkit.WebViewFactory.getUpdateService().waitForAndGetProvider();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "error waiting for relro creation", e);
            return android.webkit.WebViewFactory.LIBLOAD_FAILED_WAITING_FOR_WEBVIEW_REASON_UNKNOWN;
        }
        if ((response.status != android.webkit.WebViewFactory.LIBLOAD_SUCCESS) && (response.status != android.webkit.WebViewFactory.LIBLOAD_FAILED_WAITING_FOR_RELRO)) {
            return response.status;
        }
        if (!response.packageInfo.packageName.equals(packageName)) {
            return android.webkit.WebViewFactory.LIBLOAD_WRONG_PACKAGE_NAME;
        }
        android.content.pm.PackageManager packageManager = android.app.AppGlobals.getInitialApplication().getPackageManager();
        android.content.pm.PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, android.content.pm.PackageManager.GET_META_DATA | android.content.pm.PackageManager.MATCH_DEBUG_TRIAGED_MISSING);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "Couldn't find package " + packageName);
            return android.webkit.WebViewFactory.LIBLOAD_WRONG_PACKAGE_NAME;
        }
        android.webkit.WebViewFactory.sPackageInfo = packageInfo;
        int loadNativeRet = android.webkit.WebViewFactory.loadNativeLibrary(clazzLoader);
        // If we failed waiting for relro we want to return that fact even if we successfully load
        // the relro file.
        if (loadNativeRet == android.webkit.WebViewFactory.LIBLOAD_SUCCESS)
            return response.status;

        return loadNativeRet;
    }

    static android.webkit.WebViewFactoryProvider getProvider() {
        synchronized(android.webkit.WebViewFactory.sProviderLock) {
            // For now the main purpose of this function (and the factory abstraction) is to keep
            // us honest and minimize usage of WebView internals when binding the proxy.
            if (android.webkit.WebViewFactory.sProviderInstance != null)
                return android.webkit.WebViewFactory.sProviderInstance;

            final int uid = android.os.Process.myUid();
            if ((uid == android.os.Process.ROOT_UID) || (uid == android.os.Process.SYSTEM_UID)) {
                throw new java.lang.UnsupportedOperationException("For security reasons, WebView is not allowed in privileged processes");
            }
            android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskReads();
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "WebViewFactory.getProvider()");
            try {
                java.lang.Class<android.webkit.WebViewFactoryProvider> providerClass = android.webkit.WebViewFactory.getProviderClass();
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "providerClass.newInstance()");
                try {
                    android.webkit.WebViewFactory.sProviderInstance = providerClass.getConstructor(android.webkit.WebViewDelegate.class).newInstance(new android.webkit.WebViewDelegate());
                    if (android.webkit.WebViewFactory.DEBUG)
                        android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, "Loaded provider: " + android.webkit.WebViewFactory.sProviderInstance);

                    return android.webkit.WebViewFactory.sProviderInstance;
                } catch (java.lang.Exception e) {
                    android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "error instantiating provider", e);
                    throw new android.util.AndroidRuntimeException(e);
                } finally {
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
                }
            } finally {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
                android.os.StrictMode.setThreadPolicy(oldPolicy);
            }
        }
    }

    /**
     * Returns true if the signatures match, false otherwise
     */
    private static boolean signaturesEquals(android.content.pm.Signature[] s1, android.content.pm.Signature[] s2) {
        if (s1 == null) {
            return s2 == null;
        }
        if (s2 == null)
            return false;

        android.util.ArraySet<android.content.pm.Signature> set1 = new android.util.ArraySet<>();
        for (android.content.pm.Signature signature : s1) {
            set1.add(signature);
        }
        android.util.ArraySet<android.content.pm.Signature> set2 = new android.util.ArraySet<>();
        for (android.content.pm.Signature signature : s2) {
            set2.add(signature);
        }
        return set1.equals(set2);
    }

    // Throws MissingWebViewPackageException on failure
    private static void verifyPackageInfo(android.content.pm.PackageInfo chosen, android.content.pm.PackageInfo toUse) {
        if (!chosen.packageName.equals(toUse.packageName)) {
            throw new android.webkit.WebViewFactory.MissingWebViewPackageException(((("Failed to verify WebView provider, " + "packageName mismatch, expected: ") + chosen.packageName) + " actual: ") + toUse.packageName);
        }
        if (chosen.versionCode > toUse.versionCode) {
            throw new android.webkit.WebViewFactory.MissingWebViewPackageException(((("Failed to verify WebView provider, " + "version code is lower than expected: ") + chosen.versionCode) + " actual: ") + toUse.versionCode);
        }
        if (android.webkit.WebViewFactory.getWebViewLibrary(toUse.applicationInfo) == null) {
            throw new android.webkit.WebViewFactory.MissingWebViewPackageException("Tried to load an invalid WebView provider: " + toUse.packageName);
        }
        if (!android.webkit.WebViewFactory.signaturesEquals(chosen.signatures, toUse.signatures)) {
            throw new android.webkit.WebViewFactory.MissingWebViewPackageException("Failed to verify WebView provider, " + "signature mismatch");
        }
    }

    private static android.content.Context getWebViewContextAndSetProvider() {
        android.app.Application initialApplication = android.app.AppGlobals.getInitialApplication();
        try {
            android.webkit.WebViewProviderResponse response = null;
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "WebViewUpdateService.waitForAndGetProvider()");
            try {
                response = android.webkit.WebViewFactory.getUpdateService().waitForAndGetProvider();
            } finally {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
            }
            if ((response.status != android.webkit.WebViewFactory.LIBLOAD_SUCCESS) && (response.status != android.webkit.WebViewFactory.LIBLOAD_FAILED_WAITING_FOR_RELRO)) {
                throw new android.webkit.WebViewFactory.MissingWebViewPackageException("Failed to load WebView provider: " + android.webkit.WebViewFactory.getWebViewPreparationErrorReason(response.status));
            }
            // Register to be killed before fetching package info - so that we will be
            // killed if the package info goes out-of-date.
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "ActivityManager.addPackageDependency()");
            try {
                android.app.ActivityManagerNative.getDefault().addPackageDependency(response.packageInfo.packageName);
            } finally {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
            }
            // Fetch package info and verify it against the chosen package
            android.content.pm.PackageInfo newPackageInfo = null;
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "PackageManager.getPackageInfo()");
            try {
                newPackageInfo = initialApplication.getPackageManager().getPackageInfo(response.packageInfo.packageName, (((android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES | android.content.pm.PackageManager.MATCH_DEBUG_TRIAGED_MISSING) | // Make sure that we fetch the current provider even if its not
                // installed for the current user
                android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES) | // Fetch signatures for verification
                android.content.pm.PackageManager.GET_SIGNATURES) | // Get meta-data for meta data flag verification
                android.content.pm.PackageManager.GET_META_DATA);
            } finally {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
            }
            // Validate the newly fetched package info, throws MissingWebViewPackageException on
            // failure
            android.webkit.WebViewFactory.verifyPackageInfo(response.packageInfo, newPackageInfo);
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "initialApplication.createApplicationContext");
            try {
                // Construct an app context to load the Java code into the current app.
                android.content.Context webViewContext = initialApplication.createApplicationContext(newPackageInfo.applicationInfo, android.content.Context.CONTEXT_INCLUDE_CODE | android.content.Context.CONTEXT_IGNORE_SECURITY);
                android.webkit.WebViewFactory.sPackageInfo = newPackageInfo;
                return webViewContext;
            } finally {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
            }
        } catch (android.os.RemoteException | android.content.pm.PackageManager.NameNotFoundException e) {
            throw new android.webkit.WebViewFactory.MissingWebViewPackageException("Failed to load WebView provider: " + e);
        }
    }

    private static java.lang.Class<android.webkit.WebViewFactoryProvider> getProviderClass() {
        android.content.Context webViewContext = null;
        android.app.Application initialApplication = android.app.AppGlobals.getInitialApplication();
        try {
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "WebViewFactory.getWebViewContextAndSetProvider()");
            try {
                webViewContext = android.webkit.WebViewFactory.getWebViewContextAndSetProvider();
            } finally {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
            }
            android.util.Log.i(android.webkit.WebViewFactory.LOGTAG, ((((("Loading " + android.webkit.WebViewFactory.sPackageInfo.packageName) + " version ") + android.webkit.WebViewFactory.sPackageInfo.versionName) + " (code ") + android.webkit.WebViewFactory.sPackageInfo.versionCode) + ")");
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "WebViewFactory.getChromiumProviderClass()");
            try {
                initialApplication.getAssets().addAssetPathAsSharedLibrary(webViewContext.getApplicationInfo().sourceDir);
                java.lang.ClassLoader clazzLoader = webViewContext.getClassLoader();
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "WebViewFactory.loadNativeLibrary()");
                android.webkit.WebViewFactory.loadNativeLibrary(clazzLoader);
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_WEBVIEW, "Class.forName()");
                try {
                    return ((java.lang.Class<android.webkit.WebViewFactoryProvider>) (java.lang.Class.forName(android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_FACTORY, true, clazzLoader)));
                } finally {
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
                }
            } catch (java.lang.ClassNotFoundException e) {
                android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "error loading provider", e);
                throw new android.util.AndroidRuntimeException(e);
            } finally {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_WEBVIEW);
            }
        } catch (android.webkit.WebViewFactory.MissingWebViewPackageException e) {
            // If the package doesn't exist, then try loading the null WebView instead.
            // If that succeeds, then this is a device without WebView support; if it fails then
            // swallow the failure, complain that the real WebView is missing and rethrow the
            // original exception.
            try {
                return ((java.lang.Class<android.webkit.WebViewFactoryProvider>) (java.lang.Class.forName(android.webkit.WebViewFactory.NULL_WEBVIEW_FACTORY)));
            } catch (java.lang.ClassNotFoundException e2) {
                // Ignore.
            }
            android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "Chromium WebView package does not exist", e);
            throw new android.util.AndroidRuntimeException(e);
        }
    }

    /**
     * Perform any WebView loading preparations that must happen in the zygote.
     * Currently, this means allocating address space to load the real JNI library later.
     */
    public static void prepareWebViewInZygote() {
        try {
            java.lang.System.loadLibrary("webviewchromium_loader");
            long addressSpaceToReserve = android.os.SystemProperties.getLong(android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY, android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES);
            android.webkit.WebViewFactory.sAddressSpaceReserved = android.webkit.WebViewFactory.nativeReserveAddressSpace(addressSpaceToReserve);
            if (android.webkit.WebViewFactory.sAddressSpaceReserved) {
                if (android.webkit.WebViewFactory.DEBUG) {
                    android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, ("address space reserved: " + addressSpaceToReserve) + " bytes");
                }
            } else {
                android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, ("reserving " + addressSpaceToReserve) + " bytes of address space failed");
            }
        } catch (java.lang.Throwable t) {
            // Log and discard errors at this stage as we must not crash the zygote.
            android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "error preparing native loader", t);
        }
    }

    private static int prepareWebViewInSystemServer(java.lang.String[] nativeLibraryPaths) {
        if (android.webkit.WebViewFactory.DEBUG)
            android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, "creating relro files");

        int numRelros = 0;
        // We must always trigger createRelRo regardless of the value of nativeLibraryPaths. Any
        // unexpected values will be handled there to ensure that we trigger notifying any process
        // waiting on relro creation.
        if (android.os.Build.SUPPORTED_32_BIT_ABIS.length > 0) {
            if (android.webkit.WebViewFactory.DEBUG)
                android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, "Create 32 bit relro");

            /* is64Bit */
            android.webkit.WebViewFactory.createRelroFile(false, nativeLibraryPaths);
            numRelros++;
        }
        if (android.os.Build.SUPPORTED_64_BIT_ABIS.length > 0) {
            if (android.webkit.WebViewFactory.DEBUG)
                android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, "Create 64 bit relro");

            /* is64Bit */
            android.webkit.WebViewFactory.createRelroFile(true, nativeLibraryPaths);
            numRelros++;
        }
        return numRelros;
    }

    /**
     *
     *
     * @unknown 
     */
    public static int onWebViewProviderChanged(android.content.pm.PackageInfo packageInfo) {
        java.lang.String[] nativeLibs = null;
        try {
            nativeLibs = android.webkit.WebViewFactory.getWebViewNativeLibraryPaths(packageInfo);
            if (nativeLibs != null) {
                long newVmSize = 0L;
                for (java.lang.String path : nativeLibs) {
                    if ((path == null) || android.text.TextUtils.isEmpty(path))
                        continue;

                    if (android.webkit.WebViewFactory.DEBUG)
                        android.util.Log.d(android.webkit.WebViewFactory.LOGTAG, "Checking file size of " + path);

                    java.io.File f = new java.io.File(path);
                    if (f.exists()) {
                        newVmSize = java.lang.Math.max(newVmSize, f.length());
                        continue;
                    }
                    if (path.contains("!/")) {
                        java.lang.String[] split = android.text.TextUtils.split(path, "!/");
                        if (split.length == 2) {
                            try (java.util.zip.ZipFile z = new java.util.zip.ZipFile(split[0])) {
                                java.util.zip.ZipEntry e = z.getEntry(split[1]);
                                if ((e != null) && (e.getMethod() == java.util.zip.ZipEntry.STORED)) {
                                    newVmSize = java.lang.Math.max(newVmSize, e.getSize());
                                    continue;
                                }
                            } catch (java.io.IOException e) {
                                android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, ("error reading APK file " + split[0]) + ", ", e);
                            }
                        }
                    }
                    android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "error sizing load for " + path);
                }
                if (android.webkit.WebViewFactory.DEBUG) {
                    android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, ("Based on library size, need " + newVmSize) + " bytes of address space.");
                }
                // The required memory can be larger than the file on disk (due to .bss), and an
                // upgraded version of the library will likely be larger, so always attempt to
                // reserve twice as much as we think to allow for the library to grow during this
                // boot cycle.
                newVmSize = java.lang.Math.max(2 * newVmSize, android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES);
                android.util.Log.d(android.webkit.WebViewFactory.LOGTAG, "Setting new address space to " + newVmSize);
                android.os.SystemProperties.set(android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY, java.lang.Long.toString(newVmSize));
            }
        } catch (java.lang.Throwable t) {
            // Log and discard errors at this stage as we must not crash the system server.
            android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "error preparing webview native library", t);
        }
        return android.webkit.WebViewFactory.prepareWebViewInSystemServer(nativeLibs);
    }

    // throws MissingWebViewPackageException
    private static java.lang.String getLoadFromApkPath(java.lang.String apkPath, java.lang.String[] abiList, java.lang.String nativeLibFileName) {
        // Search the APK for a native library conforming to a listed ABI.
        try (java.util.zip.ZipFile z = new java.util.zip.ZipFile(apkPath)) {
            for (java.lang.String abi : abiList) {
                final java.lang.String entry = (("lib/" + abi) + "/") + nativeLibFileName;
                java.util.zip.ZipEntry e = z.getEntry(entry);
                if ((e != null) && (e.getMethod() == java.util.zip.ZipEntry.STORED)) {
                    // Return a path formatted for dlopen() load from APK.
                    return (apkPath + "!/") + entry;
                }
            }
        } catch (java.io.IOException e) {
            throw new android.webkit.WebViewFactory.MissingWebViewPackageException(e);
        }
        return "";
    }

    // throws MissingWebViewPackageException
    private static java.lang.String[] getWebViewNativeLibraryPaths(android.content.pm.PackageInfo packageInfo) {
        android.content.pm.ApplicationInfo ai = packageInfo.applicationInfo;
        final java.lang.String NATIVE_LIB_FILE_NAME = android.webkit.WebViewFactory.getWebViewLibrary(ai);
        java.lang.String path32;
        java.lang.String path64;
        boolean primaryArchIs64bit = dalvik.system.VMRuntime.is64BitAbi(ai.primaryCpuAbi);
        if (!android.text.TextUtils.isEmpty(ai.secondaryCpuAbi)) {
            // Multi-arch case.
            if (primaryArchIs64bit) {
                // Primary arch: 64-bit, secondary: 32-bit.
                path64 = ai.nativeLibraryDir;
                path32 = ai.secondaryNativeLibraryDir;
            } else {
                // Primary arch: 32-bit, secondary: 64-bit.
                path64 = ai.secondaryNativeLibraryDir;
                path32 = ai.nativeLibraryDir;
            }
        } else
            if (primaryArchIs64bit) {
                // Single-arch 64-bit.
                path64 = ai.nativeLibraryDir;
                path32 = "";
            } else {
                // Single-arch 32-bit.
                path32 = ai.nativeLibraryDir;
                path64 = "";
            }

        // Form the full paths to the extracted native libraries.
        // If libraries were not extracted, try load from APK paths instead.
        if (!android.text.TextUtils.isEmpty(path32)) {
            path32 += "/" + NATIVE_LIB_FILE_NAME;
            java.io.File f = new java.io.File(path32);
            if (!f.exists()) {
                path32 = android.webkit.WebViewFactory.getLoadFromApkPath(ai.sourceDir, android.os.Build.SUPPORTED_32_BIT_ABIS, NATIVE_LIB_FILE_NAME);
            }
        }
        if (!android.text.TextUtils.isEmpty(path64)) {
            path64 += "/" + NATIVE_LIB_FILE_NAME;
            java.io.File f = new java.io.File(path64);
            if (!f.exists()) {
                path64 = android.webkit.WebViewFactory.getLoadFromApkPath(ai.sourceDir, android.os.Build.SUPPORTED_64_BIT_ABIS, NATIVE_LIB_FILE_NAME);
            }
        }
        if (android.webkit.WebViewFactory.DEBUG)
            android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, (("Native 32-bit lib: " + path32) + ", 64-bit lib: ") + path64);

        return new java.lang.String[]{ path32, path64 };
    }

    private static void createRelroFile(final boolean is64Bit, java.lang.String[] nativeLibraryPaths) {
        final java.lang.String abi = (is64Bit) ? android.os.Build.SUPPORTED_64_BIT_ABIS[0] : android.os.Build.SUPPORTED_32_BIT_ABIS[0];
        // crashHandler is invoked by the ActivityManagerService when the isolated process crashes.
        java.lang.Runnable crashHandler = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                try {
                    android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, ("relro file creator for " + abi) + " crashed. Proceeding without");
                    android.webkit.WebViewFactory.getUpdateService().notifyRelroCreationCompleted();
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "Cannot reach WebViewUpdateService. " + e.getMessage());
                }
            }
        };
        try {
            if (((nativeLibraryPaths == null) || (nativeLibraryPaths[0] == null)) || (nativeLibraryPaths[1] == null)) {
                throw new java.lang.IllegalArgumentException("Native library paths to the WebView RelRo process must not be null!");
            }
            int pid = com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class).startIsolatedProcess(android.webkit.WebViewFactory.RelroFileCreator.class.getName(), nativeLibraryPaths, "WebViewLoader-" + abi, abi, android.os.Process.SHARED_RELRO_UID, crashHandler);
            if (pid <= 0)
                throw new java.lang.Exception("Failed to start the relro file creator process");

        } catch (java.lang.Throwable t) {
            // Log and discard errors as we must not crash the system server.
            android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "error starting relro file creator for abi " + abi, t);
            crashHandler.run();
        }
    }

    private static class RelroFileCreator {
        // Called in an unprivileged child process to create the relro file.
        public static void main(java.lang.String[] args) {
            boolean result = false;
            boolean is64Bit = is64Bit();
            try {
                if (((args.length != 2) || (args[0] == null)) || (args[1] == null)) {
                    android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "Invalid RelroFileCreator args: " + java.util.Arrays.toString(args));
                    return;
                }
                android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, ((((("RelroFileCreator (64bit = " + is64Bit) + "), ") + " 32-bit lib: ") + args[0]) + ", 64-bit lib: ") + args[1]);
                if (!android.webkit.WebViewFactory.sAddressSpaceReserved) {
                    android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "can't create relro file; address space not reserved");
                    return;
                }
                result = /* path32 */
                /* path64 */
                android.webkit.WebViewFactory.nativeCreateRelroFile(args[0], args[1], android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_NATIVE_RELRO_32, android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_NATIVE_RELRO_64);
                if (result && android.webkit.WebViewFactory.DEBUG)
                    android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, "created relro file");

            } finally {
                // We must do our best to always notify the update service, even if something fails.
                try {
                    android.webkit.WebViewFactory.getUpdateService().notifyRelroCreationCompleted();
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "error notifying update service", e);
                }
                if (!result)
                    android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "failed to create relro file");

                // Must explicitly exit or else this process will just sit around after we return.
                java.lang.System.exit(0);
            }
        }
    }

    // Assumes that we have waited for relro creation and set sPackageInfo
    private static int loadNativeLibrary(java.lang.ClassLoader clazzLoader) {
        if (!android.webkit.WebViewFactory.sAddressSpaceReserved) {
            android.util.Log.e(android.webkit.WebViewFactory.LOGTAG, "can't load with relro file; address space not reserved");
            return android.webkit.WebViewFactory.LIBLOAD_ADDRESS_SPACE_NOT_RESERVED;
        }
        java.lang.String[] args = android.webkit.WebViewFactory.getWebViewNativeLibraryPaths(android.webkit.WebViewFactory.sPackageInfo);
        int result = /* path32 */
        /* path64 */
        android.webkit.WebViewFactory.nativeLoadWithRelroFile(args[0], args[1], android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_NATIVE_RELRO_32, android.webkit.WebViewFactory.CHROMIUM_WEBVIEW_NATIVE_RELRO_64, clazzLoader);
        if (result != android.webkit.WebViewFactory.LIBLOAD_SUCCESS) {
            android.util.Log.w(android.webkit.WebViewFactory.LOGTAG, "failed to load with relro file, proceeding without");
        } else
            if (android.webkit.WebViewFactory.DEBUG) {
                android.util.Log.v(android.webkit.WebViewFactory.LOGTAG, "loaded with relro file");
            }

        return result;
    }

    private static java.lang.String WEBVIEW_UPDATE_SERVICE_NAME = "webviewupdate";

    /**
     *
     *
     * @unknown 
     */
    public static android.webkit.IWebViewUpdateService getUpdateService() {
        return IWebViewUpdateService.Stub.asInterface(android.os.ServiceManager.getService(android.webkit.WebViewFactory.WEBVIEW_UPDATE_SERVICE_NAME));
    }

    private static native boolean nativeReserveAddressSpace(long addressSpaceToReserve);

    private static native boolean nativeCreateRelroFile(java.lang.String lib32, java.lang.String lib64, java.lang.String relro32, java.lang.String relro64);

    private static native int nativeLoadWithRelroFile(java.lang.String lib32, java.lang.String lib64, java.lang.String relro32, java.lang.String relro64, java.lang.ClassLoader clazzLoader);
}

