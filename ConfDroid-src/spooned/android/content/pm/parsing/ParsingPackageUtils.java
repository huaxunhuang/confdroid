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
package android.content.pm.parsing;


/**
 * TODO(b/135203078): Differentiate between parse_ methods and some add_ method for whether it
 * mutates the passed-in component or not. Or consolidate so all parse_ methods mutate.
 *
 * @unknown 
 */
public class ParsingPackageUtils {
    public static final java.lang.String TAG = android.content.pm.parsing.ParsingUtils.TAG;

    /**
     *
     *
     * @see #parseDefault(ParseInput, File, int, boolean)
     */
    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseDefaultOneTime(java.io.File file, @android.content.pm.PackageParser.ParseFlags
    int parseFlags, boolean collectCertificates) {
        android.content.pm.parsing.result.ParseInput input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing().reset();
        return android.content.pm.parsing.ParsingPackageUtils.parseDefault(input, file, parseFlags, collectCertificates);
    }

    /**
     * For cases outside of PackageManagerService when an APK needs to be parsed as a one-off
     * request, without caching the input object and without querying the internal system state
     * for feature support.
     */
    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseDefault(android.content.pm.parsing.result.ParseInput input, java.io.File file, @android.content.pm.PackageParser.ParseFlags
    int parseFlags, boolean collectCertificates) {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> result;
        android.content.pm.parsing.ParsingPackageUtils parser = new android.content.pm.parsing.ParsingPackageUtils(false, null, null, new android.content.pm.parsing.ParsingPackageUtils.Callback() {
            @java.lang.Override
            public boolean hasFeature(java.lang.String feature) {
                // Assume the device doesn't support anything. This will affect permission parsing
                // and will force <uses-permission/> declarations to include all requiredNotFeature
                // permissions and exclude all requiredFeature permissions. This mirrors the old
                // behavior.
                return false;
            }

            @java.lang.Override
            public android.content.pm.parsing.ParsingPackage startParsingPackage(@android.annotation.NonNull
            java.lang.String packageName, @android.annotation.NonNull
            java.lang.String baseCodePath, @android.annotation.NonNull
            java.lang.String codePath, @android.annotation.NonNull
            android.content.res.TypedArray manifestArray, boolean isCoreApp) {
                return new android.content.pm.parsing.ParsingPackageImpl(packageName, baseCodePath, codePath, manifestArray);
            }
        });
        try {
            result = parser.parsePackage(input, file, parseFlags);
            if (result.isError()) {
                return result;
            }
        } catch (android.content.pm.PackageParser.PackageParserException e) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Error parsing package", e);
        }
        try {
            android.content.pm.parsing.ParsingPackage pkg = result.getResult();
            if (collectCertificates) {
                pkg.setSigningDetails(/* skipVerify */
                android.content.pm.parsing.ParsingPackageUtils.getSigningDetails(pkg, false));
            }
            return input.success(pkg);
        } catch (android.content.pm.PackageParser.PackageParserException e) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Error collecting package certificates", e);
        }
    }

    private boolean mOnlyCoreApps;

    private java.lang.String[] mSeparateProcesses;

    private android.util.DisplayMetrics mDisplayMetrics;

    private android.content.pm.parsing.ParsingPackageUtils.Callback mCallback;

    public ParsingPackageUtils(boolean onlyCoreApps, java.lang.String[] separateProcesses, android.util.DisplayMetrics displayMetrics, @android.annotation.NonNull
    android.content.pm.parsing.ParsingPackageUtils.Callback callback) {
        mOnlyCoreApps = onlyCoreApps;
        mSeparateProcesses = separateProcesses;
        mDisplayMetrics = displayMetrics;
        mCallback = callback;
    }

    /**
     * Parse the package at the given location. Automatically detects if the
     * package is a monolithic style (single APK file) or cluster style
     * (directory of APKs).
     * <p>
     * This performs sanity checking on cluster style packages, such as
     * requiring identical package name and version codes, a single base APK,
     * and unique split names.
     * <p>
     * Note that this <em>does not</em> perform signature verification; that
     * must be done separately in {@link #getSigningDetails(ParsingPackageRead, boolean)}.
     *
     * If {@code useCaches} is true, the package parser might return a cached
     * result from a previous parse of the same {@code packageFile} with the same
     * {@code flags}. Note that this method does not check whether {@code packageFile}
     * has changed since the last parse, it's up to callers to do so.
     *
     * @see PackageParser#parsePackageLite(File, int)
     */
    public android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parsePackage(android.content.pm.parsing.result.ParseInput input, java.io.File packageFile, int flags) throws android.content.pm.PackageParser.PackageParserException {
        if (packageFile.isDirectory()) {
            return parseClusterPackage(input, packageFile, flags);
        } else {
            return parseMonolithicPackage(input, packageFile, flags);
        }
    }

    /**
     * Parse all APKs contained in the given directory, treating them as a
     * single package. This also performs sanity checking, such as requiring
     * identical package name and version codes, a single base APK, and unique
     * split names.
     * <p>
     * Note that this <em>does not</em> perform signature verification; that
     * must be done separately in {@link #getSigningDetails(ParsingPackageRead, boolean)}.
     */
    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseClusterPackage(android.content.pm.parsing.result.ParseInput input, java.io.File packageDir, int flags) {
        android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.PackageLite> liteResult = android.content.pm.parsing.ApkLiteParseUtils.parseClusterPackageLite(input, packageDir, 0);
        if (liteResult.isError()) {
            return input.error(liteResult);
        }
        final android.content.pm.PackageParser.PackageLite lite = liteResult.getResult();
        if (mOnlyCoreApps && (!lite.coreApp)) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_ONLY_COREAPP_ALLOWED, "Not a coreApp: " + packageDir);
        }
        // Build the split dependency tree.
        android.util.SparseArray<int[]> splitDependencies = null;
        final android.content.pm.split.SplitAssetLoader assetLoader;
        if (lite.isolatedSplits && (!com.android.internal.util.ArrayUtils.isEmpty(lite.splitNames))) {
            try {
                splitDependencies = android.content.pm.split.SplitAssetDependencyLoader.createDependenciesFromPackage(lite);
                assetLoader = new android.content.pm.split.SplitAssetDependencyLoader(lite, splitDependencies, flags);
            } catch (android.content.pm.split.SplitDependencyLoader.IllegalDependencyException e) {
                return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, e.getMessage());
            }
        } else {
            assetLoader = new android.content.pm.split.DefaultSplitAssetLoader(lite, flags);
        }
        try {
            final android.content.res.AssetManager assets = assetLoader.getBaseAssetManager();
            final java.io.File baseApk = new java.io.File(lite.baseCodePath);
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> result = parseBaseApk(input, baseApk, lite.codePath, assets, flags);
            if (result.isError()) {
                return input.error(result);
            }
            android.content.pm.parsing.ParsingPackage pkg = result.getResult();
            if (!com.android.internal.util.ArrayUtils.isEmpty(lite.splitNames)) {
                pkg.asSplit(lite.splitNames, lite.splitCodePaths, lite.splitRevisionCodes, splitDependencies);
                final int num = lite.splitNames.length;
                for (int i = 0; i < num; i++) {
                    final android.content.res.AssetManager splitAssets = assetLoader.getSplitAssetManager(i);
                    parseSplitApk(input, pkg, i, splitAssets, flags);
                }
            }
            pkg.setUse32BitAbi(lite.use32bitAbi);
            return input.success(pkg);
        } catch (android.content.pm.PackageParser.PackageParserException e) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to load assets: " + lite.baseCodePath, e);
        } finally {
            libcore.io.IoUtils.closeQuietly(assetLoader);
        }
    }

    /**
     * Parse the given APK file, treating it as as a single monolithic package.
     * <p>
     * Note that this <em>does not</em> perform signature verification; that
     * must be done separately in {@link #getSigningDetails(ParsingPackageRead, boolean)}.
     */
    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseMonolithicPackage(android.content.pm.parsing.result.ParseInput input, java.io.File apkFile, int flags) throws android.content.pm.PackageParser.PackageParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.PackageLite> liteResult = android.content.pm.parsing.ApkLiteParseUtils.parseMonolithicPackageLite(input, apkFile, flags);
        if (liteResult.isError()) {
            return input.error(liteResult);
        }
        final android.content.pm.PackageParser.PackageLite lite = liteResult.getResult();
        if (mOnlyCoreApps && (!lite.coreApp)) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_ONLY_COREAPP_ALLOWED, "Not a coreApp: " + apkFile);
        }
        final android.content.pm.split.SplitAssetLoader assetLoader = new android.content.pm.split.DefaultSplitAssetLoader(lite, flags);
        try {
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> result = parseBaseApk(input, apkFile, apkFile.getCanonicalPath(), assetLoader.getBaseAssetManager(), flags);
            if (result.isError()) {
                return input.error(result);
            }
            return input.success(result.getResult().setUse32BitAbi(lite.use32bitAbi));
        } catch (java.io.IOException e) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to get path: " + apkFile, e);
        } finally {
            libcore.io.IoUtils.closeQuietly(assetLoader);
        }
    }

    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseBaseApk(android.content.pm.parsing.result.ParseInput input, java.io.File apkFile, java.lang.String codePath, android.content.res.AssetManager assets, int flags) {
        final java.lang.String apkPath = apkFile.getAbsolutePath();
        java.lang.String volumeUuid = null;
        if (apkPath.startsWith(android.content.pm.PackageParser.MNT_EXPAND)) {
            final int end = apkPath.indexOf('/', android.content.pm.PackageParser.MNT_EXPAND.length());
            volumeUuid = apkPath.substring(android.content.pm.PackageParser.MNT_EXPAND.length(), end);
        }
        if (android.content.pm.PackageParser.DEBUG_JAR)
            android.util.Slog.d(android.content.pm.parsing.ParsingPackageUtils.TAG, "Scanning base APK: " + apkPath);

        final int cookie = assets.findCookieForPath(apkPath);
        if (cookie == 0) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Failed adding asset path: " + apkPath);
        }
        try (android.content.res.XmlResourceParser parser = assets.openXmlResourceParser(cookie, android.content.pm.PackageParser.ANDROID_MANIFEST_FILENAME)) {
            final android.content.res.Resources res = new android.content.res.Resources(assets, mDisplayMetrics, null);
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> result = parseBaseApk(input, apkPath, codePath, res, parser, flags);
            if (result.isError()) {
                return input.error(result.getErrorCode(), (((apkPath + " (at ") + parser.getPositionDescription()) + "): ") + result.getErrorMessage());
            }
            final android.content.pm.parsing.ParsingPackage pkg = result.getResult();
            if (assets.containsAllocatedTable()) {
                final android.content.pm.parsing.result.ParseResult<?> deferResult = input.deferError(((("Targeting R+ (version " + Build.VERSION_CODES.R) + " and above) requires") + " the resources.arsc of installed APKs to be stored uncompressed") + " and aligned on a 4-byte boundary", android.content.pm.parsing.result.ParseInput.DeferredError.RESOURCES_ARSC_COMPRESSED);
                if (deferResult.isError()) {
                    return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_RESOURCES_ARSC_COMPRESSED, deferResult.getErrorMessage());
                }
            }
            android.content.res.ApkAssets apkAssets = assets.getApkAssets()[0];
            if (apkAssets.definesOverlayable()) {
                android.util.SparseArray<java.lang.String> packageNames = assets.getAssignedPackageIdentifiers();
                int size = packageNames.size();
                for (int index = 0; index < size; index++) {
                    java.lang.String packageName = packageNames.get(index);
                    java.util.Map<java.lang.String, java.lang.String> overlayableToActor = assets.getOverlayableMap(packageName);
                    if ((overlayableToActor != null) && (!overlayableToActor.isEmpty())) {
                        for (java.lang.String overlayable : overlayableToActor.keySet()) {
                            pkg.addOverlayable(overlayable, overlayableToActor.get(overlayable));
                        }
                    }
                }
            }
            pkg.setVolumeUuid(volumeUuid);
            if ((flags & android.content.pm.PackageParser.PARSE_COLLECT_CERTIFICATES) != 0) {
                pkg.setSigningDetails(android.content.pm.parsing.ParsingPackageUtils.getSigningDetails(pkg, false));
            } else {
                pkg.setSigningDetails(android.content.pm.PackageParser.SigningDetails.UNKNOWN);
            }
            return input.success(pkg);
        } catch (java.lang.Exception e) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e);
        }
    }

    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseSplitApk(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, int splitIndex, android.content.res.AssetManager assets, int flags) {
        final java.lang.String apkPath = pkg.getSplitCodePaths()[splitIndex];
        if (android.content.pm.PackageParser.DEBUG_JAR)
            android.util.Slog.d(android.content.pm.parsing.ParsingPackageUtils.TAG, "Scanning split APK: " + apkPath);

        // This must always succeed, as the path has been added to the AssetManager before.
        final int cookie = assets.findCookieForPath(apkPath);
        if (cookie == 0) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Failed adding asset path: " + apkPath);
        }
        try (android.content.res.XmlResourceParser parser = assets.openXmlResourceParser(cookie, android.content.pm.PackageParser.ANDROID_MANIFEST_FILENAME)) {
            android.content.res.Resources res = new android.content.res.Resources(assets, mDisplayMetrics, null);
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseResult = parseSplitApk(input, pkg, res, parser, flags, splitIndex);
            if (parseResult.isError()) {
                return input.error(parseResult.getErrorCode(), (((apkPath + " (at ") + parser.getPositionDescription()) + "): ") + parseResult.getErrorMessage());
            }
            return parseResult;
        } catch (java.lang.Exception e) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e);
        }
    }

    /**
     * Parse the manifest of a <em>base APK</em>. When adding new features you
     * need to consider whether they should be supported by split APKs and child
     * packages.
     *
     * @param apkPath
     * 		The package apk file path
     * @param res
     * 		The resources from which to resolve values
     * @param parser
     * 		The manifest parser
     * @param flags
     * 		Flags how to parse
     * @return Parsed package or null on error.
     */
    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseBaseApk(android.content.pm.parsing.result.ParseInput input, java.lang.String apkPath, java.lang.String codePath, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags) throws android.content.pm.PackageParser.PackageParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final java.lang.String splitName;
        final java.lang.String pkgName;
        android.content.pm.parsing.result.ParseResult<android.util.Pair<java.lang.String, java.lang.String>> packageSplitResult = android.content.pm.parsing.ApkLiteParseUtils.parsePackageSplitNames(input, parser, parser);
        if (packageSplitResult.isError()) {
            return input.error(packageSplitResult);
        }
        android.util.Pair<java.lang.String, java.lang.String> packageSplit = packageSplitResult.getResult();
        pkgName = packageSplit.first;
        splitName = packageSplit.second;
        if (!android.text.TextUtils.isEmpty(splitName)) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Expected base APK, but found split " + splitName);
        }
        final android.content.res.TypedArray manifestArray = res.obtainAttributes(parser, R.styleable.AndroidManifest);
        try {
            final boolean isCoreApp = parser.getAttributeBooleanValue(null, "coreApp", false);
            final android.content.pm.parsing.ParsingPackage pkg = mCallback.startParsingPackage(pkgName, apkPath, codePath, manifestArray, isCoreApp);
            final android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> result = parseBaseApkTags(input, pkg, manifestArray, res, parser, flags);
            if (result.isError()) {
                return result;
            }
            return input.success(pkg);
        } finally {
            manifestArray.recycle();
        }
    }

    /**
     * Parse the manifest of a <em>split APK</em>.
     * <p>
     * Note that split APKs have many more restrictions on what they're capable
     * of doing, so many valid features of a base APK have been carefully
     * omitted here.
     *
     * @param pkg
     * 		builder to fill
     * @return false on failure
     */
    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseSplitApk(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, int splitIndex) throws android.content.pm.PackageParser.PackageParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.util.AttributeSet attrs = parser;
        // We parsed manifest tag earlier; just skip past it
        android.content.pm.PackageParser.parsePackageSplitNames(parser, attrs);
        int type;
        boolean foundApp = false;
        int outerDepth = parser.getDepth();
        while ((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
            if (((outerDepth + 1) < parser.getDepth()) || (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
                continue;
            }
            final android.content.pm.parsing.result.ParseResult result;
            java.lang.String tagName = parser.getName();
            if (android.content.pm.PackageParser.TAG_APPLICATION.equals(tagName)) {
                if (foundApp) {
                    if (android.content.pm.PackageParser.RIGID_PARSER) {
                        result = input.error("<manifest> has more than one <application>");
                    } else {
                        android.util.Slog.w(android.content.pm.parsing.ParsingPackageUtils.TAG, "<manifest> has more than one <application>");
                        result = input.success(null);
                    }
                } else {
                    foundApp = true;
                    result = parseSplitApplication(input, pkg, res, parser, flags, splitIndex);
                }
            } else {
                result = android.content.pm.parsing.ParsingUtils.unknownTag("<manifest>", pkg, parser, input);
            }
            if (result.isError()) {
                return input.error(result);
            }
        } 
        if (!foundApp) {
            android.content.pm.parsing.result.ParseResult<?> deferResult = input.deferError("<manifest> does not contain an <application>", android.content.pm.parsing.result.ParseInput.DeferredError.MISSING_APP_TAG);
            if (deferResult.isError()) {
                return input.error(deferResult);
            }
        }
        return input.success(pkg);
    }

    /**
     * Parse the {@code application} XML tree at the current parse location in a
     * <em>split APK</em> manifest.
     * <p>
     * Note that split APKs have many more restrictions on what they're capable
     * of doing, so many valid features of a base APK have been carefully
     * omitted here.
     */
    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseSplitApplication(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, int splitIndex) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestApplication);
        try {
            pkg.setSplitHasCode(splitIndex, sa.getBoolean(R.styleable.AndroidManifestApplication_hasCode, true));
            final java.lang.String classLoaderName = sa.getString(R.styleable.AndroidManifestApplication_classLoader);
            if ((classLoaderName == null) || com.android.internal.os.ClassLoaderFactory.isValidClassLoaderName(classLoaderName)) {
                pkg.setSplitClassLoaderName(splitIndex, classLoaderName);
            } else {
                return input.error("Invalid class loader name: " + classLoaderName);
            }
        } finally {
            sa.recycle();
        }
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            android.content.pm.parsing.component.ParsedMainComponent mainComponent = null;
            final android.content.pm.parsing.result.ParseResult result;
            java.lang.String tagName = parser.getName();
            boolean isActivity = false;
            switch (tagName) {
                case "activity" :
                    isActivity = true;
                    // fall-through
                case "receiver" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> activityResult = android.content.pm.parsing.component.ParsedActivityUtils.parseActivityOrReceiver(mSeparateProcesses, pkg, res, parser, flags, android.content.pm.PackageParser.sUseRoundIcon, input);
                    if (activityResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedActivity activity = activityResult.getResult();
                        if (isActivity) {
                            pkg.addActivity(activity);
                        } else {
                            pkg.addReceiver(activity);
                        }
                        mainComponent = activity;
                    }
                    result = activityResult;
                    break;
                case "service" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedService> serviceResult = android.content.pm.parsing.component.ParsedServiceUtils.parseService(mSeparateProcesses, pkg, res, parser, flags, android.content.pm.PackageParser.sUseRoundIcon, input);
                    if (serviceResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedService service = serviceResult.getResult();
                        pkg.addService(service);
                        mainComponent = service;
                    }
                    result = serviceResult;
                    break;
                case "provider" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProvider> providerResult = android.content.pm.parsing.component.ParsedProviderUtils.parseProvider(mSeparateProcesses, pkg, res, parser, flags, android.content.pm.PackageParser.sUseRoundIcon, input);
                    if (providerResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedProvider provider = providerResult.getResult();
                        pkg.addProvider(provider);
                        mainComponent = provider;
                    }
                    result = providerResult;
                    break;
                case "activity-alias" :
                    activityResult = android.content.pm.parsing.component.ParsedActivityUtils.parseActivityAlias(pkg, res, parser, android.content.pm.PackageParser.sUseRoundIcon, input);
                    if (activityResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedActivity activity = activityResult.getResult();
                        pkg.addActivity(activity);
                        mainComponent = activity;
                    }
                    result = activityResult;
                    break;
                default :
                    result = parseSplitBaseAppChildTags(input, tagName, pkg, res, parser);
                    break;
            }
            if (result.isError()) {
                return input.error(result);
            }
            if ((mainComponent != null) && (mainComponent.getSplitName() == null)) {
                // If the loaded component did not specify a split, inherit the split name
                // based on the split it is defined in.
                // This is used to later load the correct split when starting this
                // component.
                mainComponent.setSplitName(pkg.getSplitNames()[splitIndex]);
            }
        } 
        return input.success(pkg);
    }

    /**
     * For parsing non-MainComponents. Main ones have an order and some special handling which is
     * done directly in {@link #parseSplitApplication(ParseInput, ParsingPackage, Resources,
     * XmlResourceParser, int, int)}.
     */
    private android.content.pm.parsing.result.ParseResult parseSplitBaseAppChildTags(android.content.pm.parsing.result.ParseInput input, java.lang.String tag, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        switch (tag) {
            case "meta-data" :
                // note: application meta-data is stored off to the side, so it can
                // remain null in the primary copy (we like to avoid extra copies because
                // it can be large)
                android.content.pm.parsing.result.ParseResult<android.os.Bundle> metaDataResult = android.content.pm.parsing.ParsingPackageUtils.parseMetaData(pkg, res, parser, pkg.getMetaData(), input);
                if (metaDataResult.isSuccess()) {
                    pkg.setMetaData(metaDataResult.getResult());
                }
                return metaDataResult;
            case "uses-static-library" :
                return android.content.pm.parsing.ParsingPackageUtils.parseUsesStaticLibrary(input, pkg, res, parser);
            case "uses-library" :
                return android.content.pm.parsing.ParsingPackageUtils.parseUsesLibrary(input, pkg, res, parser);
            case "uses-package" :
                // Dependencies for app installers; we don't currently try to
                // enforce this.
                return input.success(null);
            default :
                return android.content.pm.parsing.ParsingUtils.unknownTag("<application>", pkg, parser, input);
        }
    }

    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseBaseApkTags(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.TypedArray sa, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> sharedUserResult = android.content.pm.parsing.ParsingPackageUtils.parseSharedUser(input, pkg, sa);
        if (sharedUserResult.isError()) {
            return sharedUserResult;
        }
        /* Set the global "on SD card" flag */
        pkg.setInstallLocation(android.content.pm.parsing.ParsingPackageUtils.anInteger(android.content.pm.PackageParser.PARSE_DEFAULT_INSTALL_LOCATION, R.styleable.AndroidManifest_installLocation, sa)).setTargetSandboxVersion(android.content.pm.parsing.ParsingPackageUtils.anInteger(android.content.pm.PackageParser.PARSE_DEFAULT_TARGET_SANDBOX, R.styleable.AndroidManifest_targetSandboxVersion, sa)).setExternalStorage((flags & android.content.pm.PackageParser.PARSE_EXTERNAL_STORAGE) != 0);
        boolean foundApp = false;
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            java.lang.String tagName = parser.getName();
            final android.content.pm.parsing.result.ParseResult result;
            // TODO(b/135203078): Convert to instance methods to share variables
            // <application> has special logic, so it's handled outside the general method
            if (android.content.pm.PackageParser.TAG_APPLICATION.equals(tagName)) {
                if (foundApp) {
                    if (android.content.pm.PackageParser.RIGID_PARSER) {
                        result = input.error("<manifest> has more than one <application>");
                    } else {
                        android.util.Slog.w(android.content.pm.parsing.ParsingPackageUtils.TAG, "<manifest> has more than one <application>");
                        result = input.success(null);
                    }
                } else {
                    foundApp = true;
                    result = parseBaseApplication(input, pkg, res, parser, flags);
                }
            } else {
                result = parseBaseApkTag(tagName, input, pkg, res, parser, flags);
            }
            if (result.isError()) {
                return input.error(result);
            }
        } 
        if ((!foundApp) && (com.android.internal.util.ArrayUtils.size(pkg.getInstrumentations()) == 0)) {
            android.content.pm.parsing.result.ParseResult<?> deferResult = input.deferError("<manifest> does not contain an <application> or <instrumentation>", android.content.pm.parsing.result.ParseInput.DeferredError.MISSING_APP_TAG);
            if (deferResult.isError()) {
                return input.error(deferResult);
            }
        }
        if (!android.content.pm.parsing.component.ParsedAttribution.isCombinationValid(pkg.getAttributions())) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Combination <feature> tags are not valid");
        }
        android.content.pm.parsing.ParsingPackageUtils.convertNewPermissions(pkg);
        android.content.pm.parsing.ParsingPackageUtils.convertSplitPermissions(pkg);
        // At this point we can check if an application is not supporting densities and hence
        // cannot be windowed / resized. Note that an SDK version of 0 is common for
        // pre-Doughnut applications.
        if ((pkg.getTargetSdkVersion() < android.os.Build.VERSION_CODES.DONUT) || ((((((!pkg.isSupportsSmallScreens()) && (!pkg.isSupportsNormalScreens())) && (!pkg.isSupportsLargeScreens())) && (!pkg.isSupportsExtraLargeScreens())) && (!pkg.isResizeable())) && (!pkg.isAnyDensity()))) {
            android.content.pm.parsing.ParsingPackageUtils.adjustPackageToBeUnresizeableAndUnpipable(pkg);
        }
        return input.success(pkg);
    }

    private android.content.pm.parsing.result.ParseResult parseBaseApkTag(java.lang.String tag, android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        switch (tag) {
            case android.content.pm.PackageParser.TAG_OVERLAY :
                return android.content.pm.parsing.ParsingPackageUtils.parseOverlay(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_KEY_SETS :
                return android.content.pm.parsing.ParsingPackageUtils.parseKeySets(input, pkg, res, parser);
            case "feature" :
                // TODO moltmann: Remove
            case android.content.pm.PackageParser.TAG_ATTRIBUTION :
                return android.content.pm.parsing.ParsingPackageUtils.parseAttribution(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_PERMISSION_GROUP :
                return android.content.pm.parsing.ParsingPackageUtils.parsePermissionGroup(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_PERMISSION :
                return android.content.pm.parsing.ParsingPackageUtils.parsePermission(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_PERMISSION_TREE :
                return android.content.pm.parsing.ParsingPackageUtils.parsePermissionTree(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_USES_PERMISSION :
            case android.content.pm.PackageParser.TAG_USES_PERMISSION_SDK_M :
            case android.content.pm.PackageParser.TAG_USES_PERMISSION_SDK_23 :
                return parseUsesPermission(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_USES_CONFIGURATION :
                return android.content.pm.parsing.ParsingPackageUtils.parseUsesConfiguration(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_USES_FEATURE :
                return android.content.pm.parsing.ParsingPackageUtils.parseUsesFeature(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_FEATURE_GROUP :
                return android.content.pm.parsing.ParsingPackageUtils.parseFeatureGroup(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_USES_SDK :
                return android.content.pm.parsing.ParsingPackageUtils.parseUsesSdk(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_SUPPORT_SCREENS :
                return android.content.pm.parsing.ParsingPackageUtils.parseSupportScreens(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_PROTECTED_BROADCAST :
                return android.content.pm.parsing.ParsingPackageUtils.parseProtectedBroadcast(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_INSTRUMENTATION :
                return android.content.pm.parsing.ParsingPackageUtils.parseInstrumentation(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_ORIGINAL_PACKAGE :
                return android.content.pm.parsing.ParsingPackageUtils.parseOriginalPackage(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_ADOPT_PERMISSIONS :
                return android.content.pm.parsing.ParsingPackageUtils.parseAdoptPermissions(input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_USES_GL_TEXTURE :
            case android.content.pm.PackageParser.TAG_COMPATIBLE_SCREENS :
            case android.content.pm.PackageParser.TAG_SUPPORTS_INPUT :
            case android.content.pm.PackageParser.TAG_EAT_COMMENT :
                // Just skip this tag
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                return input.success(pkg);
            case android.content.pm.PackageParser.TAG_RESTRICT_UPDATE :
                return android.content.pm.parsing.ParsingPackageUtils.parseRestrictUpdateHash(flags, input, pkg, res, parser);
            case android.content.pm.PackageParser.TAG_QUERIES :
                return android.content.pm.parsing.ParsingPackageUtils.parseQueries(input, pkg, res, parser);
            default :
                return android.content.pm.parsing.ParsingUtils.unknownTag("<manifest>", pkg, parser, input);
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseSharedUser(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.TypedArray sa) {
        java.lang.String str = android.content.pm.parsing.ParsingPackageUtils.nonConfigString(0, R.styleable.AndroidManifest_sharedUserId, sa);
        if (android.text.TextUtils.isEmpty(str)) {
            return input.success(pkg);
        }
        if (!"android".equals(pkg.getPackageName())) {
            android.content.pm.parsing.result.ParseResult<?> nameResult = android.content.pm.parsing.ParsingPackageUtils.validateName(input, str, true, true);
            if (nameResult.isError()) {
                return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID, (("<manifest> specifies bad sharedUserId name \"" + str) + "\": ") + nameResult.getErrorMessage());
            }
        }
        return input.success(pkg.setSharedUserId(str.intern()).setSharedUserLabel(android.content.pm.parsing.ParsingPackageUtils.resId(R.styleable.AndroidManifest_sharedUserLabel, sa)));
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseKeySets(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // we've encountered the 'key-sets' tag
        // all the keys and keysets that we want must be defined here
        // so we're going to iterate over the parser and pull out the things we want
        int outerDepth = parser.getDepth();
        int currentKeySetDepth = -1;
        int type;
        java.lang.String currentKeySet = null;
        android.util.ArrayMap<java.lang.String, java.security.PublicKey> publicKeys = new android.util.ArrayMap();
        android.util.ArraySet<java.lang.String> upgradeKeySets = new android.util.ArraySet();
        android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> definedKeySets = new android.util.ArrayMap();
        android.util.ArraySet<java.lang.String> improperKeySets = new android.util.ArraySet();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if (type == org.xmlpull.v1.XmlPullParser.END_TAG) {
                if (parser.getDepth() == currentKeySetDepth) {
                    currentKeySet = null;
                    currentKeySetDepth = -1;
                }
                continue;
            }
            java.lang.String tagName = parser.getName();
            switch (tagName) {
                case "key-set" :
                    {
                        if (currentKeySet != null) {
                            return input.error("Improperly nested 'key-set' tag at " + parser.getPositionDescription());
                        }
                        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestKeySet);
                        try {
                            final java.lang.String keysetName = sa.getNonResourceString(R.styleable.AndroidManifestKeySet_name);
                            definedKeySets.put(keysetName, new android.util.ArraySet());
                            currentKeySet = keysetName;
                            currentKeySetDepth = parser.getDepth();
                        } finally {
                            sa.recycle();
                        }
                    }
                    break;
                case "public-key" :
                    {
                        if (currentKeySet == null) {
                            return input.error("Improperly nested 'key-set' tag at " + parser.getPositionDescription());
                        }
                        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPublicKey);
                        try {
                            final java.lang.String publicKeyName = android.content.pm.parsing.ParsingPackageUtils.nonResString(R.styleable.AndroidManifestPublicKey_name, sa);
                            final java.lang.String encodedKey = android.content.pm.parsing.ParsingPackageUtils.nonResString(R.styleable.AndroidManifestPublicKey_value, sa);
                            if ((encodedKey == null) && (publicKeys.get(publicKeyName) == null)) {
                                return input.error((("'public-key' " + publicKeyName) + " must define a public-key value on first use at ") + parser.getPositionDescription());
                            } else
                                if (encodedKey != null) {
                                    java.security.PublicKey currentKey = android.content.pm.PackageParser.parsePublicKey(encodedKey);
                                    if (currentKey == null) {
                                        android.util.Slog.w(android.content.pm.parsing.ParsingPackageUtils.TAG, ((("No recognized valid key in 'public-key' tag at " + parser.getPositionDescription()) + " key-set ") + currentKeySet) + " will not be added to the package's defined key-sets.");
                                        improperKeySets.add(currentKeySet);
                                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                        continue;
                                    }
                                    if ((publicKeys.get(publicKeyName) == null) || publicKeys.get(publicKeyName).equals(currentKey)) {
                                        /* public-key first definition, or matches old definition */
                                        publicKeys.put(publicKeyName, currentKey);
                                    } else {
                                        return input.error((("Value of 'public-key' " + publicKeyName) + " conflicts with previously defined value at ") + parser.getPositionDescription());
                                    }
                                }

                            definedKeySets.get(currentKeySet).add(publicKeyName);
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        } finally {
                            sa.recycle();
                        }
                    }
                    break;
                case "upgrade-key-set" :
                    {
                        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUpgradeKeySet);
                        try {
                            java.lang.String name = sa.getNonResourceString(R.styleable.AndroidManifestUpgradeKeySet_name);
                            upgradeKeySets.add(name);
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        } finally {
                            sa.recycle();
                        }
                    }
                    break;
                default :
                    android.content.pm.parsing.result.ParseResult result = android.content.pm.parsing.ParsingUtils.unknownTag("<key-sets>", pkg, parser, input);
                    if (result.isError()) {
                        return input.error(result);
                    }
                    break;
            }
        } 
        java.lang.String packageName = pkg.getPackageName();
        java.util.Set<java.lang.String> publicKeyNames = publicKeys.keySet();
        if (publicKeyNames.removeAll(definedKeySets.keySet())) {
            return input.error(("Package" + packageName) + " AndroidManifest.xml 'key-set' and 'public-key' names must be distinct.");
        }
        for (ArrayMap.Entry<java.lang.String, android.util.ArraySet<java.lang.String>> e : definedKeySets.entrySet()) {
            final java.lang.String keySetName = e.getKey();
            if (e.getValue().size() == 0) {
                android.util.Slog.w(android.content.pm.parsing.ParsingPackageUtils.TAG, ((((("Package" + packageName) + " AndroidManifest.xml ") + "'key-set' ") + keySetName) + " has no valid associated 'public-key'.") + " Not including in package's defined key-sets.");
                continue;
            } else
                if (improperKeySets.contains(keySetName)) {
                    android.util.Slog.w(android.content.pm.parsing.ParsingPackageUtils.TAG, ((((("Package" + packageName) + " AndroidManifest.xml ") + "'key-set' ") + keySetName) + " contained improper 'public-key'") + " tags. Not including in package's defined key-sets.");
                    continue;
                }

            for (java.lang.String s : e.getValue()) {
                pkg.addKeySet(keySetName, publicKeys.get(s));
            }
        }
        if (pkg.getKeySetMapping().keySet().containsAll(upgradeKeySets)) {
            pkg.setUpgradeKeySets(upgradeKeySets);
        } else {
            return input.error(("Package" + packageName) + " AndroidManifest.xml does not define all 'upgrade-key-set's .");
        }
        return input.success(pkg);
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseAttribution(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedAttribution> result = android.content.pm.parsing.component.ParsedAttributionUtils.parseAttribution(res, parser, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.addAttribution(result.getResult()));
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parsePermissionGroup(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermissionGroup> result = android.content.pm.parsing.component.ParsedPermissionUtils.parsePermissionGroup(pkg, res, parser, android.content.pm.PackageParser.sUseRoundIcon, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.addPermissionGroup(result.getResult()));
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parsePermission(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermission> result = android.content.pm.parsing.component.ParsedPermissionUtils.parsePermission(pkg, res, parser, android.content.pm.PackageParser.sUseRoundIcon, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.addPermission(result.getResult()));
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parsePermissionTree(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermission> result = android.content.pm.parsing.component.ParsedPermissionUtils.parsePermissionTree(pkg, res, parser, android.content.pm.PackageParser.sUseRoundIcon, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.addPermission(result.getResult()));
    }

    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseUsesPermission(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesPermission);
        try {
            // Note: don't allow this value to be a reference to a resource
            // that may change.
            java.lang.String name = sa.getNonResourceString(R.styleable.AndroidManifestUsesPermission_name);
            int maxSdkVersion = 0;
            android.util.TypedValue val = sa.peekValue(R.styleable.AndroidManifestUsesPermission_maxSdkVersion);
            if (val != null) {
                if ((val.type >= android.util.TypedValue.TYPE_FIRST_INT) && (val.type <= android.util.TypedValue.TYPE_LAST_INT)) {
                    maxSdkVersion = val.data;
                }
            }
            final java.lang.String requiredFeature = sa.getNonConfigurationString(R.styleable.AndroidManifestUsesPermission_requiredFeature, 0);
            final java.lang.String requiredNotfeature = sa.getNonConfigurationString(R.styleable.AndroidManifestUsesPermission_requiredNotFeature, 0);
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            // Can only succeed from here on out
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> success = input.success(pkg);
            if (name == null) {
                return success;
            }
            if ((maxSdkVersion != 0) && (maxSdkVersion < Build.VERSION.RESOURCES_SDK_INT)) {
                return success;
            }
            // Only allow requesting this permission if the platform supports the given feature.
            if (((requiredFeature != null) && (mCallback != null)) && (!mCallback.hasFeature(requiredFeature))) {
                return success;
            }
            // Only allow requesting this permission if the platform doesn't support the given
            // feature.
            if (((requiredNotfeature != null) && (mCallback != null)) && mCallback.hasFeature(requiredNotfeature)) {
                return success;
            }
            if (!pkg.getRequestedPermissions().contains(name)) {
                pkg.addRequestedPermission(name.intern());
            } else {
                android.util.Slog.w(android.content.pm.parsing.ParsingPackageUtils.TAG, (((("Ignoring duplicate uses-permissions/uses-permissions-sdk-m: " + name) + " in package: ") + pkg.getPackageName()) + " at: ") + parser.getPositionDescription());
            }
            return success;
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseUsesConfiguration(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.pm.ConfigurationInfo cPref = new android.content.pm.ConfigurationInfo();
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesConfiguration);
        try {
            cPref.reqTouchScreen = sa.getInt(R.styleable.AndroidManifestUsesConfiguration_reqTouchScreen, android.content.res.Configuration.TOUCHSCREEN_UNDEFINED);
            cPref.reqKeyboardType = sa.getInt(R.styleable.AndroidManifestUsesConfiguration_reqKeyboardType, android.content.res.Configuration.KEYBOARD_UNDEFINED);
            if (sa.getBoolean(R.styleable.AndroidManifestUsesConfiguration_reqHardKeyboard, false)) {
                cPref.reqInputFeatures |= android.content.pm.ConfigurationInfo.INPUT_FEATURE_HARD_KEYBOARD;
            }
            cPref.reqNavigation = sa.getInt(R.styleable.AndroidManifestUsesConfiguration_reqNavigation, android.content.res.Configuration.NAVIGATION_UNDEFINED);
            if (sa.getBoolean(R.styleable.AndroidManifestUsesConfiguration_reqFiveWayNav, false)) {
                cPref.reqInputFeatures |= android.content.pm.ConfigurationInfo.INPUT_FEATURE_FIVE_WAY_NAV;
            }
            pkg.addConfigPreference(cPref);
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseUsesFeature(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.pm.FeatureInfo fi = android.content.pm.parsing.ParsingPackageUtils.parseFeatureInfo(res, parser);
        pkg.addReqFeature(fi);
        if (fi.name == null) {
            android.content.pm.ConfigurationInfo cPref = new android.content.pm.ConfigurationInfo();
            cPref.reqGlEsVersion = fi.reqGlEsVersion;
            pkg.addConfigPreference(cPref);
        }
        return input.success(pkg);
    }

    private static android.content.pm.FeatureInfo parseFeatureInfo(android.content.res.Resources res, android.util.AttributeSet attrs) {
        android.content.pm.FeatureInfo fi = new android.content.pm.FeatureInfo();
        android.content.res.TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUsesFeature);
        try {
            // Note: don't allow this value to be a reference to a resource
            // that may change.
            fi.name = sa.getNonResourceString(R.styleable.AndroidManifestUsesFeature_name);
            fi.version = sa.getInt(R.styleable.AndroidManifestUsesFeature_version, 0);
            if (fi.name == null) {
                fi.reqGlEsVersion = sa.getInt(R.styleable.AndroidManifestUsesFeature_glEsVersion, android.content.pm.FeatureInfo.GL_ES_VERSION_UNDEFINED);
            }
            if (sa.getBoolean(R.styleable.AndroidManifestUsesFeature_required, true)) {
                fi.flags |= android.content.pm.FeatureInfo.FLAG_REQUIRED;
            }
            return fi;
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseFeatureGroup(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.FeatureGroupInfo group = new android.content.pm.FeatureGroupInfo();
        java.util.ArrayList<android.content.pm.FeatureInfo> features = null;
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final java.lang.String innerTagName = parser.getName();
            if (innerTagName.equals("uses-feature")) {
                android.content.pm.FeatureInfo featureInfo = android.content.pm.parsing.ParsingPackageUtils.parseFeatureInfo(res, parser);
                // FeatureGroups are stricter and mandate that
                // any <uses-feature> declared are mandatory.
                featureInfo.flags |= android.content.pm.FeatureInfo.FLAG_REQUIRED;
                features = com.android.internal.util.ArrayUtils.add(features, featureInfo);
            } else {
                android.util.Slog.w(android.content.pm.parsing.ParsingPackageUtils.TAG, (((("Unknown element under <feature-group>: " + innerTagName) + " at ") + pkg.getBaseCodePath()) + " ") + parser.getPositionDescription());
            }
        } 
        if (features != null) {
            group.features = new android.content.pm.FeatureInfo[features.size()];
            group.features = features.toArray(group.features);
        }
        pkg.addFeatureGroup(group);
        return input.success(pkg);
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseUsesSdk(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        if (android.content.pm.PackageParser.SDK_VERSION > 0) {
            android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesSdk);
            try {
                int minVers = 1;
                java.lang.String minCode = null;
                int targetVers = 0;
                java.lang.String targetCode = null;
                android.util.TypedValue val = sa.peekValue(R.styleable.AndroidManifestUsesSdk_minSdkVersion);
                if (val != null) {
                    if ((val.type == android.util.TypedValue.TYPE_STRING) && (val.string != null)) {
                        minCode = val.string.toString();
                    } else {
                        // If it's not a string, it's an integer.
                        minVers = val.data;
                    }
                }
                val = sa.peekValue(R.styleable.AndroidManifestUsesSdk_targetSdkVersion);
                if (val != null) {
                    if ((val.type == android.util.TypedValue.TYPE_STRING) && (val.string != null)) {
                        targetCode = val.string.toString();
                        if (minCode == null) {
                            minCode = targetCode;
                        }
                    } else {
                        // If it's not a string, it's an integer.
                        targetVers = val.data;
                    }
                } else {
                    targetVers = minVers;
                    targetCode = minCode;
                }
                android.content.pm.parsing.result.ParseResult<java.lang.Integer> targetSdkVersionResult = android.content.pm.parsing.ParsingPackageUtils.computeTargetSdkVersion(targetVers, targetCode, android.content.pm.PackageParser.SDK_CODENAMES, input);
                if (targetSdkVersionResult.isError()) {
                    return input.error(targetSdkVersionResult);
                }
                int targetSdkVersion = targetSdkVersionResult.getResult();
                android.content.pm.parsing.result.ParseResult<?> deferResult = input.enableDeferredError(pkg.getPackageName(), targetSdkVersion);
                if (deferResult.isError()) {
                    return input.error(deferResult);
                }
                android.content.pm.parsing.result.ParseResult<java.lang.Integer> minSdkVersionResult = android.content.pm.parsing.ParsingPackageUtils.computeMinSdkVersion(minVers, minCode, android.content.pm.PackageParser.SDK_VERSION, android.content.pm.PackageParser.SDK_CODENAMES, input);
                if (minSdkVersionResult.isError()) {
                    return input.error(minSdkVersionResult);
                }
                int minSdkVersion = minSdkVersionResult.getResult();
                pkg.setMinSdkVersion(minSdkVersion).setTargetSdkVersion(targetSdkVersion);
                int type;
                final int innerDepth = parser.getDepth();
                android.util.SparseIntArray minExtensionVersions = null;
                while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
                    if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                        continue;
                    }
                    final android.content.pm.parsing.result.ParseResult result;
                    if (parser.getName().equals("extension-sdk")) {
                        if (minExtensionVersions == null) {
                            minExtensionVersions = new android.util.SparseIntArray();
                        }
                        result = android.content.pm.parsing.ParsingPackageUtils.parseExtensionSdk(input, res, parser, minExtensionVersions);
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    } else {
                        result = android.content.pm.parsing.ParsingUtils.unknownTag("<uses-sdk>", pkg, parser, input);
                    }
                    if (result.isError()) {
                        return input.error(result);
                    }
                } 
                pkg.setMinExtensionVersions(android.content.pm.parsing.ParsingPackageUtils.exactSizedCopyOfSparseArray(minExtensionVersions));
            } finally {
                sa.recycle();
            }
        }
        return input.success(pkg);
    }

    @android.annotation.Nullable
    private static android.util.SparseIntArray exactSizedCopyOfSparseArray(@android.annotation.Nullable
    android.util.SparseIntArray input) {
        if (input == null) {
            return null;
        }
        android.util.SparseIntArray output = new android.util.SparseIntArray(input.size());
        for (int i = 0; i < input.size(); i++) {
            output.put(input.keyAt(i), input.valueAt(i));
        }
        return output;
    }

    private static android.content.pm.parsing.result.ParseResult<android.util.SparseIntArray> parseExtensionSdk(android.content.pm.parsing.result.ParseInput input, android.content.res.Resources res, android.content.res.XmlResourceParser parser, android.util.SparseIntArray minExtensionVersions) {
        int sdkVersion;
        int minVersion;
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestExtensionSdk);
        try {
            sdkVersion = sa.getInt(R.styleable.AndroidManifestExtensionSdk_sdkVersion, -1);
            minVersion = sa.getInt(R.styleable.AndroidManifestExtensionSdk_minExtensionVersion, -1);
        } finally {
            sa.recycle();
        }
        if (sdkVersion < 0) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<extension-sdk> must specify an sdkVersion >= 0");
        }
        if (minVersion < 0) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<extension-sdk> must specify minExtensionVersion >= 0");
        }
        try {
            int version = android.os.ext.SdkExtensions.getExtensionVersion(sdkVersion);
            if (version < minVersion) {
                return input.error(android.content.pm.PackageManager.INSTALL_FAILED_OLDER_SDK, (((("Package requires " + sdkVersion) + " extension version ") + minVersion) + " which exceeds device version ") + version);
            }
        } catch (java.lang.RuntimeException e) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, ("Specified sdkVersion " + sdkVersion) + " is not valid");
        }
        minExtensionVersions.put(sdkVersion, minVersion);
        return input.success(minExtensionVersions);
    }

    /**
     * {@link ParseResult} version of
     * {@link PackageParser#computeMinSdkVersion(int, String, int, String[], String[])}
     */
    public static android.content.pm.parsing.result.ParseResult<java.lang.Integer> computeMinSdkVersion(@android.annotation.IntRange(from = 1)
    int minVers, @android.annotation.Nullable
    java.lang.String minCode, @android.annotation.IntRange(from = 1)
    int platformSdkVersion, @android.annotation.NonNull
    java.lang.String[] platformSdkCodenames, @android.annotation.NonNull
    android.content.pm.parsing.result.ParseInput input) {
        // If it's a release SDK, make sure we meet the minimum SDK requirement.
        if (minCode == null) {
            if (minVers <= platformSdkVersion) {
                return input.success(minVers);
            }
            // We don't meet the minimum SDK requirement.
            return input.error(android.content.pm.PackageManager.INSTALL_FAILED_OLDER_SDK, ((("Requires newer sdk version #" + minVers) + " (current version is #") + platformSdkVersion) + ")");
        }
        // If it's a pre-release SDK and the codename matches this platform, we
        // definitely meet the minimum SDK requirement.
        if (android.content.pm.parsing.ParsingPackageUtils.matchTargetCode(platformSdkCodenames, minCode)) {
            return input.success(Build.VERSION_CODES.CUR_DEVELOPMENT);
        }
        // Otherwise, we're looking at an incompatible pre-release SDK.
        if (platformSdkCodenames.length > 0) {
            return input.error(android.content.pm.PackageManager.INSTALL_FAILED_OLDER_SDK, ((("Requires development platform " + minCode) + " (current platform is any of ") + java.util.Arrays.toString(platformSdkCodenames)) + ")");
        } else {
            return input.error(android.content.pm.PackageManager.INSTALL_FAILED_OLDER_SDK, ("Requires development platform " + minCode) + " but this is a release platform.");
        }
    }

    /**
     * {@link ParseResult} version of
     * {@link PackageParser#computeTargetSdkVersion(int, String, String[], String[])}
     */
    public static android.content.pm.parsing.result.ParseResult<java.lang.Integer> computeTargetSdkVersion(@android.annotation.IntRange(from = 0)
    int targetVers, @android.annotation.Nullable
    java.lang.String targetCode, @android.annotation.NonNull
    java.lang.String[] platformSdkCodenames, @android.annotation.NonNull
    android.content.pm.parsing.result.ParseInput input) {
        // If it's a release SDK, return the version number unmodified.
        if (targetCode == null) {
            return input.success(targetVers);
        }
        // If it's a pre-release SDK and the codename matches this platform, it
        // definitely targets this SDK.
        if (android.content.pm.parsing.ParsingPackageUtils.matchTargetCode(platformSdkCodenames, targetCode)) {
            return input.success(Build.VERSION_CODES.CUR_DEVELOPMENT);
        }
        // Otherwise, we're looking at an incompatible pre-release SDK.
        if (platformSdkCodenames.length > 0) {
            return input.error(android.content.pm.PackageManager.INSTALL_FAILED_OLDER_SDK, ((("Requires development platform " + targetCode) + " (current platform is any of ") + java.util.Arrays.toString(platformSdkCodenames)) + ")");
        } else {
            return input.error(android.content.pm.PackageManager.INSTALL_FAILED_OLDER_SDK, ("Requires development platform " + targetCode) + " but this is a release platform.");
        }
    }

    /**
     * Matches a given {@code targetCode} against a set of release codeNames. Target codes can
     * either be of the form {@code [codename]}" (e.g {@code "Q"}) or of the form
     * {@code [codename].[fingerprint]} (e.g {@code "Q.cafebc561"}).
     */
    private static boolean matchTargetCode(@android.annotation.NonNull
    java.lang.String[] codeNames, @android.annotation.NonNull
    java.lang.String targetCode) {
        final java.lang.String targetCodeName;
        final int targetCodeIdx = targetCode.indexOf('.');
        if (targetCodeIdx == (-1)) {
            targetCodeName = targetCode;
        } else {
            targetCodeName = targetCode.substring(0, targetCodeIdx);
        }
        return com.android.internal.util.ArrayUtils.contains(codeNames, targetCodeName);
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseRestrictUpdateHash(int flags, android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        if ((flags & android.content.pm.PackageParser.PARSE_IS_SYSTEM_DIR) != 0) {
            android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestRestrictUpdate);
            try {
                final java.lang.String hash = sa.getNonConfigurationString(R.styleable.AndroidManifestRestrictUpdate_hash, 0);
                if (hash != null) {
                    final int hashLength = hash.length();
                    final byte[] hashBytes = new byte[hashLength / 2];
                    for (int i = 0; i < hashLength; i += 2) {
                        hashBytes[i / 2] = ((byte) ((java.lang.Character.digit(hash.charAt(i), 16) << 4) + java.lang.Character.digit(hash.charAt(i + 1), 16)));
                    }
                    pkg.setRestrictUpdateHash(hashBytes);
                } else {
                    pkg.setRestrictUpdateHash(null);
                }
            } finally {
                sa.recycle();
            }
        }
        return input.success(pkg);
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseQueries(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals("intent")) {
                android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> result = /* allowGlobs */
                /* allowAutoVerify */
                android.content.pm.parsing.component.ParsedIntentInfoUtils.parseIntentInfo(null, pkg, res, parser, true, true, input);
                if (result.isError()) {
                    return input.error(result);
                }
                android.content.pm.parsing.component.ParsedIntentInfo intentInfo = result.getResult();
                android.net.Uri data = null;
                java.lang.String dataType = null;
                java.lang.String host = null;
                final int numActions = intentInfo.countActions();
                final int numSchemes = intentInfo.countDataSchemes();
                final int numTypes = intentInfo.countDataTypes();
                final int numHosts = intentInfo.getHosts().length;
                if (((numSchemes == 0) && (numTypes == 0)) && (numActions == 0)) {
                    return input.error("intent tags must contain either an action or data.");
                }
                if (numActions > 1) {
                    return input.error("intent tag may have at most one action.");
                }
                if (numTypes > 1) {
                    return input.error("intent tag may have at most one data type.");
                }
                if (numSchemes > 1) {
                    return input.error("intent tag may have at most one data scheme.");
                }
                if (numHosts > 1) {
                    return input.error("intent tag may have at most one data host.");
                }
                android.content.Intent intent = new android.content.Intent();
                for (int i = 0, max = intentInfo.countCategories(); i < max; i++) {
                    intent.addCategory(intentInfo.getCategory(i));
                }
                if (numHosts == 1) {
                    host = intentInfo.getHosts()[0];
                }
                if (numSchemes == 1) {
                    data = new android.net.Uri.Builder().scheme(intentInfo.getDataScheme(0)).authority(host).path(android.content.IntentFilter.WILDCARD_PATH).build();
                }
                if (numTypes == 1) {
                    dataType = intentInfo.getDataType(0);
                    // The dataType may have had the '/' removed for the dynamic mimeType feature.
                    // If we detect that case, we add the * back.
                    if (!dataType.contains("/")) {
                        dataType = dataType + "/*";
                    }
                    if (data == null) {
                        data = new android.net.Uri.Builder().scheme("content").authority(android.content.IntentFilter.WILDCARD).path(android.content.IntentFilter.WILDCARD_PATH).build();
                    }
                }
                intent.setDataAndType(data, dataType);
                if (numActions == 1) {
                    intent.setAction(intentInfo.getAction(0));
                }
                pkg.addQueriesIntent(intent);
            } else
                if (parser.getName().equals("package")) {
                    final android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestQueriesPackage);
                    final java.lang.String packageName = sa.getNonConfigurationString(R.styleable.AndroidManifestQueriesPackage_name, 0);
                    if (android.text.TextUtils.isEmpty(packageName)) {
                        return input.error("Package name is missing from package tag.");
                    }
                    pkg.addQueriesPackage(packageName.intern());
                } else
                    if (parser.getName().equals("provider")) {
                        final android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestQueriesProvider);
                        try {
                            final java.lang.String authorities = sa.getNonConfigurationString(R.styleable.AndroidManifestQueriesProvider_authorities, 0);
                            if (android.text.TextUtils.isEmpty(authorities)) {
                                return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Authority missing from provider tag.");
                            }
                            java.util.StringTokenizer authoritiesTokenizer = new java.util.StringTokenizer(authorities, ";");
                            while (authoritiesTokenizer.hasMoreElements()) {
                                pkg.addQueriesProvider(authoritiesTokenizer.nextToken());
                            } 
                        } finally {
                            sa.recycle();
                        }
                    }


        } 
        return input.success(pkg);
    }

    /**
     * Parse the {@code application} XML tree at the current parse location in a
     * <em>base APK</em> manifest.
     * <p>
     * When adding new features, carefully consider if they should also be
     * supported by split APKs.
     *
     * This method should avoid using a getter for fields set by this method. Prefer assigning
     * a local variable and using it. Otherwise there's an ordering problem which can be broken
     * if any code moves around.
     */
    private android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseBaseApplication(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final java.lang.String pkgName = pkg.getPackageName();
        int targetSdk = pkg.getTargetSdkVersion();
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestApplication);
        try {
            // TODO(b/135203078): Remove this and force unit tests to mock an empty manifest
            // This case can only happen in unit tests where we sometimes need to create fakes
            // of various package parser data structures.
            if (sa == null) {
                return input.error("<application> does not contain any attributes");
            }
            java.lang.String name = sa.getNonConfigurationString(R.styleable.AndroidManifestApplication_name, 0);
            if (name != null) {
                java.lang.String packageName = pkg.getPackageName();
                java.lang.String outInfoName = android.content.pm.parsing.ParsingUtils.buildClassName(packageName, name);
                if (android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(outInfoName)) {
                    return input.error("<application> invalid android:name");
                } else
                    if (outInfoName == null) {
                        return input.error("Empty class name in package " + packageName);
                    }

                pkg.setClassName(outInfoName);
            }
            android.util.TypedValue labelValue = sa.peekValue(R.styleable.AndroidManifestApplication_label);
            if (labelValue != null) {
                pkg.setLabelRes(labelValue.resourceId);
                if (labelValue.resourceId == 0) {
                    pkg.setNonLocalizedLabel(labelValue.coerceToString());
                }
            }
            parseBaseAppBasicFlags(pkg, sa);
            java.lang.String manageSpaceActivity = android.content.pm.parsing.ParsingPackageUtils.nonConfigString(android.content.res.Configuration.NATIVE_CONFIG_VERSION, R.styleable.AndroidManifestApplication_manageSpaceActivity, sa);
            if (manageSpaceActivity != null) {
                java.lang.String manageSpaceActivityName = android.content.pm.parsing.ParsingUtils.buildClassName(pkgName, manageSpaceActivity);
                if (manageSpaceActivityName == null) {
                    return input.error("Empty class name in package " + pkgName);
                }
                pkg.setManageSpaceActivityName(manageSpaceActivityName);
            }
            if (pkg.isAllowBackup()) {
                // backupAgent, killAfterRestore, fullBackupContent, backupInForeground,
                // and restoreAnyVersion are only relevant if backup is possible for the
                // given application.
                java.lang.String backupAgent = android.content.pm.parsing.ParsingPackageUtils.nonConfigString(android.content.res.Configuration.NATIVE_CONFIG_VERSION, R.styleable.AndroidManifestApplication_backupAgent, sa);
                if (backupAgent != null) {
                    java.lang.String backupAgentName = android.content.pm.parsing.ParsingUtils.buildClassName(pkgName, backupAgent);
                    if (backupAgentName == null) {
                        return input.error("Empty class name in package " + pkgName);
                    }
                    if (android.content.pm.PackageParser.DEBUG_BACKUP) {
                        android.util.Slog.v(android.content.pm.parsing.ParsingPackageUtils.TAG, (((("android:backupAgent = " + backupAgentName) + " from ") + pkgName) + "+") + backupAgent);
                    }
                    pkg.setBackupAgentName(backupAgentName).setKillAfterRestore(android.content.pm.parsing.ParsingPackageUtils.bool(true, R.styleable.AndroidManifestApplication_killAfterRestore, sa)).setRestoreAnyVersion(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_restoreAnyVersion, sa)).setFullBackupOnly(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_fullBackupOnly, sa)).setBackupInForeground(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_backupInForeground, sa));
                }
                android.util.TypedValue v = sa.peekValue(R.styleable.AndroidManifestApplication_fullBackupContent);
                int fullBackupContent = 0;
                if (v != null) {
                    fullBackupContent = v.resourceId;
                    if (v.resourceId == 0) {
                        if (android.content.pm.PackageParser.DEBUG_BACKUP) {
                            android.util.Slog.v(android.content.pm.parsing.ParsingPackageUtils.TAG, "fullBackupContent specified as boolean=" + (v.data == 0 ? "false" : "true"));
                        }
                        // "false" => -1, "true" => 0
                        fullBackupContent = (v.data == 0) ? -1 : 0;
                    }
                    pkg.setFullBackupContent(fullBackupContent);
                }
                if (android.content.pm.PackageParser.DEBUG_BACKUP) {
                    android.util.Slog.v(android.content.pm.parsing.ParsingPackageUtils.TAG, (("fullBackupContent=" + fullBackupContent) + " for ") + pkgName);
                }
            }
            if (sa.getBoolean(R.styleable.AndroidManifestApplication_persistent, false)) {
                // Check if persistence is based on a feature being present
                final java.lang.String requiredFeature = sa.getNonResourceString(R.styleable.AndroidManifestApplication_persistentWhenFeatureAvailable);
                pkg.setPersistent((requiredFeature == null) || mCallback.hasFeature(requiredFeature));
            }
            // TODO(b/135203078): Should parsing code be responsible for this? Maybe move to a
            // util or just have PackageImpl return true if either flag is set
            // Debuggable implies profileable
            pkg.setProfileableByShell(pkg.isProfileableByShell() || pkg.isDebuggable());
            if (sa.hasValueOrEmpty(R.styleable.AndroidManifestApplication_resizeableActivity)) {
                pkg.setResizeableActivity(sa.getBoolean(R.styleable.AndroidManifestApplication_resizeableActivity, true));
            } else {
                pkg.setResizeableActivityViaSdkVersion(targetSdk >= Build.VERSION_CODES.N);
            }
            java.lang.String taskAffinity;
            if (targetSdk >= Build.VERSION_CODES.FROYO) {
                taskAffinity = sa.getNonConfigurationString(R.styleable.AndroidManifestApplication_taskAffinity, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
            } else {
                // Some older apps have been seen to use a resource reference
                // here that on older builds was ignored (with a warning).  We
                // need to continue to do this for them so they don't break.
                taskAffinity = sa.getNonResourceString(R.styleable.AndroidManifestApplication_taskAffinity);
            }
            android.content.pm.parsing.result.ParseResult<java.lang.String> taskAffinityResult = android.content.pm.parsing.component.ComponentParseUtils.buildTaskAffinityName(pkgName, pkgName, taskAffinity, input);
            if (taskAffinityResult.isError()) {
                return input.error(taskAffinityResult);
            }
            pkg.setTaskAffinity(taskAffinityResult.getResult());
            java.lang.String factory = sa.getNonResourceString(R.styleable.AndroidManifestApplication_appComponentFactory);
            if (factory != null) {
                java.lang.String appComponentFactory = android.content.pm.parsing.ParsingUtils.buildClassName(pkgName, factory);
                if (appComponentFactory == null) {
                    return input.error("Empty class name in package " + pkgName);
                }
                pkg.setAppComponentFactory(appComponentFactory);
            }
            java.lang.CharSequence pname;
            if (targetSdk >= Build.VERSION_CODES.FROYO) {
                pname = sa.getNonConfigurationString(R.styleable.AndroidManifestApplication_process, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
            } else {
                // Some older apps have been seen to use a resource reference
                // here that on older builds was ignored (with a warning).  We
                // need to continue to do this for them so they don't break.
                pname = sa.getNonResourceString(R.styleable.AndroidManifestApplication_process);
            }
            android.content.pm.parsing.result.ParseResult<java.lang.String> processNameResult = android.content.pm.parsing.component.ComponentParseUtils.buildProcessName(pkgName, null, pname, flags, mSeparateProcesses, input);
            if (processNameResult.isError()) {
                return input.error(processNameResult);
            }
            java.lang.String processName = processNameResult.getResult();
            pkg.setProcessName(processName);
            if (pkg.isCantSaveState()) {
                // A heavy-weight application can not be in a custom process.
                // We can do direct compare because we intern all strings.
                if ((processName != null) && (!processName.equals(pkgName))) {
                    return input.error("cantSaveState applications can not use custom processes");
                }
            }
            java.lang.String classLoaderName = pkg.getClassLoaderName();
            if ((classLoaderName != null) && (!com.android.internal.os.ClassLoaderFactory.isValidClassLoaderName(classLoaderName))) {
                return input.error("Invalid class loader name: " + classLoaderName);
            }
            pkg.setGwpAsanMode(sa.getInt(R.styleable.AndroidManifestApplication_gwpAsanMode, -1));
        } finally {
            sa.recycle();
        }
        boolean hasActivityOrder = false;
        boolean hasReceiverOrder = false;
        boolean hasServiceOrder = false;
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final android.content.pm.parsing.result.ParseResult result;
            java.lang.String tagName = parser.getName();
            boolean isActivity = false;
            switch (tagName) {
                case "activity" :
                    isActivity = true;
                    // fall-through
                case "receiver" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> activityResult = android.content.pm.parsing.component.ParsedActivityUtils.parseActivityOrReceiver(mSeparateProcesses, pkg, res, parser, flags, android.content.pm.PackageParser.sUseRoundIcon, input);
                    if (activityResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedActivity activity = activityResult.getResult();
                        if (isActivity) {
                            hasActivityOrder |= activity.getOrder() != 0;
                            pkg.addActivity(activity);
                        } else {
                            hasReceiverOrder |= activity.getOrder() != 0;
                            pkg.addReceiver(activity);
                        }
                    }
                    result = activityResult;
                    break;
                case "service" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedService> serviceResult = android.content.pm.parsing.component.ParsedServiceUtils.parseService(mSeparateProcesses, pkg, res, parser, flags, android.content.pm.PackageParser.sUseRoundIcon, input);
                    if (serviceResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedService service = serviceResult.getResult();
                        hasServiceOrder |= service.getOrder() != 0;
                        pkg.addService(service);
                    }
                    result = serviceResult;
                    break;
                case "provider" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProvider> providerResult = android.content.pm.parsing.component.ParsedProviderUtils.parseProvider(mSeparateProcesses, pkg, res, parser, flags, android.content.pm.PackageParser.sUseRoundIcon, input);
                    if (providerResult.isSuccess()) {
                        pkg.addProvider(providerResult.getResult());
                    }
                    result = providerResult;
                    break;
                case "activity-alias" :
                    activityResult = android.content.pm.parsing.component.ParsedActivityUtils.parseActivityAlias(pkg, res, parser, android.content.pm.PackageParser.sUseRoundIcon, input);
                    if (activityResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedActivity activity = activityResult.getResult();
                        hasActivityOrder |= activity.getOrder() != 0;
                        pkg.addActivity(activity);
                    }
                    result = activityResult;
                    break;
                default :
                    result = parseBaseAppChildTag(input, tagName, pkg, res, parser, flags);
                    break;
            }
            if (result.isError()) {
                return input.error(result);
            }
        } 
        if (android.text.TextUtils.isEmpty(pkg.getStaticSharedLibName())) {
            // Add a hidden app detail activity to normal apps which forwards user to App Details
            // page.
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> a = android.content.pm.parsing.ParsingPackageUtils.generateAppDetailsHiddenActivity(input, pkg);
            if (a.isError()) {
                // Error should be impossible here, as the only failure case as of SDK R is a
                // string validation error on a constant ":app_details" string passed in by the
                // parsing code itself. For this reason, this is just a hard failure instead of
                // deferred.
                return input.error(a);
            }
            pkg.addActivity(a.getResult());
        }
        if (hasActivityOrder) {
            pkg.sortActivities();
        }
        if (hasReceiverOrder) {
            pkg.sortReceivers();
        }
        if (hasServiceOrder) {
            pkg.sortServices();
        }
        // Must be run after the entire {@link ApplicationInfo} has been fully processed and after
        // every activity info has had a chance to set it from its attributes.
        android.content.pm.parsing.ParsingPackageUtils.setMaxAspectRatio(pkg);
        setMinAspectRatio(pkg);
        setSupportsSizeChanges(pkg);
        pkg.setHasDomainUrls(android.content.pm.parsing.ParsingPackageUtils.hasDomainURLs(pkg));
        return input.success(pkg);
    }

    /**
     * Collection of single-line, no (or little) logic assignments. Separated for readability.
     *
     * Flags are separated by type and by default value. They are sorted alphabetically within each
     * section.
     */
    private void parseBaseAppBasicFlags(android.content.pm.parsing.ParsingPackage pkg, android.content.res.TypedArray sa) {
        int targetSdk = pkg.getTargetSdkVersion();
        // @formatter:off
        // CHECKSTYLE:off
        // Non-Config String
        // Strings
        // Resource ID
        // Floats Default 0f
        // Ints
        // Ints Default 0
        // targetSdkVersion gated
        // Default false
        // Default true
        pkg.setAllowBackup(android.content.pm.parsing.ParsingPackageUtils.bool(true, R.styleable.AndroidManifestApplication_allowBackup, sa)).setAllowClearUserData(android.content.pm.parsing.ParsingPackageUtils.bool(true, R.styleable.AndroidManifestApplication_allowClearUserData, sa)).setAllowClearUserDataOnFailedRestore(android.content.pm.parsing.ParsingPackageUtils.bool(true, R.styleable.AndroidManifestApplication_allowClearUserDataOnFailedRestore, sa)).setAllowNativeHeapPointerTagging(android.content.pm.parsing.ParsingPackageUtils.bool(true, R.styleable.AndroidManifestApplication_allowNativeHeapPointerTagging, sa)).setEnabled(android.content.pm.parsing.ParsingPackageUtils.bool(true, R.styleable.AndroidManifestApplication_enabled, sa)).setExtractNativeLibs(android.content.pm.parsing.ParsingPackageUtils.bool(true, R.styleable.AndroidManifestApplication_extractNativeLibs, sa)).setHasCode(android.content.pm.parsing.ParsingPackageUtils.bool(true, R.styleable.AndroidManifestApplication_hasCode, sa)).setAllowTaskReparenting(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_allowTaskReparenting, sa)).setCantSaveState(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_cantSaveState, sa)).setCrossProfile(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_crossProfile, sa)).setDebuggable(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_debuggable, sa)).setDefaultToDeviceProtectedStorage(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_defaultToDeviceProtectedStorage, sa)).setDirectBootAware(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_directBootAware, sa)).setForceQueryable(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_forceQueryable, sa)).setGame(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_isGame, sa)).setHasFragileUserData(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_hasFragileUserData, sa)).setLargeHeap(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_largeHeap, sa)).setMultiArch(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_multiArch, sa)).setPreserveLegacyExternalStorage(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_preserveLegacyExternalStorage, sa)).setRequiredForAllUsers(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_requiredForAllUsers, sa)).setSupportsRtl(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_supportsRtl, sa)).setTestOnly(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_testOnly, sa)).setUseEmbeddedDex(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_useEmbeddedDex, sa)).setUsesNonSdkApi(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_usesNonSdkApi, sa)).setVmSafeMode(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestApplication_vmSafeMode, sa)).setAutoRevokePermissions(android.content.pm.parsing.ParsingPackageUtils.anInt(R.styleable.AndroidManifestApplication_autoRevokePermissions, sa)).setAllowAudioPlaybackCapture(android.content.pm.parsing.ParsingPackageUtils.bool(targetSdk >= Build.VERSION_CODES.Q, R.styleable.AndroidManifestApplication_allowAudioPlaybackCapture, sa)).setBaseHardwareAccelerated(android.content.pm.parsing.ParsingPackageUtils.bool(targetSdk >= Build.VERSION_CODES.ICE_CREAM_SANDWICH, R.styleable.AndroidManifestApplication_hardwareAccelerated, sa)).setRequestLegacyExternalStorage(android.content.pm.parsing.ParsingPackageUtils.bool(targetSdk < Build.VERSION_CODES.Q, R.styleable.AndroidManifestApplication_requestLegacyExternalStorage, sa)).setUsesCleartextTraffic(android.content.pm.parsing.ParsingPackageUtils.bool(targetSdk < Build.VERSION_CODES.P, R.styleable.AndroidManifestApplication_usesCleartextTraffic, sa)).setUiOptions(android.content.pm.parsing.ParsingPackageUtils.anInt(R.styleable.AndroidManifestApplication_uiOptions, sa)).setCategory(android.content.pm.parsing.ParsingPackageUtils.anInt(android.content.pm.ApplicationInfo.CATEGORY_UNDEFINED, R.styleable.AndroidManifestApplication_appCategory, sa)).setMaxAspectRatio(android.content.pm.parsing.ParsingPackageUtils.aFloat(R.styleable.AndroidManifestApplication_maxAspectRatio, sa)).setMinAspectRatio(android.content.pm.parsing.ParsingPackageUtils.aFloat(R.styleable.AndroidManifestApplication_minAspectRatio, sa)).setBanner(android.content.pm.parsing.ParsingPackageUtils.resId(R.styleable.AndroidManifestApplication_banner, sa)).setDescriptionRes(android.content.pm.parsing.ParsingPackageUtils.resId(R.styleable.AndroidManifestApplication_description, sa)).setIconRes(android.content.pm.parsing.ParsingPackageUtils.resId(R.styleable.AndroidManifestApplication_icon, sa)).setLogo(android.content.pm.parsing.ParsingPackageUtils.resId(R.styleable.AndroidManifestApplication_logo, sa)).setNetworkSecurityConfigRes(android.content.pm.parsing.ParsingPackageUtils.resId(R.styleable.AndroidManifestApplication_networkSecurityConfig, sa)).setRoundIconRes(android.content.pm.parsing.ParsingPackageUtils.resId(R.styleable.AndroidManifestApplication_roundIcon, sa)).setTheme(android.content.pm.parsing.ParsingPackageUtils.resId(R.styleable.AndroidManifestApplication_theme, sa)).setClassLoaderName(android.content.pm.parsing.ParsingPackageUtils.string(R.styleable.AndroidManifestApplication_classLoader, sa)).setRequiredAccountType(android.content.pm.parsing.ParsingPackageUtils.string(R.styleable.AndroidManifestApplication_requiredAccountType, sa)).setRestrictedAccountType(android.content.pm.parsing.ParsingPackageUtils.string(R.styleable.AndroidManifestApplication_restrictedAccountType, sa)).setZygotePreloadName(android.content.pm.parsing.ParsingPackageUtils.string(R.styleable.AndroidManifestApplication_zygotePreloadName, sa)).setPermission(android.content.pm.parsing.ParsingPackageUtils.nonConfigString(0, R.styleable.AndroidManifestApplication_permission, sa));
        // CHECKSTYLE:on
        // @formatter:on
    }

    /**
     * For parsing non-MainComponents. Main ones have an order and some special handling which is
     * done directly in {@link #parseBaseApplication(ParseInput, ParsingPackage, Resources,
     * XmlResourceParser, int)}.
     */
    private android.content.pm.parsing.result.ParseResult parseBaseAppChildTag(android.content.pm.parsing.result.ParseInput input, java.lang.String tag, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        switch (tag) {
            case "meta-data" :
                // TODO(b/135203078): I have no idea what this comment means
                // note: application meta-data is stored off to the side, so it can
                // remain null in the primary copy (we like to avoid extra copies because
                // it can be large)
                android.content.pm.parsing.result.ParseResult<android.os.Bundle> metaDataResult = android.content.pm.parsing.ParsingPackageUtils.parseMetaData(pkg, res, parser, pkg.getMetaData(), input);
                if (metaDataResult.isSuccess()) {
                    pkg.setMetaData(metaDataResult.getResult());
                }
                return metaDataResult;
            case "static-library" :
                return android.content.pm.parsing.ParsingPackageUtils.parseStaticLibrary(pkg, res, parser, input);
            case "library" :
                return android.content.pm.parsing.ParsingPackageUtils.parseLibrary(pkg, res, parser, input);
            case "uses-static-library" :
                return android.content.pm.parsing.ParsingPackageUtils.parseUsesStaticLibrary(input, pkg, res, parser);
            case "uses-library" :
                return android.content.pm.parsing.ParsingPackageUtils.parseUsesLibrary(input, pkg, res, parser);
            case "processes" :
                return android.content.pm.parsing.ParsingPackageUtils.parseProcesses(input, pkg, res, parser, mSeparateProcesses, flags);
            case "uses-package" :
                // Dependencies for app installers; we don't currently try to
                // enforce this.
                return input.success(null);
            case "profileable" :
                return android.content.pm.parsing.ParsingPackageUtils.parseProfileable(input, pkg, res, parser);
            default :
                return android.content.pm.parsing.ParsingUtils.unknownTag("<application>", pkg, parser, input);
        }
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseStaticLibrary(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestStaticLibrary);
        try {
            // Note: don't allow this value to be a reference to a resource
            // that may change.
            java.lang.String lname = sa.getNonResourceString(R.styleable.AndroidManifestStaticLibrary_name);
            final int version = sa.getInt(R.styleable.AndroidManifestStaticLibrary_version, -1);
            final int versionMajor = sa.getInt(R.styleable.AndroidManifestStaticLibrary_versionMajor, 0);
            // Since the app canot run without a static lib - fail if malformed
            if ((lname == null) || (version < 0)) {
                return input.error((("Bad static-library declaration name: " + lname) + " version: ") + version);
            } else
                if (pkg.getSharedUserId() != null) {
                    return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID, "sharedUserId not allowed in static shared library");
                } else
                    if (pkg.getStaticSharedLibName() != null) {
                        return input.error("Multiple static-shared libs for package " + pkg.getPackageName());
                    }


            return input.success(pkg.setStaticSharedLibName(lname.intern()).setStaticSharedLibVersion(android.content.pm.PackageInfo.composeLongVersionCode(versionMajor, version)).setStaticSharedLibrary(true));
        } finally {
            sa.recycle();
        }
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseLibrary(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestLibrary);
        try {
            // Note: don't allow this value to be a reference to a resource
            // that may change.
            java.lang.String lname = sa.getNonResourceString(R.styleable.AndroidManifestLibrary_name);
            if (lname != null) {
                lname = lname.intern();
                if (!com.android.internal.util.ArrayUtils.contains(pkg.getLibraryNames(), lname)) {
                    pkg.addLibraryName(lname);
                }
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseUsesStaticLibrary(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesStaticLibrary);
        try {
            // Note: don't allow this value to be a reference to a resource that may change.
            java.lang.String lname = sa.getNonResourceString(R.styleable.AndroidManifestUsesLibrary_name);
            final int version = sa.getInt(R.styleable.AndroidManifestUsesStaticLibrary_version, -1);
            java.lang.String certSha256Digest = sa.getNonResourceString(R.styleable.AndroidManifestUsesStaticLibrary_certDigest);
            // Since an APK providing a static shared lib can only provide the lib - fail if
            // malformed
            if (((lname == null) || (version < 0)) || (certSha256Digest == null)) {
                return input.error((((("Bad uses-static-library declaration name: " + lname) + " version: ") + version) + " certDigest") + certSha256Digest);
            }
            // Can depend only on one version of the same library
            java.util.List<java.lang.String> usesStaticLibraries = pkg.getUsesStaticLibraries();
            if (usesStaticLibraries.contains(lname)) {
                return input.error("Depending on multiple versions of static library " + lname);
            }
            lname = lname.intern();
            // We allow ":" delimiters in the SHA declaration as this is the format
            // emitted by the certtool making it easy for developers to copy/paste.
            certSha256Digest = certSha256Digest.replace(":", "").toLowerCase();
            // Fot apps targeting O-MR1 we require explicit enumeration of all certs.
            java.lang.String[] additionalCertSha256Digests = libcore.util.EmptyArray.STRING;
            if (pkg.getTargetSdkVersion() >= Build.VERSION_CODES.O_MR1) {
                android.content.pm.parsing.result.ParseResult<java.lang.String[]> certResult = android.content.pm.parsing.ParsingPackageUtils.parseAdditionalCertificates(input, res, parser);
                if (certResult.isError()) {
                    return input.error(certResult);
                }
                additionalCertSha256Digests = certResult.getResult();
            }
            final java.lang.String[] certSha256Digests = new java.lang.String[additionalCertSha256Digests.length + 1];
            certSha256Digests[0] = certSha256Digest;
            java.lang.System.arraycopy(additionalCertSha256Digests, 0, certSha256Digests, 1, additionalCertSha256Digests.length);
            return input.success(pkg.addUsesStaticLibrary(lname).addUsesStaticLibraryVersion(version).addUsesStaticLibraryCertDigests(certSha256Digests));
        } finally {
            sa.recycle();
        }
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseUsesLibrary(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesLibrary);
        try {
            // Note: don't allow this value to be a reference to a resource
            // that may change.
            java.lang.String lname = sa.getNonResourceString(R.styleable.AndroidManifestUsesLibrary_name);
            boolean req = sa.getBoolean(R.styleable.AndroidManifestUsesLibrary_required, true);
            if (lname != null) {
                lname = lname.intern();
                if (req) {
                    // Upgrade to treat as stronger constraint
                    pkg.addUsesLibrary(lname).removeUsesOptionalLibrary(lname);
                } else {
                    // Ignore if someone already defined as required
                    if (!com.android.internal.util.ArrayUtils.contains(pkg.getUsesLibraries(), lname)) {
                        pkg.addUsesOptionalLibrary(lname);
                    }
                }
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseProcesses(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String[] separateProcesses, int flags) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.util.ArrayMap<java.lang.String, android.content.pm.parsing.component.ParsedProcess>> result = android.content.pm.parsing.component.ParsedProcessUtils.parseProcesses(separateProcesses, pkg, res, parser, flags, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.setProcesses(result.getResult()));
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseProfileable(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestProfileable);
        try {
            return input.success(pkg.setProfileableByShell(pkg.isProfileableByShell() || android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestProfileable_shell, sa)));
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.parsing.result.ParseResult<java.lang.String[]> parseAdditionalCertificates(android.content.pm.parsing.result.ParseInput input, android.content.res.Resources resources, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String[] certSha256Digests = libcore.util.EmptyArray.STRING;
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final java.lang.String nodeName = parser.getName();
            if (nodeName.equals("additional-certificate")) {
                android.content.res.TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestAdditionalCertificate);
                try {
                    java.lang.String certSha256Digest = sa.getNonResourceString(R.styleable.AndroidManifestAdditionalCertificate_certDigest);
                    if (android.text.TextUtils.isEmpty(certSha256Digest)) {
                        return input.error(("Bad additional-certificate declaration with empty" + " certDigest:") + certSha256Digest);
                    }
                    // We allow ":" delimiters in the SHA declaration as this is the format
                    // emitted by the certtool making it easy for developers to copy/paste.
                    certSha256Digest = certSha256Digest.replace(":", "").toLowerCase();
                    certSha256Digests = com.android.internal.util.ArrayUtils.appendElement(java.lang.String.class, certSha256Digests, certSha256Digest);
                } finally {
                    sa.recycle();
                }
            }
        } 
        return input.success(certSha256Digests);
    }

    /**
     * Generate activity object that forwards user to App Details page automatically.
     * This activity should be invisible to user and user should not know or see it.
     */
    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> generateAppDetailsHiddenActivity(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg) {
        java.lang.String packageName = pkg.getPackageName();
        android.content.pm.parsing.result.ParseResult<java.lang.String> result = android.content.pm.parsing.component.ComponentParseUtils.buildTaskAffinityName(packageName, packageName, ":app_details", input);
        if (result.isError()) {
            return input.error(result);
        }
        java.lang.String taskAffinity = result.getResult();
        // Build custom App Details activity info instead of parsing it from xml
        return input.success(android.content.pm.parsing.component.ParsedActivity.makeAppDetailsActivity(packageName, pkg.getProcessName(), pkg.getUiOptions(), taskAffinity, pkg.isBaseHardwareAccelerated()));
    }

    /**
     * Check if one of the IntentFilter as both actions DEFAULT / VIEW and a HTTP/HTTPS data URI
     */
    private static boolean hasDomainURLs(android.content.pm.parsing.ParsingPackage pkg) {
        final java.util.List<android.content.pm.parsing.component.ParsedActivity> activities = pkg.getActivities();
        final int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            android.content.pm.parsing.component.ParsedActivity activity = activities.get(index);
            java.util.List<android.content.pm.parsing.component.ParsedIntentInfo> filters = activity.getIntents();
            final int filtersSize = filters.size();
            for (int filtersIndex = 0; filtersIndex < filtersSize; filtersIndex++) {
                android.content.pm.parsing.component.ParsedIntentInfo aii = filters.get(filtersIndex);
                if (!aii.hasAction(android.content.Intent.ACTION_VIEW))
                    continue;

                if (!aii.hasAction(android.content.Intent.ACTION_DEFAULT))
                    continue;

                if (aii.hasDataScheme(android.content.IntentFilter.SCHEME_HTTP) || aii.hasDataScheme(android.content.IntentFilter.SCHEME_HTTPS)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets the max aspect ratio of every child activity that doesn't already have an aspect
     * ratio set.
     */
    private static void setMaxAspectRatio(android.content.pm.parsing.ParsingPackage pkg) {
        // Default to (1.86) 16.7:9 aspect ratio for pre-O apps and unset for O and greater.
        // NOTE: 16.7:9 was the max aspect ratio Android devices can support pre-O per the CDD.
        float maxAspectRatio = (pkg.getTargetSdkVersion() < android.os.Build.VERSION_CODES.O) ? android.content.pm.PackageParser.DEFAULT_PRE_O_MAX_ASPECT_RATIO : 0;
        float packageMaxAspectRatio = pkg.getMaxAspectRatio();
        if (packageMaxAspectRatio != 0) {
            // Use the application max aspect ration as default if set.
            maxAspectRatio = packageMaxAspectRatio;
        } else {
            android.os.Bundle appMetaData = pkg.getMetaData();
            if ((appMetaData != null) && appMetaData.containsKey(android.content.pm.PackageParser.METADATA_MAX_ASPECT_RATIO)) {
                maxAspectRatio = appMetaData.getFloat(android.content.pm.PackageParser.METADATA_MAX_ASPECT_RATIO, maxAspectRatio);
            }
        }
        java.util.List<android.content.pm.parsing.component.ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            android.content.pm.parsing.component.ParsedActivity activity = activities.get(index);
            // If the max aspect ratio for the activity has already been set, skip.
            if (activity.getMaxAspectRatio() != null) {
                continue;
            }
            // By default we prefer to use a values defined on the activity directly than values
            // defined on the application. We do not check the styled attributes on the activity
            // as it would have already been set when we processed the activity. We wait to
            // process the meta data here since this method is called at the end of processing
            // the application and all meta data is guaranteed.
            final float activityAspectRatio = (activity.getMetaData() != null) ? activity.getMetaData().getFloat(android.content.pm.PackageParser.METADATA_MAX_ASPECT_RATIO, maxAspectRatio) : maxAspectRatio;
            activity.setMaxAspectRatio(activity.getResizeMode(), activityAspectRatio);
        }
    }

    /**
     * Sets the min aspect ratio of every child activity that doesn't already have an aspect
     * ratio set.
     */
    private void setMinAspectRatio(android.content.pm.parsing.ParsingPackage pkg) {
        final float minAspectRatio;
        float packageMinAspectRatio = pkg.getMinAspectRatio();
        if (packageMinAspectRatio != 0) {
            // Use the application max aspect ration as default if set.
            minAspectRatio = packageMinAspectRatio;
        } else {
            // Default to (1.33) 4:3 aspect ratio for pre-Q apps and unset for Q and greater.
            // NOTE: 4:3 was the min aspect ratio Android devices can support pre-Q per the CDD,
            // except for watches which always supported 1:1.
            minAspectRatio = (pkg.getTargetSdkVersion() >= Build.VERSION_CODES.Q) ? 0 : (mCallback != null) && mCallback.hasFeature(android.content.pm.PackageManager.FEATURE_WATCH) ? android.content.pm.PackageParser.DEFAULT_PRE_Q_MIN_ASPECT_RATIO_WATCH : android.content.pm.PackageParser.DEFAULT_PRE_Q_MIN_ASPECT_RATIO;
        }
        java.util.List<android.content.pm.parsing.component.ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            android.content.pm.parsing.component.ParsedActivity activity = activities.get(index);
            if (activity.getMinAspectRatio() == null) {
                activity.setMinAspectRatio(activity.getResizeMode(), minAspectRatio);
            }
        }
    }

    private void setSupportsSizeChanges(android.content.pm.parsing.ParsingPackage pkg) {
        final android.os.Bundle appMetaData = pkg.getMetaData();
        final boolean supportsSizeChanges = (appMetaData != null) && appMetaData.getBoolean(android.content.pm.PackageParser.METADATA_SUPPORTS_SIZE_CHANGES, false);
        java.util.List<android.content.pm.parsing.component.ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            android.content.pm.parsing.component.ParsedActivity activity = activities.get(index);
            if (supportsSizeChanges || ((activity.getMetaData() != null) && activity.getMetaData().getBoolean(android.content.pm.PackageParser.METADATA_SUPPORTS_SIZE_CHANGES, false))) {
                activity.setSupportsSizeChanges(true);
            }
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseOverlay(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestResourceOverlay);
        try {
            java.lang.String target = sa.getString(R.styleable.AndroidManifestResourceOverlay_targetPackage);
            int priority = android.content.pm.parsing.ParsingPackageUtils.anInt(0, R.styleable.AndroidManifestResourceOverlay_priority, sa);
            if (target == null) {
                return input.error("<overlay> does not specify a target package");
            } else
                if ((priority < 0) || (priority > 9999)) {
                    return input.error("<overlay> priority must be between 0 and 9999");
                }

            // check to see if overlay should be excluded based on system property condition
            java.lang.String propName = sa.getString(R.styleable.AndroidManifestResourceOverlay_requiredSystemPropertyName);
            java.lang.String propValue = sa.getString(R.styleable.AndroidManifestResourceOverlay_requiredSystemPropertyValue);
            if (!android.content.pm.PackageParser.checkRequiredSystemProperties(propName, propValue)) {
                java.lang.String message = (((((("Skipping target and overlay pair " + target) + " and ") + pkg.getBaseCodePath()) + ": overlay ignored due to required system property: ") + propName) + " with value: ") + propValue;
                android.util.Slog.i(android.content.pm.parsing.ParsingPackageUtils.TAG, message);
                return input.skip(message);
            }
            return input.success(pkg.setOverlay(true).setOverlayTarget(target).setOverlayPriority(priority).setOverlayTargetName(sa.getString(R.styleable.AndroidManifestResourceOverlay_targetName)).setOverlayCategory(sa.getString(R.styleable.AndroidManifestResourceOverlay_category)).setOverlayIsStatic(android.content.pm.parsing.ParsingPackageUtils.bool(false, R.styleable.AndroidManifestResourceOverlay_isStatic, sa)));
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseProtectedBroadcast(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestProtectedBroadcast);
        try {
            // Note: don't allow this value to be a reference to a resource
            // that may change.
            java.lang.String name = android.content.pm.parsing.ParsingPackageUtils.nonResString(R.styleable.AndroidManifestProtectedBroadcast_name, sa);
            if (name != null) {
                pkg.addProtectedBroadcast(name);
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseSupportScreens(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestSupportsScreens);
        try {
            int requiresSmallestWidthDp = android.content.pm.parsing.ParsingPackageUtils.anInt(0, R.styleable.AndroidManifestSupportsScreens_requiresSmallestWidthDp, sa);
            int compatibleWidthLimitDp = android.content.pm.parsing.ParsingPackageUtils.anInt(0, R.styleable.AndroidManifestSupportsScreens_compatibleWidthLimitDp, sa);
            int largestWidthLimitDp = android.content.pm.parsing.ParsingPackageUtils.anInt(0, R.styleable.AndroidManifestSupportsScreens_largestWidthLimitDp, sa);
            // This is a trick to get a boolean and still able to detect
            // if a value was actually set.
            return input.success(pkg.setSupportsSmallScreens(android.content.pm.parsing.ParsingPackageUtils.anInt(1, R.styleable.AndroidManifestSupportsScreens_smallScreens, sa)).setSupportsNormalScreens(android.content.pm.parsing.ParsingPackageUtils.anInt(1, R.styleable.AndroidManifestSupportsScreens_normalScreens, sa)).setSupportsLargeScreens(android.content.pm.parsing.ParsingPackageUtils.anInt(1, R.styleable.AndroidManifestSupportsScreens_largeScreens, sa)).setSupportsExtraLargeScreens(android.content.pm.parsing.ParsingPackageUtils.anInt(1, R.styleable.AndroidManifestSupportsScreens_xlargeScreens, sa)).setResizeable(android.content.pm.parsing.ParsingPackageUtils.anInt(1, R.styleable.AndroidManifestSupportsScreens_resizeable, sa)).setAnyDensity(android.content.pm.parsing.ParsingPackageUtils.anInt(1, R.styleable.AndroidManifestSupportsScreens_anyDensity, sa)).setRequiresSmallestWidthDp(requiresSmallestWidthDp).setCompatibleWidthLimitDp(compatibleWidthLimitDp).setLargestWidthLimitDp(largestWidthLimitDp));
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseInstrumentation(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedInstrumentation> result = android.content.pm.parsing.component.ParsedInstrumentationUtils.parseInstrumentation(pkg, res, parser, android.content.pm.PackageParser.sUseRoundIcon, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.addInstrumentation(result.getResult()));
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseOriginalPackage(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestOriginalPackage);
        try {
            java.lang.String orig = sa.getNonConfigurationString(R.styleable.AndroidManifestOriginalPackage_name, 0);
            if (!pkg.getPackageName().equals(orig)) {
                if (pkg.getOriginalPackages().isEmpty()) {
                    pkg.setRealPackage(pkg.getPackageName());
                }
                pkg.addOriginalPackage(orig);
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ParsingPackage> parseAdoptPermissions(android.content.pm.parsing.result.ParseInput input, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestOriginalPackage);
        try {
            java.lang.String name = android.content.pm.parsing.ParsingPackageUtils.nonConfigString(0, R.styleable.AndroidManifestOriginalPackage_name, sa);
            if (name != null) {
                pkg.addAdoptPermission(name);
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static void convertNewPermissions(android.content.pm.parsing.ParsingPackage pkg) {
        final int NP = android.content.pm.PackageParser.NEW_PERMISSIONS.length;
        java.lang.StringBuilder newPermsMsg = null;
        for (int ip = 0; ip < NP; ip++) {
            final android.content.pm.PackageParser.NewPermissionInfo npi = android.content.pm.PackageParser.NEW_PERMISSIONS[ip];
            if (pkg.getTargetSdkVersion() >= npi.sdkVersion) {
                break;
            }
            if (!pkg.getRequestedPermissions().contains(npi.name)) {
                if (newPermsMsg == null) {
                    newPermsMsg = new java.lang.StringBuilder(128);
                    newPermsMsg.append(pkg.getPackageName());
                    newPermsMsg.append(": compat added ");
                } else {
                    newPermsMsg.append(' ');
                }
                newPermsMsg.append(npi.name);
                pkg.addRequestedPermission(npi.name).addImplicitPermission(npi.name);
            }
        }
        if (newPermsMsg != null) {
            android.util.Slog.i(android.content.pm.parsing.ParsingPackageUtils.TAG, newPermsMsg.toString());
        }
    }

    private static void convertSplitPermissions(android.content.pm.parsing.ParsingPackage pkg) {
        java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> splitPermissions;
        try {
            splitPermissions = android.app.ActivityThread.getPermissionManager().getSplitPermissions();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        final int listSize = splitPermissions.size();
        for (int is = 0; is < listSize; is++) {
            final android.content.pm.permission.SplitPermissionInfoParcelable spi = splitPermissions.get(is);
            java.util.List<java.lang.String> requestedPermissions = pkg.getRequestedPermissions();
            if ((pkg.getTargetSdkVersion() >= spi.getTargetSdk()) || (!requestedPermissions.contains(spi.getSplitPermission()))) {
                continue;
            }
            final java.util.List<java.lang.String> newPerms = spi.getNewPermissions();
            for (int in = 0; in < newPerms.size(); in++) {
                final java.lang.String perm = newPerms.get(in);
                if (!requestedPermissions.contains(perm)) {
                    pkg.addRequestedPermission(perm).addImplicitPermission(perm);
                }
            }
        }
    }

    /**
     * This is a pre-density application which will get scaled - instead of being pixel perfect.
     * This type of application is not resizable.
     *
     * @param pkg
     * 		The package which needs to be marked as unresizable.
     */
    private static void adjustPackageToBeUnresizeableAndUnpipable(android.content.pm.parsing.ParsingPackage pkg) {
        java.util.List<android.content.pm.parsing.component.ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            android.content.pm.parsing.component.ParsedActivity activity = activities.get(index);
            activity.setResizeMode(android.content.pm.ActivityInfo.RESIZE_MODE_UNRESIZEABLE).setFlags(activity.getFlags() & (~android.content.pm.ActivityInfo.FLAG_SUPPORTS_PICTURE_IN_PICTURE));
        }
    }

    private static android.content.pm.parsing.result.ParseResult validateName(android.content.pm.parsing.result.ParseInput input, java.lang.String name, boolean requireSeparator, boolean requireFilename) {
        final int N = name.length();
        boolean hasSep = false;
        boolean front = true;
        for (int i = 0; i < N; i++) {
            final char c = name.charAt(i);
            if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) {
                front = false;
                continue;
            }
            if (!front) {
                if (((c >= '0') && (c <= '9')) || (c == '_')) {
                    continue;
                }
            }
            if (c == '.') {
                hasSep = true;
                front = true;
                continue;
            }
            return input.error(("bad character '" + c) + "'");
        }
        if (requireFilename && (!android.os.FileUtils.isValidExtFilename(name))) {
            return input.error("Invalid filename");
        }
        return hasSep || (!requireSeparator) ? input.success(null) : input.error("must have at least one '.' separator");
    }

    public static android.content.pm.parsing.result.ParseResult<android.os.Bundle> parseMetaData(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, android.os.Bundle data, android.content.pm.parsing.result.ParseInput input) {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestMetaData);
        try {
            if (data == null) {
                data = new android.os.Bundle();
            }
            java.lang.String name = android.text.TextUtils.safeIntern(android.content.pm.parsing.ParsingPackageUtils.nonConfigString(0, R.styleable.AndroidManifestMetaData_name, sa));
            if (name == null) {
                return input.error("<meta-data> requires an android:name attribute");
            }
            android.util.TypedValue v = sa.peekValue(R.styleable.AndroidManifestMetaData_resource);
            if ((v != null) && (v.resourceId != 0)) {
                // Slog.i(TAG, "Meta data ref " + name + ": " + v);
                data.putInt(name, v.resourceId);
            } else {
                v = sa.peekValue(R.styleable.AndroidManifestMetaData_value);
                // Slog.i(TAG, "Meta data " + name + ": " + v);
                if (v != null) {
                    if (v.type == android.util.TypedValue.TYPE_STRING) {
                        java.lang.CharSequence cs = v.coerceToString();
                        data.putString(name, cs != null ? cs.toString() : null);
                    } else
                        if (v.type == android.util.TypedValue.TYPE_INT_BOOLEAN) {
                            data.putBoolean(name, v.data != 0);
                        } else
                            if ((v.type >= android.util.TypedValue.TYPE_FIRST_INT) && (v.type <= android.util.TypedValue.TYPE_LAST_INT)) {
                                data.putInt(name, v.data);
                            } else
                                if (v.type == android.util.TypedValue.TYPE_FLOAT) {
                                    data.putFloat(name, v.getFloat());
                                } else {
                                    if (!android.content.pm.PackageParser.RIGID_PARSER) {
                                        android.util.Slog.w(android.content.pm.parsing.ParsingPackageUtils.TAG, ((((("<meta-data> only supports string, integer, float, color, " + "boolean, and resource reference types: ") + parser.getName()) + " at ") + pkg.getBaseCodePath()) + " ") + parser.getPositionDescription());
                                    } else {
                                        return input.error("<meta-data> only supports string, integer, float, " + "color, boolean, and resource reference types");
                                    }
                                }



                } else {
                    return input.error("<meta-data> requires an android:value " + "or android:resource attribute");
                }
            }
            return input.success(data);
        } finally {
            sa.recycle();
        }
    }

    /**
     * Collect certificates from all the APKs described in the given package. Also asserts that
     * all APK contents are signed correctly and consistently.
     *
     * TODO(b/155513789): Remove this in favor of collecting certificates during the original parse
     *  call if requested. Leaving this as an optional method for the caller means we have to
     *  construct a dummy ParseInput.
     */
    @android.annotation.CheckResult
    public static android.content.pm.PackageParser.SigningDetails getSigningDetails(android.content.pm.parsing.ParsingPackageRead pkg, boolean skipVerify) throws android.content.pm.PackageParser.PackageParserException {
        android.content.pm.PackageParser.SigningDetails signingDetails = android.content.pm.PackageParser.SigningDetails.UNKNOWN;
        android.content.pm.parsing.result.ParseInput input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing().reset();
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER, "collectCertificates");
        try {
            android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.SigningDetails> result = android.content.pm.parsing.ParsingPackageUtils.getSigningDetails(input, pkg.getBaseCodePath(), skipVerify, pkg.isStaticSharedLibrary(), signingDetails, pkg.getTargetSdkVersion());
            if (result.isError()) {
                throw new android.content.pm.PackageParser.PackageParserException(result.getErrorCode(), result.getErrorMessage(), result.getException());
            }
            signingDetails = result.getResult();
            java.lang.String[] splitCodePaths = pkg.getSplitCodePaths();
            if (!com.android.internal.util.ArrayUtils.isEmpty(splitCodePaths)) {
                for (int i = 0; i < splitCodePaths.length; i++) {
                    result = android.content.pm.parsing.ParsingPackageUtils.getSigningDetails(input, splitCodePaths[i], skipVerify, pkg.isStaticSharedLibrary(), signingDetails, pkg.getTargetSdkVersion());
                    if (result.isError()) {
                        throw new android.content.pm.PackageParser.PackageParserException(result.getErrorCode(), result.getErrorMessage(), result.getException());
                    }
                    signingDetails = result.getResult();
                }
            }
            return signingDetails;
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER);
        }
    }

    @android.annotation.CheckResult
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.PackageParser.SigningDetails> getSigningDetails(android.content.pm.parsing.result.ParseInput input, java.lang.String baseCodePath, boolean skipVerify, boolean isStaticSharedLibrary, @android.annotation.NonNull
    android.content.pm.PackageParser.SigningDetails existingSigningDetails, int targetSdk) {
        int minSignatureScheme = android.util.apk.ApkSignatureVerifier.getMinimumSignatureSchemeVersionForTargetSdk(targetSdk);
        if (isStaticSharedLibrary) {
            // must use v2 signing scheme
            minSignatureScheme = android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.SIGNING_BLOCK_V2;
        }
        android.content.pm.PackageParser.SigningDetails verified;
        try {
            if (skipVerify) {
                // systemDir APKs are already trusted, save time by not verifying; since the
                // signature is not verified and some system apps can have their V2+ signatures
                // stripped allow pulling the certs from the jar signature.
                verified = android.util.apk.ApkSignatureVerifier.unsafeGetCertsWithoutVerification(baseCodePath, android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.JAR);
            } else {
                verified = android.util.apk.ApkSignatureVerifier.verify(baseCodePath, minSignatureScheme);
            }
        } catch (android.content.pm.PackageParser.PackageParserException e) {
            return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed collecting certificates for " + baseCodePath, e);
        }
        // Verify that entries are signed consistently with the first pkg
        // we encountered. Note that for splits, certificates may have
        // already been populated during an earlier parse of a base APK.
        if (existingSigningDetails == android.content.pm.PackageParser.SigningDetails.UNKNOWN) {
            return input.success(verified);
        } else {
            if (!android.content.pm.Signature.areExactMatch(existingSigningDetails.signatures, verified.signatures)) {
                return input.error(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES, baseCodePath + " has mismatched certificates");
            }
            return input.success(existingSigningDetails);
        }
    }

    /* The following set of methods makes code easier to read by re-ordering the TypedArray methods.

    The first parameter is the default, which is the most important to understand for someone
    reading through the parsing code.

    That's followed by the attribute name, which is usually irrelevant during reading because
    it'll look like setSomeValue(true, R.styleable.ReallyLongParentName_SomeValueAttr... and
    the "setSomeValue" part is enough to communicate what the line does.

    Last comes the TypedArray, which is by far the least important since each try-with-resources
    should only have 1.
     */
    // Note there is no variant of bool without a defaultValue parameter, since explicit true/false
    // is important to specify when adding an attribute.
    private static boolean bool(boolean defaultValue, @android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getBoolean(attribute, defaultValue);
    }

    private static float aFloat(float defaultValue, @android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getFloat(attribute, defaultValue);
    }

    private static float aFloat(@android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getFloat(attribute, 0.0F);
    }

    private static int anInt(int defaultValue, @android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getInt(attribute, defaultValue);
    }

    private static int anInteger(int defaultValue, @android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getInteger(attribute, defaultValue);
    }

    private static int anInt(@android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getInt(attribute, 0);
    }

    @android.annotation.AnyRes
    private static int resId(@android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getResourceId(attribute, 0);
    }

    private static java.lang.String string(@android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getString(attribute);
    }

    private static java.lang.String nonConfigString(int allowedChangingConfigs, @android.annotation.StyleableRes
    int attribute, android.content.res.TypedArray sa) {
        return sa.getNonConfigurationString(attribute, allowedChangingConfigs);
    }

    private static java.lang.String nonResString(@android.annotation.StyleableRes
    int index, android.content.res.TypedArray sa) {
        return sa.getNonResourceString(index);
    }

    /**
     * Callback interface for retrieving information that may be needed while parsing
     * a package.
     */
    public interface Callback {
        boolean hasFeature(java.lang.String feature);

        android.content.pm.parsing.ParsingPackage startParsingPackage(@android.annotation.NonNull
        java.lang.String packageName, @android.annotation.NonNull
        java.lang.String baseCodePath, @android.annotation.NonNull
        java.lang.String codePath, @android.annotation.NonNull
        android.content.res.TypedArray manifestArray, boolean isCoreApp);
    }
}

