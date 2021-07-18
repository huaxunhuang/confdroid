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
package android.content.pm.parsing;


/**
 *
 *
 * @unknown 
 */
public class ApkLiteParseUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    // TODO(b/135203078): Consolidate constants
    private static final int DEFAULT_MIN_SDK_VERSION = 1;

    private static final int DEFAULT_TARGET_SDK_VERSION = 0;

    private static final int PARSE_DEFAULT_INSTALL_LOCATION = android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED;

    /**
     * Parse only lightweight details about the package at the given location.
     * Automatically detects if the package is a monolithic style (single APK
     * file) or cluster style (directory of APKs).
     * <p>
     * This performs sanity checking on cluster style packages, such as
     * requiring identical package name and version codes, a single base APK,
     * and unique split names.
     *
     * @see PackageParser#parsePackage(File, int)
     */
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.PackageLite> parsePackageLite(android.content.pm.parsing.result.ParseInput input, java.io.File packageFile, int flags) {
        if (packageFile.isDirectory()) {
            return android.content.pm.parsing.ApkLiteParseUtils.parseClusterPackageLite(input, packageFile, flags);
        } else {
            return android.content.pm.parsing.ApkLiteParseUtils.parseMonolithicPackageLite(input, packageFile, flags);
        }
    }

    public static android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.PackageLite> parseMonolithicPackageLite(android.content.pm.parsing.result.ParseInput input, java.io.File packageFile, int flags) {
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER, "parseApkLite");
        try {
            android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.ApkLite> result = android.content.pm.parsing.ApkLiteParseUtils.parseApkLite(input, packageFile, flags);
            if (result.isError()) {
                return input.error(result);
            }
            final android.content.pm.PackageParser.ApkLite baseApk = result.getResult();
            final java.lang.String packagePath = packageFile.getAbsolutePath();
            return input.success(new android.content.pm.PackageParser.PackageLite(packagePath, baseApk, null, null, null, null, null, null));
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER);
        }
    }

    public static android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.PackageLite> parseClusterPackageLite(android.content.pm.parsing.result.ParseInput input, java.io.File packageDir, int flags) {
        final java.io.File[] files = packageDir.listFiles();
        if (com.android.internal.util.ArrayUtils.isEmpty(files)) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NOT_APK, "No packages found in split");
        }
        // Apk directory is directly nested under the current directory
        if ((files.length == 1) && files[0].isDirectory()) {
            return android.content.pm.parsing.ApkLiteParseUtils.parseClusterPackageLite(input, files[0], flags);
        }
        java.lang.String packageName = null;
        int versionCode = 0;
        final android.util.ArrayMap<java.lang.String, android.content.pm.PackageParser.ApkLite> apks = new android.util.ArrayMap();
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER, "parseApkLite");
        try {
            for (java.io.File file : files) {
                if (android.content.pm.PackageParser.isApkFile(file)) {
                    android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.ApkLite> result = android.content.pm.parsing.ApkLiteParseUtils.parseApkLite(input, file, flags);
                    if (result.isError()) {
                        return input.error(result);
                    }
                    final android.content.pm.PackageParser.ApkLite lite = result.getResult();
                    // Assert that all package names and version codes are
                    // consistent with the first one we encounter.
                    if (packageName == null) {
                        packageName = lite.packageName;
                        versionCode = lite.versionCode;
                    } else {
                        if (!packageName.equals(lite.packageName)) {
                            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, (((("Inconsistent package " + lite.packageName) + " in ") + file) + "; expected ") + packageName);
                        }
                        if (versionCode != lite.versionCode) {
                            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, (((("Inconsistent version " + lite.versionCode) + " in ") + file) + "; expected ") + versionCode);
                        }
                    }
                    // Assert that each split is defined only oncuses-static-libe
                    if (apks.put(lite.splitName, lite) != null) {
                        return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, (("Split name " + lite.splitName) + " defined more than once; most recent was ") + file);
                    }
                }
            }
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER);
        }
        final android.content.pm.PackageParser.ApkLite baseApk = apks.remove(null);
        if (baseApk == null) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Missing base APK in " + packageDir);
        }
        // Always apply deterministic ordering based on splitName
        final int size = apks.size();
        java.lang.String[] splitNames = null;
        boolean[] isFeatureSplits = null;
        java.lang.String[] usesSplitNames = null;
        java.lang.String[] configForSplits = null;
        java.lang.String[] splitCodePaths = null;
        int[] splitRevisionCodes = null;
        if (size > 0) {
            splitNames = new java.lang.String[size];
            isFeatureSplits = new boolean[size];
            usesSplitNames = new java.lang.String[size];
            configForSplits = new java.lang.String[size];
            splitCodePaths = new java.lang.String[size];
            splitRevisionCodes = new int[size];
            splitNames = apks.keySet().toArray(splitNames);
            java.util.Arrays.sort(splitNames, android.content.pm.PackageParser.sSplitNameComparator);
            for (int i = 0; i < size; i++) {
                final android.content.pm.PackageParser.ApkLite apk = apks.get(splitNames[i]);
                usesSplitNames[i] = apk.usesSplitName;
                isFeatureSplits[i] = apk.isFeatureSplit;
                configForSplits[i] = apk.configForSplit;
                splitCodePaths[i] = apk.codePath;
                splitRevisionCodes[i] = apk.revisionCode;
            }
        }
        final java.lang.String codePath = packageDir.getAbsolutePath();
        return input.success(new android.content.pm.PackageParser.PackageLite(codePath, baseApk, splitNames, isFeatureSplits, usesSplitNames, configForSplits, splitCodePaths, splitRevisionCodes));
    }

    /**
     * Utility method that retrieves lightweight details about a single APK
     * file, including package name, split name, and install location.
     *
     * @param apkFile
     * 		path to a single APK
     * @param flags
     * 		optional parse flags, such as
     * 		{@link PackageParser#PARSE_COLLECT_CERTIFICATES}
     */
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.ApkLite> parseApkLite(android.content.pm.parsing.result.ParseInput input, java.io.File apkFile, int flags) {
        return android.content.pm.parsing.ApkLiteParseUtils.parseApkLiteInner(input, apkFile, null, null, flags);
    }

    /**
     * Utility method that retrieves lightweight details about a single APK
     * file, including package name, split name, and install location.
     *
     * @param fd
     * 		already open file descriptor of an apk file
     * @param debugPathName
     * 		arbitrary text name for this file, for debug output
     * @param flags
     * 		optional parse flags, such as
     * 		{@link PackageParser#PARSE_COLLECT_CERTIFICATES}
     */
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.ApkLite> parseApkLite(android.content.pm.parsing.result.ParseInput input, java.io.FileDescriptor fd, java.lang.String debugPathName, int flags) {
        return android.content.pm.parsing.ApkLiteParseUtils.parseApkLiteInner(input, null, fd, debugPathName, flags);
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.ApkLite> parseApkLiteInner(android.content.pm.parsing.result.ParseInput input, java.io.File apkFile, java.io.FileDescriptor fd, java.lang.String debugPathName, int flags) {
        final java.lang.String apkPath = (fd != null) ? debugPathName : apkFile.getAbsolutePath();
        android.content.res.XmlResourceParser parser = null;
        android.content.res.ApkAssets apkAssets = null;
        try {
            try {
                apkAssets = (fd != null) ? /* flags */
                /* assets */
                android.content.res.ApkAssets.loadFromFd(fd, debugPathName, 0, null) : android.content.res.ApkAssets.loadFromPath(apkPath);
            } catch (java.io.IOException e) {
                return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NOT_APK, "Failed to parse " + apkPath, e);
            }
            parser = apkAssets.openXml(android.content.pm.PackageParser.ANDROID_MANIFEST_FILENAME);
            final android.content.pm.PackageParser.SigningDetails signingDetails;
            if ((flags & android.content.pm.PackageParser.PARSE_COLLECT_CERTIFICATES) != 0) {
                final boolean skipVerify = (flags & android.content.pm.PackageParser.PARSE_IS_SYSTEM_DIR) != 0;
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER, "collectCertificates");
                try {
                    android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.SigningDetails> result = android.content.pm.parsing.ParsingPackageUtils.getSigningDetails(input, apkFile.getAbsolutePath(), skipVerify, false, android.content.pm.PackageParser.SigningDetails.UNKNOWN, android.content.pm.parsing.ApkLiteParseUtils.DEFAULT_TARGET_SDK_VERSION);
                    if (result.isError()) {
                        return input.error(result);
                    }
                    signingDetails = result.getResult();
                } finally {
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER);
                }
            } else {
                signingDetails = android.content.pm.PackageParser.SigningDetails.UNKNOWN;
            }
            final android.util.AttributeSet attrs = parser;
            return android.content.pm.parsing.ApkLiteParseUtils.parseApkLite(input, apkPath, parser, attrs, signingDetails);
        } catch (org.xmlpull.v1.XmlPullParserException | java.io.IOException | java.lang.RuntimeException e) {
            android.util.Slog.w(android.content.pm.parsing.ApkLiteParseUtils.TAG, "Failed to parse " + apkPath, e);
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e);
        } finally {
            libcore.io.IoUtils.closeQuietly(parser);
            if (apkAssets != null) {
                try {
                    apkAssets.close();
                } catch (java.lang.Throwable ignored) {
                }
            }
            // TODO(b/72056911): Implement AutoCloseable on ApkAssets.
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.ApkLite> parseApkLite(android.content.pm.parsing.result.ParseInput input, java.lang.String codePath, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.pm.PackageParser.SigningDetails signingDetails) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.util.Pair<java.lang.String, java.lang.String>> result = android.content.pm.parsing.ApkLiteParseUtils.parsePackageSplitNames(input, parser, attrs);
        if (result.isError()) {
            return input.error(result);
        }
        android.util.Pair<java.lang.String, java.lang.String> packageSplit = result.getResult();
        int installLocation = android.content.pm.parsing.ApkLiteParseUtils.PARSE_DEFAULT_INSTALL_LOCATION;
        int versionCode = 0;
        int versionCodeMajor = 0;
        int targetSdkVersion = android.content.pm.parsing.ApkLiteParseUtils.DEFAULT_TARGET_SDK_VERSION;
        int minSdkVersion = android.content.pm.parsing.ApkLiteParseUtils.DEFAULT_MIN_SDK_VERSION;
        int revisionCode = 0;
        boolean coreApp = false;
        boolean debuggable = false;
        boolean profilableByShell = false;
        boolean multiArch = false;
        boolean use32bitAbi = false;
        boolean extractNativeLibs = true;
        boolean isolatedSplits = false;
        boolean isFeatureSplit = false;
        boolean isSplitRequired = false;
        boolean useEmbeddedDex = false;
        java.lang.String configForSplit = null;
        java.lang.String usesSplitName = null;
        java.lang.String targetPackage = null;
        boolean overlayIsStatic = false;
        int overlayPriority = 0;
        java.lang.String requiredSystemPropertyName = null;
        java.lang.String requiredSystemPropertyValue = null;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            final java.lang.String attr = attrs.getAttributeName(i);
            switch (attr) {
                case "installLocation" :
                    installLocation = attrs.getAttributeIntValue(i, android.content.pm.parsing.ApkLiteParseUtils.PARSE_DEFAULT_INSTALL_LOCATION);
                    break;
                case "versionCode" :
                    versionCode = attrs.getAttributeIntValue(i, 0);
                    break;
                case "versionCodeMajor" :
                    versionCodeMajor = attrs.getAttributeIntValue(i, 0);
                    break;
                case "revisionCode" :
                    revisionCode = attrs.getAttributeIntValue(i, 0);
                    break;
                case "coreApp" :
                    coreApp = attrs.getAttributeBooleanValue(i, false);
                    break;
                case "isolatedSplits" :
                    isolatedSplits = attrs.getAttributeBooleanValue(i, false);
                    break;
                case "configForSplit" :
                    configForSplit = attrs.getAttributeValue(i);
                    break;
                case "isFeatureSplit" :
                    isFeatureSplit = attrs.getAttributeBooleanValue(i, false);
                    break;
                case "isSplitRequired" :
                    isSplitRequired = attrs.getAttributeBooleanValue(i, false);
                    break;
            }
        }
        // Only search the tree when the tag is the direct child of <manifest> tag
        int type;
        final int searchDepth = parser.getDepth() + 1;
        final java.util.List<android.content.pm.VerifierInfo> verifiers = new java.util.ArrayList<>();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() >= searchDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            if (parser.getDepth() != searchDepth) {
                continue;
            }
            if (android.content.pm.PackageParser.TAG_PACKAGE_VERIFIER.equals(parser.getName())) {
                final android.content.pm.VerifierInfo verifier = android.content.pm.parsing.ApkLiteParseUtils.parseVerifier(attrs);
                if (verifier != null) {
                    verifiers.add(verifier);
                }
            } else
                if (android.content.pm.PackageParser.TAG_APPLICATION.equals(parser.getName())) {
                    for (int i = 0; i < attrs.getAttributeCount(); ++i) {
                        final java.lang.String attr = attrs.getAttributeName(i);
                        switch (attr) {
                            case "debuggable" :
                                debuggable = attrs.getAttributeBooleanValue(i, false);
                                if (debuggable) {
                                    // Debuggable implies profileable
                                    profilableByShell = true;
                                }
                                break;
                            case "multiArch" :
                                multiArch = attrs.getAttributeBooleanValue(i, false);
                                break;
                            case "use32bitAbi" :
                                use32bitAbi = attrs.getAttributeBooleanValue(i, false);
                                break;
                            case "extractNativeLibs" :
                                extractNativeLibs = attrs.getAttributeBooleanValue(i, true);
                                break;
                            case "useEmbeddedDex" :
                                useEmbeddedDex = attrs.getAttributeBooleanValue(i, false);
                                break;
                        }
                    }
                } else
                    if (android.content.pm.PackageParser.TAG_OVERLAY.equals(parser.getName())) {
                        for (int i = 0; i < attrs.getAttributeCount(); ++i) {
                            final java.lang.String attr = attrs.getAttributeName(i);
                            if ("requiredSystemPropertyName".equals(attr)) {
                                requiredSystemPropertyName = attrs.getAttributeValue(i);
                            } else
                                if ("requiredSystemPropertyValue".equals(attr)) {
                                    requiredSystemPropertyValue = attrs.getAttributeValue(i);
                                } else
                                    if ("targetPackage".equals(attr)) {
                                        targetPackage = attrs.getAttributeValue(i);
                                    } else
                                        if ("isStatic".equals(attr)) {
                                            overlayIsStatic = attrs.getAttributeBooleanValue(i, false);
                                        } else
                                            if ("priority".equals(attr)) {
                                                overlayPriority = attrs.getAttributeIntValue(i, 0);
                                            }




                        }
                    } else
                        if (android.content.pm.PackageParser.TAG_USES_SPLIT.equals(parser.getName())) {
                            if (usesSplitName != null) {
                                android.util.Slog.w(android.content.pm.parsing.ApkLiteParseUtils.TAG, "Only one <uses-split> permitted. Ignoring others.");
                                continue;
                            }
                            usesSplitName = attrs.getAttributeValue(android.content.pm.PackageParser.ANDROID_RESOURCES, "name");
                            if (usesSplitName == null) {
                                return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<uses-split> tag requires 'android:name' attribute");
                            }
                        } else
                            if (android.content.pm.PackageParser.TAG_USES_SDK.equals(parser.getName())) {
                                for (int i = 0; i < attrs.getAttributeCount(); ++i) {
                                    final java.lang.String attr = attrs.getAttributeName(i);
                                    if ("targetSdkVersion".equals(attr)) {
                                        targetSdkVersion = attrs.getAttributeIntValue(i, android.content.pm.parsing.ApkLiteParseUtils.DEFAULT_TARGET_SDK_VERSION);
                                    }
                                    if ("minSdkVersion".equals(attr)) {
                                        minSdkVersion = attrs.getAttributeIntValue(i, android.content.pm.parsing.ApkLiteParseUtils.DEFAULT_MIN_SDK_VERSION);
                                    }
                                }
                            } else
                                if (android.content.pm.PackageParser.TAG_PROFILEABLE.equals(parser.getName())) {
                                    for (int i = 0; i < attrs.getAttributeCount(); ++i) {
                                        final java.lang.String attr = attrs.getAttributeName(i);
                                        if ("shell".equals(attr)) {
                                            profilableByShell = attrs.getAttributeBooleanValue(i, profilableByShell);
                                        }
                                    }
                                }





        } 
        // Check to see if overlay should be excluded based on system property condition
        if (!android.content.pm.PackageParser.checkRequiredSystemProperties(requiredSystemPropertyName, requiredSystemPropertyValue)) {
            android.util.Slog.i(android.content.pm.parsing.ApkLiteParseUtils.TAG, (((((("Skipping target and overlay pair " + targetPackage) + " and ") + codePath) + ": overlay ignored due to required system property: ") + requiredSystemPropertyName) + " with value: ") + requiredSystemPropertyValue);
            targetPackage = null;
            overlayIsStatic = false;
            overlayPriority = 0;
        }
        return input.success(new android.content.pm.PackageParser.ApkLite(codePath, packageSplit.first, packageSplit.second, isFeatureSplit, configForSplit, usesSplitName, isSplitRequired, versionCode, versionCodeMajor, revisionCode, installLocation, verifiers, signingDetails, coreApp, debuggable, profilableByShell, multiArch, use32bitAbi, useEmbeddedDex, extractNativeLibs, isolatedSplits, targetPackage, overlayIsStatic, overlayPriority, minSdkVersion, targetSdkVersion));
    }

    public static android.content.pm.parsing.result.ParseResult<android.util.Pair<java.lang.String, java.lang.String>> parsePackageSplitNames(android.content.pm.parsing.result.ParseInput input, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
        } 
        if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No start tag found");
        }
        if (!parser.getName().equals(android.content.pm.PackageParser.TAG_MANIFEST)) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No <manifest> tag");
        }
        final java.lang.String packageName = attrs.getAttributeValue(null, "package");
        if (!"android".equals(packageName)) {
            final java.lang.String error = android.content.pm.PackageParser.validateName(packageName, true, true);
            if (error != null) {
                return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest package: " + error);
            }
        }
        java.lang.String splitName = attrs.getAttributeValue(null, "split");
        if (splitName != null) {
            if (splitName.length() == 0) {
                splitName = null;
            } else {
                final java.lang.String error = android.content.pm.PackageParser.validateName(splitName, false, false);
                if (error != null) {
                    return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest split: " + error);
                }
            }
        }
        return input.success(android.util.Pair.create(packageName.intern(), splitName != null ? splitName.intern() : splitName));
    }

    public static android.content.pm.VerifierInfo parseVerifier(android.util.AttributeSet attrs) {
        java.lang.String packageName = null;
        java.lang.String encodedPublicKey = null;
        final int attrCount = attrs.getAttributeCount();
        for (int i = 0; i < attrCount; i++) {
            final int attrResId = attrs.getAttributeNameResource(i);
            switch (attrResId) {
                case R.attr.name :
                    packageName = attrs.getAttributeValue(i);
                    break;
                case R.attr.publicKey :
                    encodedPublicKey = attrs.getAttributeValue(i);
                    break;
            }
        }
        if ((packageName == null) || (packageName.length() == 0)) {
            android.util.Slog.i(android.content.pm.parsing.ApkLiteParseUtils.TAG, "verifier package name was null; skipping");
            return null;
        }
        final java.security.PublicKey publicKey = android.content.pm.PackageParser.parsePublicKey(encodedPublicKey);
        if (publicKey == null) {
            android.util.Slog.i(android.content.pm.parsing.ApkLiteParseUtils.TAG, "Unable to parse verifier public key for " + packageName);
            return null;
        }
        return new android.content.pm.VerifierInfo(packageName, publicKey);
    }
}

