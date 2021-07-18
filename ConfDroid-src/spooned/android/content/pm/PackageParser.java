/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * Parser for package files (APKs) on disk. This supports apps packaged either
 * as a single "monolithic" APK, or apps packaged as a "cluster" of multiple
 * APKs in a single directory.
 * <p>
 * Apps packaged as multiple APKs always consist of a single "base" APK (with a
 * {@code null} split name) and zero or more "split" APKs (with unique split
 * names). Any subset of those split APKs are a valid install, as long as the
 * following constraints are met:
 * <ul>
 * <li>All APKs must have the exact same package name, version code, and signing
 * certificates.
 * <li>All APKs must have unique split names.
 * <li>All installations must contain a single base APK.
 * </ul>
 *
 * @unknown 
 */
public class PackageParser {
    private static final boolean DEBUG_JAR = false;

    private static final boolean DEBUG_PARSER = false;

    private static final boolean DEBUG_BACKUP = false;

    private static final boolean LOG_PARSE_TIMINGS = android.os.Build.IS_DEBUGGABLE;

    private static final int LOG_PARSE_TIMINGS_THRESHOLD_MS = 100;

    private static final java.lang.String PROPERTY_CHILD_PACKAGES_ENABLED = "persist.sys.child_packages_enabled";

    private static final boolean MULTI_PACKAGE_APK_ENABLED = android.os.Build.IS_DEBUGGABLE && android.os.SystemProperties.getBoolean(android.content.pm.PackageParser.PROPERTY_CHILD_PACKAGES_ENABLED, false);

    private static final float DEFAULT_PRE_O_MAX_ASPECT_RATIO = 1.86F;

    private static final float DEFAULT_PRE_Q_MIN_ASPECT_RATIO = 1.333F;

    private static final float DEFAULT_PRE_Q_MIN_ASPECT_RATIO_WATCH = 1.0F;

    private static final int DEFAULT_MIN_SDK_VERSION = 1;

    private static final int DEFAULT_TARGET_SDK_VERSION = 0;

    // TODO: switch outError users to PackageParserException
    // TODO: refactor "codePath" to "apkPath"
    /**
     * File name in an APK for the Android manifest.
     */
    public static final java.lang.String ANDROID_MANIFEST_FILENAME = "AndroidManifest.xml";

    /**
     * Path prefix for apps on expanded storage
     */
    private static final java.lang.String MNT_EXPAND = "/mnt/expand/";

    private static final java.lang.String TAG_MANIFEST = "manifest";

    private static final java.lang.String TAG_APPLICATION = "application";

    private static final java.lang.String TAG_PACKAGE_VERIFIER = "package-verifier";

    private static final java.lang.String TAG_OVERLAY = "overlay";

    private static final java.lang.String TAG_KEY_SETS = "key-sets";

    private static final java.lang.String TAG_PERMISSION_GROUP = "permission-group";

    private static final java.lang.String TAG_PERMISSION = "permission";

    private static final java.lang.String TAG_PERMISSION_TREE = "permission-tree";

    private static final java.lang.String TAG_USES_PERMISSION = "uses-permission";

    private static final java.lang.String TAG_USES_PERMISSION_SDK_M = "uses-permission-sdk-m";

    private static final java.lang.String TAG_USES_PERMISSION_SDK_23 = "uses-permission-sdk-23";

    private static final java.lang.String TAG_USES_CONFIGURATION = "uses-configuration";

    private static final java.lang.String TAG_USES_FEATURE = "uses-feature";

    private static final java.lang.String TAG_FEATURE_GROUP = "feature-group";

    private static final java.lang.String TAG_USES_SDK = "uses-sdk";

    private static final java.lang.String TAG_SUPPORT_SCREENS = "supports-screens";

    private static final java.lang.String TAG_PROTECTED_BROADCAST = "protected-broadcast";

    private static final java.lang.String TAG_INSTRUMENTATION = "instrumentation";

    private static final java.lang.String TAG_ORIGINAL_PACKAGE = "original-package";

    private static final java.lang.String TAG_ADOPT_PERMISSIONS = "adopt-permissions";

    private static final java.lang.String TAG_USES_GL_TEXTURE = "uses-gl-texture";

    private static final java.lang.String TAG_COMPATIBLE_SCREENS = "compatible-screens";

    private static final java.lang.String TAG_SUPPORTS_INPUT = "supports-input";

    private static final java.lang.String TAG_EAT_COMMENT = "eat-comment";

    private static final java.lang.String TAG_PACKAGE = "package";

    private static final java.lang.String TAG_RESTRICT_UPDATE = "restrict-update";

    private static final java.lang.String TAG_USES_SPLIT = "uses-split";

    private static final java.lang.String METADATA_MAX_ASPECT_RATIO = "android.max_aspect";

    /**
     * Bit mask of all the valid bits that can be set in recreateOnConfigChanges.
     *
     * @unknown 
     */
    private static final int RECREATE_ON_CONFIG_CHANGES_MASK = android.content.pm.ActivityInfo.CONFIG_MCC | android.content.pm.ActivityInfo.CONFIG_MNC;

    // These are the tags supported by child packages
    private static final java.util.Set<java.lang.String> CHILD_PACKAGE_TAGS = new android.util.ArraySet();

    static {
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_APPLICATION);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_USES_PERMISSION);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_USES_PERMISSION_SDK_M);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_USES_PERMISSION_SDK_23);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_USES_CONFIGURATION);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_USES_FEATURE);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_FEATURE_GROUP);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_USES_SDK);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_SUPPORT_SCREENS);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_INSTRUMENTATION);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_USES_GL_TEXTURE);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_COMPATIBLE_SCREENS);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_SUPPORTS_INPUT);
        android.content.pm.PackageParser.CHILD_PACKAGE_TAGS.add(android.content.pm.PackageParser.TAG_EAT_COMMENT);
    }

    private static final boolean LOG_UNSAFE_BROADCASTS = false;

    /**
     * Total number of packages that were read from the cache.  We use it only for logging.
     */
    public static final java.util.concurrent.atomic.AtomicInteger sCachedPackageReadCount = new java.util.concurrent.atomic.AtomicInteger();

    // Set of broadcast actions that are safe for manifest receivers
    private static final java.util.Set<java.lang.String> SAFE_BROADCASTS = new android.util.ArraySet();

    static {
        android.content.pm.PackageParser.SAFE_BROADCASTS.add(android.content.Intent.ACTION_BOOT_COMPLETED);
    }

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String APK_FILE_EXTENSION = ".apk";

    /**
     *
     *
     * @unknown 
     */
    public static class NewPermissionInfo {
        @android.annotation.UnsupportedAppUsage
        public final java.lang.String name;

        @android.annotation.UnsupportedAppUsage
        public final int sdkVersion;

        public final int fileVersion;

        public NewPermissionInfo(java.lang.String name, int sdkVersion, int fileVersion) {
            this.name = name;
            this.sdkVersion = sdkVersion;
            this.fileVersion = fileVersion;
        }
    }

    /**
     * List of new permissions that have been added since 1.0.
     * NOTE: These must be declared in SDK version order, with permissions
     * added to older SDKs appearing before those added to newer SDKs.
     * If sdkVersion is 0, then this is not a permission that we want to
     * automatically add to older apps, but we do want to allow it to be
     * granted during a platform update.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static final android.content.pm.PackageParser.NewPermissionInfo[] NEW_PERMISSIONS = new android.content.pm.PackageParser.NewPermissionInfo[]{ new <android.content.pm.android.Manifest.permission.WRITE_EXTERNAL_STORAGE>android.content.pm.PackageParser.NewPermissionInfo(android.os.Build.VERSION_CODES.DONUT, 0), new <android.content.pm.android.Manifest.permission.READ_PHONE_STATE>android.content.pm.PackageParser.NewPermissionInfo(android.os.Build.VERSION_CODES.DONUT, 0) };

    /**
     *
     *
     * @deprecated callers should move to explicitly passing around source path.
     */
    @java.lang.Deprecated
    private java.lang.String mArchiveSourcePath;

    private java.lang.String[] mSeparateProcesses;

    private boolean mOnlyCoreApps;

    private android.util.DisplayMetrics mMetrics;

    @android.annotation.UnsupportedAppUsage
    private android.content.pm.PackageParser.Callback mCallback;

    private java.io.File mCacheDir;

    private static final int SDK_VERSION = Build.VERSION.SDK_INT;

    private static final java.lang.String[] SDK_CODENAMES = Build.VERSION.ACTIVE_CODENAMES;

    private int mParseError = android.content.pm.PackageManager.INSTALL_SUCCEEDED;

    private static boolean sCompatibilityModeEnabled = true;

    private static boolean sUseRoundIcon = false;

    private static final int PARSE_DEFAULT_INSTALL_LOCATION = android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED;

    private static final int PARSE_DEFAULT_TARGET_SANDBOX = 1;

    static class ParsePackageItemArgs {
        final android.content.pm.PackageParser.Package owner;

        final java.lang.String[] outError;

        final int nameRes;

        final int labelRes;

        final int iconRes;

        final int roundIconRes;

        final int logoRes;

        final int bannerRes;

        java.lang.String tag;

        android.content.res.TypedArray sa;

        ParsePackageItemArgs(android.content.pm.PackageParser.Package _owner, java.lang.String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _roundIconRes, int _logoRes, int _bannerRes) {
            owner = _owner;
            outError = _outError;
            nameRes = _nameRes;
            labelRes = _labelRes;
            iconRes = _iconRes;
            logoRes = _logoRes;
            bannerRes = _bannerRes;
            roundIconRes = _roundIconRes;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static class ParseComponentArgs extends android.content.pm.PackageParser.ParsePackageItemArgs {
        final java.lang.String[] sepProcesses;

        final int processRes;

        final int descriptionRes;

        final int enabledRes;

        int flags;

        public ParseComponentArgs(android.content.pm.PackageParser.Package _owner, java.lang.String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _roundIconRes, int _logoRes, int _bannerRes, java.lang.String[] _sepProcesses, int _processRes, int _descriptionRes, int _enabledRes) {
            super(_owner, _outError, _nameRes, _labelRes, _iconRes, _roundIconRes, _logoRes, _bannerRes);
            sepProcesses = _sepProcesses;
            processRes = _processRes;
            descriptionRes = _descriptionRes;
            enabledRes = _enabledRes;
        }
    }

    /**
     * Lightweight parsed details about a single package.
     */
    public static class PackageLite {
        @android.annotation.UnsupportedAppUsage
        public final java.lang.String packageName;

        public final int versionCode;

        public final int versionCodeMajor;

        @android.annotation.UnsupportedAppUsage
        public final int installLocation;

        public final android.content.pm.VerifierInfo[] verifiers;

        /**
         * Names of any split APKs, ordered by parsed splitName
         */
        public final java.lang.String[] splitNames;

        /**
         * Names of any split APKs that are features. Ordered by splitName
         */
        public final boolean[] isFeatureSplits;

        /**
         * Dependencies of any split APKs, ordered by parsed splitName
         */
        public final java.lang.String[] usesSplitNames;

        public final java.lang.String[] configForSplit;

        /**
         * Path where this package was found on disk. For monolithic packages
         * this is path to single base APK file; for cluster packages this is
         * path to the cluster directory.
         */
        public final java.lang.String codePath;

        /**
         * Path of base APK
         */
        public final java.lang.String baseCodePath;

        /**
         * Paths of any split APKs, ordered by parsed splitName
         */
        public final java.lang.String[] splitCodePaths;

        /**
         * Revision code of base APK
         */
        public final int baseRevisionCode;

        /**
         * Revision codes of any split APKs, ordered by parsed splitName
         */
        public final int[] splitRevisionCodes;

        public final boolean coreApp;

        public final boolean debuggable;

        public final boolean multiArch;

        public final boolean use32bitAbi;

        public final boolean extractNativeLibs;

        public final boolean isolatedSplits;

        public PackageLite(java.lang.String codePath, android.content.pm.PackageParser.ApkLite baseApk, java.lang.String[] splitNames, boolean[] isFeatureSplits, java.lang.String[] usesSplitNames, java.lang.String[] configForSplit, java.lang.String[] splitCodePaths, int[] splitRevisionCodes) {
            this.packageName = baseApk.packageName;
            this.versionCode = baseApk.versionCode;
            this.versionCodeMajor = baseApk.versionCodeMajor;
            this.installLocation = baseApk.installLocation;
            this.verifiers = baseApk.verifiers;
            this.splitNames = splitNames;
            this.isFeatureSplits = isFeatureSplits;
            this.usesSplitNames = usesSplitNames;
            this.configForSplit = configForSplit;
            this.codePath = codePath;
            this.baseCodePath = baseApk.codePath;
            this.splitCodePaths = splitCodePaths;
            this.baseRevisionCode = baseApk.revisionCode;
            this.splitRevisionCodes = splitRevisionCodes;
            this.coreApp = baseApk.coreApp;
            this.debuggable = baseApk.debuggable;
            this.multiArch = baseApk.multiArch;
            this.use32bitAbi = baseApk.use32bitAbi;
            this.extractNativeLibs = baseApk.extractNativeLibs;
            this.isolatedSplits = baseApk.isolatedSplits;
        }

        public java.util.List<java.lang.String> getAllCodePaths() {
            java.util.ArrayList<java.lang.String> paths = new java.util.ArrayList<>();
            paths.add(baseCodePath);
            if (!com.android.internal.util.ArrayUtils.isEmpty(splitCodePaths)) {
                java.util.Collections.addAll(paths, splitCodePaths);
            }
            return paths;
        }
    }

    /**
     * Lightweight parsed details about a single APK file.
     */
    public static class ApkLite {
        public final java.lang.String codePath;

        public final java.lang.String packageName;

        public final java.lang.String splitName;

        public boolean isFeatureSplit;

        public final java.lang.String configForSplit;

        public final java.lang.String usesSplitName;

        public final int versionCode;

        public final int versionCodeMajor;

        public final int revisionCode;

        public final int installLocation;

        public final int minSdkVersion;

        public final int targetSdkVersion;

        public final android.content.pm.VerifierInfo[] verifiers;

        public final android.content.pm.PackageParser.SigningDetails signingDetails;

        public final boolean coreApp;

        public final boolean debuggable;

        public final boolean multiArch;

        public final boolean use32bitAbi;

        public final boolean extractNativeLibs;

        public final boolean isolatedSplits;

        public final boolean isSplitRequired;

        public final boolean useEmbeddedDex;

        public ApkLite(java.lang.String codePath, java.lang.String packageName, java.lang.String splitName, boolean isFeatureSplit, java.lang.String configForSplit, java.lang.String usesSplitName, boolean isSplitRequired, int versionCode, int versionCodeMajor, int revisionCode, int installLocation, java.util.List<android.content.pm.VerifierInfo> verifiers, android.content.pm.PackageParser.SigningDetails signingDetails, boolean coreApp, boolean debuggable, boolean multiArch, boolean use32bitAbi, boolean useEmbeddedDex, boolean extractNativeLibs, boolean isolatedSplits, int minSdkVersion, int targetSdkVersion) {
            this.codePath = codePath;
            this.packageName = packageName;
            this.splitName = splitName;
            this.isFeatureSplit = isFeatureSplit;
            this.configForSplit = configForSplit;
            this.usesSplitName = usesSplitName;
            this.versionCode = versionCode;
            this.versionCodeMajor = versionCodeMajor;
            this.revisionCode = revisionCode;
            this.installLocation = installLocation;
            this.signingDetails = signingDetails;
            this.verifiers = verifiers.toArray(new android.content.pm.VerifierInfo[verifiers.size()]);
            this.coreApp = coreApp;
            this.debuggable = debuggable;
            this.multiArch = multiArch;
            this.use32bitAbi = use32bitAbi;
            this.useEmbeddedDex = useEmbeddedDex;
            this.extractNativeLibs = extractNativeLibs;
            this.isolatedSplits = isolatedSplits;
            this.isSplitRequired = isSplitRequired;
            this.minSdkVersion = minSdkVersion;
            this.targetSdkVersion = targetSdkVersion;
        }

        public long getLongVersionCode() {
            return android.content.pm.PackageInfo.composeLongVersionCode(versionCodeMajor, versionCode);
        }
    }

    /**
     * Cached parse state for new components.
     *
     * Allows reuse of the same parse argument records to avoid GC pressure.  Lifetime is carefully
     * scoped to the parsing of a single application element.
     */
    private static class CachedComponentArgs {
        android.content.pm.PackageParser.ParseComponentArgs mActivityArgs;

        android.content.pm.PackageParser.ParseComponentArgs mActivityAliasArgs;

        android.content.pm.PackageParser.ParseComponentArgs mServiceArgs;

        android.content.pm.PackageParser.ParseComponentArgs mProviderArgs;
    }

    /**
     * Cached state for parsing instrumentation to avoid GC pressure.
     *
     * Must be manually reset to null for each new manifest.
     */
    private android.content.pm.PackageParser.ParsePackageItemArgs mParseInstrumentationArgs;

    /**
     * If set to true, we will only allow package files that exactly match
     *  the DTD.  Otherwise, we try to get as much from the package as we
     *  can without failing.  This should normally be set to false, to
     *  support extensions to the DTD in future versions.
     */
    private static final boolean RIGID_PARSER = false;

    private static final java.lang.String TAG = "PackageParser";

    @android.annotation.UnsupportedAppUsage
    public PackageParser() {
        mMetrics = new android.util.DisplayMetrics();
        mMetrics.setToDefaults();
    }

    @android.annotation.UnsupportedAppUsage
    public void setSeparateProcesses(java.lang.String[] procs) {
        mSeparateProcesses = procs;
    }

    /**
     * Flag indicating this parser should only consider apps with
     * {@code coreApp} manifest attribute to be valid apps. This is useful when
     * creating a minimalist boot environment.
     */
    public void setOnlyCoreApps(boolean onlyCoreApps) {
        mOnlyCoreApps = onlyCoreApps;
    }

    public void setDisplayMetrics(android.util.DisplayMetrics metrics) {
        mMetrics = metrics;
    }

    /**
     * Sets the cache directory for this package parser.
     */
    public void setCacheDir(java.io.File cacheDir) {
        mCacheDir = cacheDir;
    }

    /**
     * Callback interface for retrieving information that may be needed while parsing
     * a package.
     */
    public interface Callback {
        boolean hasFeature(java.lang.String feature);

        java.lang.String[] getOverlayPaths(java.lang.String targetPackageName, java.lang.String targetPath);

        java.lang.String[] getOverlayApks(java.lang.String targetPackageName);
    }

    /**
     * Standard implementation of {@link Callback} on top of the public {@link PackageManager}
     * class.
     */
    public static final class CallbackImpl implements android.content.pm.PackageParser.Callback {
        private final android.content.pm.PackageManager mPm;

        public CallbackImpl(android.content.pm.PackageManager pm) {
            mPm = pm;
        }

        @java.lang.Override
        public boolean hasFeature(java.lang.String feature) {
            return mPm.hasSystemFeature(feature);
        }

        @java.lang.Override
        public java.lang.String[] getOverlayPaths(java.lang.String targetPackageName, java.lang.String targetPath) {
            return null;
        }

        @java.lang.Override
        public java.lang.String[] getOverlayApks(java.lang.String targetPackageName) {
            return null;
        }
    }

    /**
     * Set the {@link Callback} that can be used while parsing.
     */
    public void setCallback(android.content.pm.PackageParser.Callback cb) {
        mCallback = cb;
    }

    public static final boolean isApkFile(java.io.File file) {
        return android.content.pm.PackageParser.isApkPath(file.getName());
    }

    public static boolean isApkPath(java.lang.String path) {
        return path.endsWith(android.content.pm.PackageParser.APK_FILE_EXTENSION);
    }

    /**
     * Generate and return the {@link PackageInfo} for a parsed package.
     *
     * @param p
     * 		the parsed package.
     * @param flags
     * 		indicating which optional information is included.
     */
    @android.annotation.UnsupportedAppUsage
    public static android.content.pm.PackageInfo generatePackageInfo(android.content.pm.PackageParser.Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, java.util.Set<java.lang.String> grantedPermissions, android.content.pm.PackageUserState state) {
        return android.content.pm.PackageParser.generatePackageInfo(p, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, state, android.os.UserHandle.getCallingUserId());
    }

    /**
     * Returns true if the package is installed and not hidden, or if the caller
     * explicitly wanted all uninstalled and hidden packages as well.
     *
     * @param appInfo
     * 		The applicationInfo of the app being checked.
     */
    private static boolean checkUseInstalledOrHidden(int flags, android.content.pm.PackageUserState state, android.content.pm.ApplicationInfo appInfo) {
        // Returns false if the package is hidden system app until installed.
        if (((((flags & android.content.pm.PackageManager.MATCH_HIDDEN_UNTIL_INSTALLED_COMPONENTS) == 0) && (!state.installed)) && (appInfo != null)) && appInfo.hiddenUntilInstalled) {
            return false;
        }
        // If available for the target user, or trying to match uninstalled packages and it's
        // a system app.
        return state.isAvailable(flags) || (((appInfo != null) && appInfo.isSystemApp()) && (((flags & android.content.pm.PackageManager.MATCH_KNOWN_PACKAGES) != 0) || ((flags & android.content.pm.PackageManager.MATCH_HIDDEN_UNTIL_INSTALLED_COMPONENTS) != 0)));
    }

    public static boolean isAvailable(android.content.pm.PackageUserState state) {
        return android.content.pm.PackageParser.checkUseInstalledOrHidden(0, state, null);
    }

    @android.annotation.UnsupportedAppUsage
    public static android.content.pm.PackageInfo generatePackageInfo(android.content.pm.PackageParser.Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, java.util.Set<java.lang.String> grantedPermissions, android.content.pm.PackageUserState state, int userId) {
        if ((!android.content.pm.PackageParser.checkUseInstalledOrHidden(flags, state, p.applicationInfo)) || (!p.isMatch(flags))) {
            return null;
        }
        android.content.pm.PackageInfo pi = new android.content.pm.PackageInfo();
        pi.packageName = p.packageName;
        pi.splitNames = p.splitNames;
        pi.versionCode = p.mVersionCode;
        pi.versionCodeMajor = p.mVersionCodeMajor;
        pi.baseRevisionCode = p.baseRevisionCode;
        pi.splitRevisionCodes = p.splitRevisionCodes;
        pi.versionName = p.mVersionName;
        pi.sharedUserId = p.mSharedUserId;
        pi.sharedUserLabel = p.mSharedUserLabel;
        pi.applicationInfo = android.content.pm.PackageParser.generateApplicationInfo(p, flags, state, userId);
        pi.installLocation = p.installLocation;
        pi.isStub = p.isStub;
        pi.coreApp = p.coreApp;
        if (((pi.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) || ((pi.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)) {
            pi.requiredForAllUsers = p.mRequiredForAllUsers;
        }
        pi.restrictedAccountType = p.mRestrictedAccountType;
        pi.requiredAccountType = p.mRequiredAccountType;
        pi.overlayTarget = p.mOverlayTarget;
        pi.targetOverlayableName = p.mOverlayTargetName;
        pi.overlayCategory = p.mOverlayCategory;
        pi.overlayPriority = p.mOverlayPriority;
        pi.mOverlayIsStatic = p.mOverlayIsStatic;
        pi.compileSdkVersion = p.mCompileSdkVersion;
        pi.compileSdkVersionCodename = p.mCompileSdkVersionCodename;
        pi.firstInstallTime = firstInstallTime;
        pi.lastUpdateTime = lastUpdateTime;
        if ((flags & android.content.pm.PackageManager.GET_GIDS) != 0) {
            pi.gids = gids;
        }
        if ((flags & android.content.pm.PackageManager.GET_CONFIGURATIONS) != 0) {
            int N = (p.configPreferences != null) ? p.configPreferences.size() : 0;
            if (N > 0) {
                pi.configPreferences = new android.content.pm.ConfigurationInfo[N];
                p.configPreferences.toArray(pi.configPreferences);
            }
            N = (p.reqFeatures != null) ? p.reqFeatures.size() : 0;
            if (N > 0) {
                pi.reqFeatures = new android.content.pm.FeatureInfo[N];
                p.reqFeatures.toArray(pi.reqFeatures);
            }
            N = (p.featureGroups != null) ? p.featureGroups.size() : 0;
            if (N > 0) {
                pi.featureGroups = new android.content.pm.FeatureGroupInfo[N];
                p.featureGroups.toArray(pi.featureGroups);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_ACTIVITIES) != 0) {
            final int N = p.activities.size();
            if (N > 0) {
                int num = 0;
                final android.content.pm.ActivityInfo[] res = new android.content.pm.ActivityInfo[N];
                for (int i = 0; i < N; i++) {
                    final android.content.pm.PackageParser.Activity a = p.activities.get(i);
                    if (state.isMatch(a.info, flags)) {
                        if (android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(a.className)) {
                            continue;
                        }
                        res[num++] = android.content.pm.PackageParser.generateActivityInfo(a, flags, state, userId);
                    }
                }
                pi.activities = com.android.internal.util.ArrayUtils.trimToSize(res, num);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_RECEIVERS) != 0) {
            final int N = p.receivers.size();
            if (N > 0) {
                int num = 0;
                final android.content.pm.ActivityInfo[] res = new android.content.pm.ActivityInfo[N];
                for (int i = 0; i < N; i++) {
                    final android.content.pm.PackageParser.Activity a = p.receivers.get(i);
                    if (state.isMatch(a.info, flags)) {
                        res[num++] = android.content.pm.PackageParser.generateActivityInfo(a, flags, state, userId);
                    }
                }
                pi.receivers = com.android.internal.util.ArrayUtils.trimToSize(res, num);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_SERVICES) != 0) {
            final int N = p.services.size();
            if (N > 0) {
                int num = 0;
                final android.content.pm.ServiceInfo[] res = new android.content.pm.ServiceInfo[N];
                for (int i = 0; i < N; i++) {
                    final android.content.pm.PackageParser.Service s = p.services.get(i);
                    if (state.isMatch(s.info, flags)) {
                        res[num++] = android.content.pm.PackageParser.generateServiceInfo(s, flags, state, userId);
                    }
                }
                pi.services = com.android.internal.util.ArrayUtils.trimToSize(res, num);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_PROVIDERS) != 0) {
            final int N = p.providers.size();
            if (N > 0) {
                int num = 0;
                final android.content.pm.ProviderInfo[] res = new android.content.pm.ProviderInfo[N];
                for (int i = 0; i < N; i++) {
                    final android.content.pm.PackageParser.Provider pr = p.providers.get(i);
                    if (state.isMatch(pr.info, flags)) {
                        res[num++] = android.content.pm.PackageParser.generateProviderInfo(pr, flags, state, userId);
                    }
                }
                pi.providers = com.android.internal.util.ArrayUtils.trimToSize(res, num);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_INSTRUMENTATION) != 0) {
            int N = p.instrumentation.size();
            if (N > 0) {
                pi.instrumentation = new android.content.pm.InstrumentationInfo[N];
                for (int i = 0; i < N; i++) {
                    pi.instrumentation[i] = android.content.pm.PackageParser.generateInstrumentationInfo(p.instrumentation.get(i), flags);
                }
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_PERMISSIONS) != 0) {
            int N = p.permissions.size();
            if (N > 0) {
                pi.permissions = new android.content.pm.PermissionInfo[N];
                for (int i = 0; i < N; i++) {
                    pi.permissions[i] = android.content.pm.PackageParser.generatePermissionInfo(p.permissions.get(i), flags);
                }
            }
            N = p.requestedPermissions.size();
            if (N > 0) {
                pi.requestedPermissions = new java.lang.String[N];
                pi.requestedPermissionsFlags = new int[N];
                for (int i = 0; i < N; i++) {
                    final java.lang.String perm = p.requestedPermissions.get(i);
                    pi.requestedPermissions[i] = perm;
                    // The notion of required permissions is deprecated but for compatibility.
                    pi.requestedPermissionsFlags[i] |= android.content.pm.PackageInfo.REQUESTED_PERMISSION_REQUIRED;
                    if ((grantedPermissions != null) && grantedPermissions.contains(perm)) {
                        pi.requestedPermissionsFlags[i] |= android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED;
                    }
                }
            }
        }
        // deprecated method of getting signing certificates
        if ((flags & android.content.pm.PackageManager.GET_SIGNATURES) != 0) {
            if (p.mSigningDetails.hasPastSigningCertificates()) {
                // Package has included signing certificate rotation information.  Return the oldest
                // cert so that programmatic checks keep working even if unaware of key rotation.
                pi.signatures = new android.content.pm.Signature[1];
                pi.signatures[0] = p.mSigningDetails.pastSigningCertificates[0];
            } else
                if (p.mSigningDetails.hasSignatures()) {
                    // otherwise keep old behavior
                    int numberOfSigs = p.mSigningDetails.signatures.length;
                    pi.signatures = new android.content.pm.Signature[numberOfSigs];
                    java.lang.System.arraycopy(p.mSigningDetails.signatures, 0, pi.signatures, 0, numberOfSigs);
                }

        }
        // replacement for GET_SIGNATURES
        if ((flags & android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES) != 0) {
            if (p.mSigningDetails != android.content.pm.PackageParser.SigningDetails.UNKNOWN) {
                // only return a valid SigningInfo if there is signing information to report
                pi.signingInfo = new android.content.pm.SigningInfo(p.mSigningDetails);
            } else {
                pi.signingInfo = null;
            }
        }
        return pi;
    }

    public static final int PARSE_MUST_BE_APK = 1 << 0;

    public static final int PARSE_IGNORE_PROCESSES = 1 << 1;

    public static final int PARSE_EXTERNAL_STORAGE = 1 << 3;

    public static final int PARSE_IS_SYSTEM_DIR = 1 << 4;

    public static final int PARSE_COLLECT_CERTIFICATES = 1 << 5;

    public static final int PARSE_ENFORCE_CODE = 1 << 6;

    public static final int PARSE_CHATTY = 1 << 31;

    @android.annotation.IntDef(flag = true, prefix = { "PARSE_" }, value = { android.content.pm.PackageParser.PARSE_CHATTY, android.content.pm.PackageParser.PARSE_COLLECT_CERTIFICATES, android.content.pm.PackageParser.PARSE_ENFORCE_CODE, android.content.pm.PackageParser.PARSE_EXTERNAL_STORAGE, android.content.pm.PackageParser.PARSE_IGNORE_PROCESSES, android.content.pm.PackageParser.PARSE_IS_SYSTEM_DIR, android.content.pm.PackageParser.PARSE_MUST_BE_APK })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ParseFlags {}

    private static final java.util.Comparator<java.lang.String> sSplitNameComparator = new android.content.pm.PackageParser.SplitNameComparator();

    /**
     * Used to sort a set of APKs based on their split names, always placing the
     * base APK (with {@code null} split name) first.
     */
    private static class SplitNameComparator implements java.util.Comparator<java.lang.String> {
        @java.lang.Override
        public int compare(java.lang.String lhs, java.lang.String rhs) {
            if (lhs == null) {
                return -1;
            } else
                if (rhs == null) {
                    return 1;
                } else {
                    return lhs.compareTo(rhs);
                }

        }
    }

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
    @android.annotation.UnsupportedAppUsage
    public static android.content.pm.PackageParser.PackageLite parsePackageLite(java.io.File packageFile, int flags) throws android.content.pm.PackageParser.PackageParserException {
        if (packageFile.isDirectory()) {
            return android.content.pm.PackageParser.parseClusterPackageLite(packageFile, flags);
        } else {
            return android.content.pm.PackageParser.parseMonolithicPackageLite(packageFile, flags);
        }
    }

    private static android.content.pm.PackageParser.PackageLite parseMonolithicPackageLite(java.io.File packageFile, int flags) throws android.content.pm.PackageParser.PackageParserException {
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER, "parseApkLite");
        final android.content.pm.PackageParser.ApkLite baseApk = android.content.pm.PackageParser.parseApkLite(packageFile, flags);
        final java.lang.String packagePath = packageFile.getAbsolutePath();
        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER);
        return new android.content.pm.PackageParser.PackageLite(packagePath, baseApk, null, null, null, null, null, null);
    }

    static android.content.pm.PackageParser.PackageLite parseClusterPackageLite(java.io.File packageDir, int flags) throws android.content.pm.PackageParser.PackageParserException {
        final java.io.File[] files = packageDir.listFiles();
        if (com.android.internal.util.ArrayUtils.isEmpty(files)) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NOT_APK, "No packages found in split");
        }
        java.lang.String packageName = null;
        int versionCode = 0;
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER, "parseApkLite");
        final android.util.ArrayMap<java.lang.String, android.content.pm.PackageParser.ApkLite> apks = new android.util.ArrayMap();
        for (java.io.File file : files) {
            if (android.content.pm.PackageParser.isApkFile(file)) {
                final android.content.pm.PackageParser.ApkLite lite = android.content.pm.PackageParser.parseApkLite(file, flags);
                // Assert that all package names and version codes are
                // consistent with the first one we encounter.
                if (packageName == null) {
                    packageName = lite.packageName;
                    versionCode = lite.versionCode;
                } else {
                    if (!packageName.equals(lite.packageName)) {
                        throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, (((("Inconsistent package " + lite.packageName) + " in ") + file) + "; expected ") + packageName);
                    }
                    if (versionCode != lite.versionCode) {
                        throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, (((("Inconsistent version " + lite.versionCode) + " in ") + file) + "; expected ") + versionCode);
                    }
                }
                // Assert that each split is defined only once
                if (apks.put(lite.splitName, lite) != null) {
                    throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, (("Split name " + lite.splitName) + " defined more than once; most recent was ") + file);
                }
            }
        }
        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER);
        final android.content.pm.PackageParser.ApkLite baseApk = apks.remove(null);
        if (baseApk == null) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Missing base APK in " + packageDir);
        }
        // Always apply deterministic ordering based on splitName
        final int size = apks.size();
        java.lang.String[] splitNames = null;
        boolean[] isFeatureSplits = null;
        java.lang.String[] usesSplitNames = null;
        java.lang.String[] configForSplits = null;
        java.lang.String[] splitCodePaths = null;
        int[] splitRevisionCodes = null;
        java.lang.String[] splitClassLoaderNames = null;
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
        return new android.content.pm.PackageParser.PackageLite(codePath, baseApk, splitNames, isFeatureSplits, usesSplitNames, configForSplits, splitCodePaths, splitRevisionCodes);
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
     * must be done separately in {@link #collectCertificates(Package, int)}.
     *
     * If {@code useCaches} is true, the package parser might return a cached
     * result from a previous parse of the same {@code packageFile} with the same
     * {@code flags}. Note that this method does not check whether {@code packageFile}
     * has changed since the last parse, it's up to callers to do so.
     *
     * @see #parsePackageLite(File, int)
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.pm.PackageParser.Package parsePackage(java.io.File packageFile, int flags, boolean useCaches) throws android.content.pm.PackageParser.PackageParserException {
        android.content.pm.PackageParser.Package parsed = (useCaches) ? getCachedResult(packageFile, flags) : null;
        if (parsed != null) {
            return parsed;
        }
        long parseTime = (android.content.pm.PackageParser.LOG_PARSE_TIMINGS) ? android.os.SystemClock.uptimeMillis() : 0;
        if (packageFile.isDirectory()) {
            parsed = parseClusterPackage(packageFile, flags);
        } else {
            parsed = parseMonolithicPackage(packageFile, flags);
        }
        long cacheTime = (android.content.pm.PackageParser.LOG_PARSE_TIMINGS) ? android.os.SystemClock.uptimeMillis() : 0;
        cacheResult(packageFile, flags, parsed);
        if (android.content.pm.PackageParser.LOG_PARSE_TIMINGS) {
            parseTime = cacheTime - parseTime;
            cacheTime = android.os.SystemClock.uptimeMillis() - cacheTime;
            if ((parseTime + cacheTime) > android.content.pm.PackageParser.LOG_PARSE_TIMINGS_THRESHOLD_MS) {
                android.util.Slog.i(android.content.pm.PackageParser.TAG, ((((("Parse times for '" + packageFile) + "': parse=") + parseTime) + "ms, update_cache=") + cacheTime) + " ms");
            }
        }
        return parsed;
    }

    /**
     * Equivalent to {@link #parsePackage(File, int, boolean)} with {@code useCaches == false}.
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.pm.PackageParser.Package parsePackage(java.io.File packageFile, int flags) throws android.content.pm.PackageParser.PackageParserException {
        return /* useCaches */
        parsePackage(packageFile, flags, false);
    }

    /**
     * Returns the cache key for a specificied {@code packageFile} and {@code flags}.
     */
    private java.lang.String getCacheKey(java.io.File packageFile, int flags) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(packageFile.getName());
        sb.append('-');
        sb.append(flags);
        return sb.toString();
    }

    @com.android.internal.annotations.VisibleForTesting
    protected android.content.pm.PackageParser.Package fromCacheEntry(byte[] bytes) {
        return android.content.pm.PackageParser.fromCacheEntryStatic(bytes);
    }

    /**
     * static version of {@link #fromCacheEntry} for unit tests.
     */
    @com.android.internal.annotations.VisibleForTesting
    public static android.content.pm.PackageParser.Package fromCacheEntryStatic(byte[] bytes) {
        final android.os.Parcel p = android.os.Parcel.obtain();
        p.unmarshall(bytes, 0, bytes.length);
        p.setDataPosition(0);
        final android.content.pm.PackageParserCacheHelper.ReadHelper helper = new android.content.pm.PackageParserCacheHelper.ReadHelper(p);
        helper.startAndInstall();
        android.content.pm.PackageParser.Package pkg = new android.content.pm.PackageParser.Package(p);
        p.recycle();
        android.content.pm.PackageParser.sCachedPackageReadCount.incrementAndGet();
        return pkg;
    }

    @com.android.internal.annotations.VisibleForTesting
    protected byte[] toCacheEntry(android.content.pm.PackageParser.Package pkg) {
        return android.content.pm.PackageParser.toCacheEntryStatic(pkg);
    }

    /**
     * static version of {@link #toCacheEntry} for unit tests.
     */
    @com.android.internal.annotations.VisibleForTesting
    public static byte[] toCacheEntryStatic(android.content.pm.PackageParser.Package pkg) {
        final android.os.Parcel p = android.os.Parcel.obtain();
        final android.content.pm.PackageParserCacheHelper.WriteHelper helper = new android.content.pm.PackageParserCacheHelper.WriteHelper(p);
        /* flags */
        pkg.writeToParcel(p, 0);
        helper.finishAndUninstall();
        byte[] serialized = p.marshall();
        p.recycle();
        return serialized;
    }

    /**
     * Given a {@code packageFile} and a {@code cacheFile} returns whether the
     * cache file is up to date based on the mod-time of both files.
     */
    private static boolean isCacheUpToDate(java.io.File packageFile, java.io.File cacheFile) {
        try {
            // NOTE: We don't use the File.lastModified API because it has the very
            // non-ideal failure mode of returning 0 with no excepions thrown.
            // The nio2 Files API is a little better but is considerably more expensive.
            final android.system.StructStat pkg = android.system.Os.stat(packageFile.getAbsolutePath());
            final android.system.StructStat cache = android.system.Os.stat(cacheFile.getAbsolutePath());
            return pkg.st_mtime < cache.st_mtime;
        } catch (android.system.ErrnoException ee) {
            // The most common reason why stat fails is that a given cache file doesn't
            // exist. We ignore that here. It's easy to reason that it's safe to say the
            // cache isn't up to date if we see any sort of exception here.
            // 
            // (1) Exception while stating the package file : This should never happen,
            // and if it does, we do a full package parse (which is likely to throw the
            // same exception).
            // (2) Exception while stating the cache file : If the file doesn't exist, the
            // cache is obviously out of date. If the file *does* exist, we can't read it.
            // We will attempt to delete and recreate it after parsing the package.
            if (ee.errno != android.system.OsConstants.ENOENT) {
                android.util.Slog.w("Error while stating package cache : ", ee);
            }
            return false;
        }
    }

    /**
     * Returns the cached parse result for {@code packageFile} for parse flags {@code flags},
     * or {@code null} if no cached result exists.
     */
    private android.content.pm.PackageParser.Package getCachedResult(java.io.File packageFile, int flags) {
        if (mCacheDir == null) {
            return null;
        }
        final java.lang.String cacheKey = getCacheKey(packageFile, flags);
        final java.io.File cacheFile = new java.io.File(mCacheDir, cacheKey);
        try {
            // If the cache is not up to date, return null.
            if (!android.content.pm.PackageParser.isCacheUpToDate(packageFile, cacheFile)) {
                return null;
            }
            final byte[] bytes = libcore.io.IoUtils.readFileAsByteArray(cacheFile.getAbsolutePath());
            android.content.pm.PackageParser.Package p = fromCacheEntry(bytes);
            if (mCallback != null) {
                java.lang.String[] overlayApks = mCallback.getOverlayApks(p.packageName);
                if ((overlayApks != null) && (overlayApks.length > 0)) {
                    for (java.lang.String overlayApk : overlayApks) {
                        // If a static RRO is updated, return null.
                        if (!android.content.pm.PackageParser.isCacheUpToDate(new java.io.File(overlayApk), cacheFile)) {
                            return null;
                        }
                    }
                }
            }
            return p;
        } catch (java.lang.Throwable e) {
            android.util.Slog.w(android.content.pm.PackageParser.TAG, "Error reading package cache: ", e);
            // If something went wrong while reading the cache entry, delete the cache file
            // so that we regenerate it the next time.
            cacheFile.delete();
            return null;
        }
    }

    /**
     * Caches the parse result for {@code packageFile} with flags {@code flags}.
     */
    private void cacheResult(java.io.File packageFile, int flags, android.content.pm.PackageParser.Package parsed) {
        if (mCacheDir == null) {
            return;
        }
        try {
            final java.lang.String cacheKey = getCacheKey(packageFile, flags);
            final java.io.File cacheFile = new java.io.File(mCacheDir, cacheKey);
            if (cacheFile.exists()) {
                if (!cacheFile.delete()) {
                    android.util.Slog.e(android.content.pm.PackageParser.TAG, "Unable to delete cache file: " + cacheFile);
                }
            }
            final byte[] cacheEntry = toCacheEntry(parsed);
            if (cacheEntry == null) {
                return;
            }
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(cacheFile)) {
                fos.write(cacheEntry);
            } catch (java.io.IOException ioe) {
                android.util.Slog.w(android.content.pm.PackageParser.TAG, "Error writing cache entry.", ioe);
                cacheFile.delete();
            }
        } catch (java.lang.Throwable e) {
            android.util.Slog.w(android.content.pm.PackageParser.TAG, "Error saving package cache.", e);
        }
    }

    /**
     * Parse all APKs contained in the given directory, treating them as a
     * single package. This also performs sanity checking, such as requiring
     * identical package name and version codes, a single base APK, and unique
     * split names.
     * <p>
     * Note that this <em>does not</em> perform signature verification; that
     * must be done separately in {@link #collectCertificates(Package, int)}.
     */
    private android.content.pm.PackageParser.Package parseClusterPackage(java.io.File packageDir, int flags) throws android.content.pm.PackageParser.PackageParserException {
        final android.content.pm.PackageParser.PackageLite lite = android.content.pm.PackageParser.parseClusterPackageLite(packageDir, 0);
        if (mOnlyCoreApps && (!lite.coreApp)) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Not a coreApp: " + packageDir);
        }
        // Build the split dependency tree.
        android.util.SparseArray<int[]> splitDependencies = null;
        final android.content.pm.split.SplitAssetLoader assetLoader;
        if (lite.isolatedSplits && (!com.android.internal.util.ArrayUtils.isEmpty(lite.splitNames))) {
            try {
                splitDependencies = android.content.pm.split.SplitAssetDependencyLoader.createDependenciesFromPackage(lite);
                assetLoader = new android.content.pm.split.SplitAssetDependencyLoader(lite, splitDependencies, flags);
            } catch (android.content.pm.split.SplitDependencyLoader.IllegalDependencyException e) {
                throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, e.getMessage());
            }
        } else {
            assetLoader = new android.content.pm.split.DefaultSplitAssetLoader(lite, flags);
        }
        try {
            final android.content.res.AssetManager assets = assetLoader.getBaseAssetManager();
            final java.io.File baseApk = new java.io.File(lite.baseCodePath);
            final android.content.pm.PackageParser.Package pkg = parseBaseApk(baseApk, assets, flags);
            if (pkg == null) {
                throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NOT_APK, "Failed to parse base APK: " + baseApk);
            }
            if (!com.android.internal.util.ArrayUtils.isEmpty(lite.splitNames)) {
                final int num = lite.splitNames.length;
                pkg.splitNames = lite.splitNames;
                pkg.splitCodePaths = lite.splitCodePaths;
                pkg.splitRevisionCodes = lite.splitRevisionCodes;
                pkg.splitFlags = new int[num];
                pkg.splitPrivateFlags = new int[num];
                pkg.applicationInfo.splitNames = pkg.splitNames;
                pkg.applicationInfo.splitDependencies = splitDependencies;
                pkg.applicationInfo.splitClassLoaderNames = new java.lang.String[num];
                for (int i = 0; i < num; i++) {
                    final android.content.res.AssetManager splitAssets = assetLoader.getSplitAssetManager(i);
                    parseSplitApk(pkg, i, splitAssets, flags);
                }
            }
            pkg.setCodePath(packageDir.getCanonicalPath());
            pkg.setUse32bitAbi(lite.use32bitAbi);
            return pkg;
        } catch (java.io.IOException e) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to get path: " + lite.baseCodePath, e);
        } finally {
            libcore.io.IoUtils.closeQuietly(assetLoader);
        }
    }

    /**
     * Parse the given APK file, treating it as as a single monolithic package.
     * <p>
     * Note that this <em>does not</em> perform signature verification; that
     * must be done separately in {@link #collectCertificates(Package, int)}.
     *
     * @deprecated external callers should move to
    {@link #parsePackage(File, int)}. Eventually this method will
    be marked private.
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public android.content.pm.PackageParser.Package parseMonolithicPackage(java.io.File apkFile, int flags) throws android.content.pm.PackageParser.PackageParserException {
        final android.content.pm.PackageParser.PackageLite lite = android.content.pm.PackageParser.parseMonolithicPackageLite(apkFile, flags);
        if (mOnlyCoreApps) {
            if (!lite.coreApp) {
                throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Not a coreApp: " + apkFile);
            }
        }
        final android.content.pm.split.SplitAssetLoader assetLoader = new android.content.pm.split.DefaultSplitAssetLoader(lite, flags);
        try {
            final android.content.pm.PackageParser.Package pkg = parseBaseApk(apkFile, assetLoader.getBaseAssetManager(), flags);
            pkg.setCodePath(apkFile.getCanonicalPath());
            pkg.setUse32bitAbi(lite.use32bitAbi);
            return pkg;
        } catch (java.io.IOException e) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to get path: " + apkFile, e);
        } finally {
            libcore.io.IoUtils.closeQuietly(assetLoader);
        }
    }

    private android.content.pm.PackageParser.Package parseBaseApk(java.io.File apkFile, android.content.res.AssetManager assets, int flags) throws android.content.pm.PackageParser.PackageParserException {
        final java.lang.String apkPath = apkFile.getAbsolutePath();
        java.lang.String volumeUuid = null;
        if (apkPath.startsWith(android.content.pm.PackageParser.MNT_EXPAND)) {
            final int end = apkPath.indexOf('/', android.content.pm.PackageParser.MNT_EXPAND.length());
            volumeUuid = apkPath.substring(android.content.pm.PackageParser.MNT_EXPAND.length(), end);
        }
        mParseError = android.content.pm.PackageManager.INSTALL_SUCCEEDED;
        mArchiveSourcePath = apkFile.getAbsolutePath();
        if (android.content.pm.PackageParser.DEBUG_JAR)
            android.util.Slog.d(android.content.pm.PackageParser.TAG, "Scanning base APK: " + apkPath);

        android.content.res.XmlResourceParser parser = null;
        try {
            final int cookie = assets.findCookieForPath(apkPath);
            if (cookie == 0) {
                throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Failed adding asset path: " + apkPath);
            }
            parser = assets.openXmlResourceParser(cookie, android.content.pm.PackageParser.ANDROID_MANIFEST_FILENAME);
            final android.content.res.Resources res = new android.content.res.Resources(assets, mMetrics, null);
            final java.lang.String[] outError = new java.lang.String[1];
            final android.content.pm.PackageParser.Package pkg = parseBaseApk(apkPath, res, parser, flags, outError);
            if (pkg == null) {
                throw new android.content.pm.PackageParser.PackageParserException(mParseError, (((apkPath + " (at ") + parser.getPositionDescription()) + "): ") + outError[0]);
            }
            pkg.setVolumeUuid(volumeUuid);
            pkg.setApplicationVolumeUuid(volumeUuid);
            pkg.setBaseCodePath(apkPath);
            pkg.setSigningDetails(android.content.pm.PackageParser.SigningDetails.UNKNOWN);
            return pkg;
        } catch (android.content.pm.PackageParser.PackageParserException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e);
        } finally {
            libcore.io.IoUtils.closeQuietly(parser);
        }
    }

    private void parseSplitApk(android.content.pm.PackageParser.Package pkg, int splitIndex, android.content.res.AssetManager assets, int flags) throws android.content.pm.PackageParser.PackageParserException {
        final java.lang.String apkPath = pkg.splitCodePaths[splitIndex];
        mParseError = android.content.pm.PackageManager.INSTALL_SUCCEEDED;
        mArchiveSourcePath = apkPath;
        if (android.content.pm.PackageParser.DEBUG_JAR)
            android.util.Slog.d(android.content.pm.PackageParser.TAG, "Scanning split APK: " + apkPath);

        final android.content.res.Resources res;
        android.content.res.XmlResourceParser parser = null;
        try {
            // This must always succeed, as the path has been added to the AssetManager before.
            final int cookie = assets.findCookieForPath(apkPath);
            if (cookie == 0) {
                throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Failed adding asset path: " + apkPath);
            }
            parser = assets.openXmlResourceParser(cookie, android.content.pm.PackageParser.ANDROID_MANIFEST_FILENAME);
            res = new android.content.res.Resources(assets, mMetrics, null);
            final java.lang.String[] outError = new java.lang.String[1];
            pkg = parseSplitApk(pkg, res, parser, flags, splitIndex, outError);
            if (pkg == null) {
                throw new android.content.pm.PackageParser.PackageParserException(mParseError, (((apkPath + " (at ") + parser.getPositionDescription()) + "): ") + outError[0]);
            }
        } catch (android.content.pm.PackageParser.PackageParserException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e);
        } finally {
            libcore.io.IoUtils.closeQuietly(parser);
        }
    }

    /**
     * Parse the manifest of a <em>split APK</em>.
     * <p>
     * Note that split APKs have many more restrictions on what they're capable
     * of doing, so many valid features of a base APK have been carefully
     * omitted here.
     */
    private android.content.pm.PackageParser.Package parseSplitApk(android.content.pm.PackageParser.Package pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, int splitIndex, java.lang.String[] outError) throws android.content.pm.PackageParser.PackageParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.util.AttributeSet attrs = parser;
        // We parsed manifest tag earlier; just skip past it
        android.content.pm.PackageParser.parsePackageSplitNames(parser, attrs);
        mParseInstrumentationArgs = null;
        int type;
        boolean foundApp = false;
        int outerDepth = parser.getDepth();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            java.lang.String tagName = parser.getName();
            if (tagName.equals(android.content.pm.PackageParser.TAG_APPLICATION)) {
                if (foundApp) {
                    if (android.content.pm.PackageParser.RIGID_PARSER) {
                        outError[0] = "<manifest> has more than one <application>";
                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return null;
                    } else {
                        android.util.Slog.w(android.content.pm.PackageParser.TAG, "<manifest> has more than one <application>");
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        continue;
                    }
                }
                foundApp = true;
                if (!parseSplitApplication(pkg, res, parser, flags, splitIndex, outError)) {
                    return null;
                }
            } else
                if (android.content.pm.PackageParser.RIGID_PARSER) {
                    outError[0] = "Bad element under <manifest>: " + parser.getName();
                    mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    return null;
                } else {
                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <manifest>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    continue;
                }

        } 
        if (!foundApp) {
            outError[0] = "<manifest> does not contain an <application>";
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
        }
        return pkg;
    }

    /**
     * Parses the public keys from the set of signatures.
     */
    public static android.util.ArraySet<java.security.PublicKey> toSigningKeys(android.content.pm.Signature[] signatures) throws java.security.cert.CertificateException {
        android.util.ArraySet<java.security.PublicKey> keys = new android.util.ArraySet(signatures.length);
        for (int i = 0; i < signatures.length; i++) {
            keys.add(signatures[i].getPublicKey());
        }
        return keys;
    }

    /**
     * Collect certificates from all the APKs described in the given package,
     * populating {@link Package#mSigningDetails}. Also asserts that all APK
     * contents are signed correctly and consistently.
     */
    @android.annotation.UnsupportedAppUsage
    public static void collectCertificates(android.content.pm.PackageParser.Package pkg, boolean skipVerify) throws android.content.pm.PackageParser.PackageParserException {
        android.content.pm.PackageParser.collectCertificatesInternal(pkg, skipVerify);
        final int childCount = (pkg.childPackages != null) ? pkg.childPackages.size() : 0;
        for (int i = 0; i < childCount; i++) {
            android.content.pm.PackageParser.Package childPkg = pkg.childPackages.get(i);
            childPkg.mSigningDetails = pkg.mSigningDetails;
        }
    }

    private static void collectCertificatesInternal(android.content.pm.PackageParser.Package pkg, boolean skipVerify) throws android.content.pm.PackageParser.PackageParserException {
        pkg.mSigningDetails = android.content.pm.PackageParser.SigningDetails.UNKNOWN;
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER, "collectCertificates");
        try {
            android.content.pm.PackageParser.collectCertificates(pkg, new java.io.File(pkg.baseCodePath), skipVerify);
            if (!com.android.internal.util.ArrayUtils.isEmpty(pkg.splitCodePaths)) {
                for (int i = 0; i < pkg.splitCodePaths.length; i++) {
                    android.content.pm.PackageParser.collectCertificates(pkg, new java.io.File(pkg.splitCodePaths[i]), skipVerify);
                }
            }
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER);
        }
    }

    @android.annotation.UnsupportedAppUsage
    private static void collectCertificates(android.content.pm.PackageParser.Package pkg, java.io.File apkFile, boolean skipVerify) throws android.content.pm.PackageParser.PackageParserException {
        final java.lang.String apkPath = apkFile.getAbsolutePath();
        int minSignatureScheme = android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.JAR;
        if (pkg.applicationInfo.isStaticSharedLibrary()) {
            // must use v2 signing scheme
            minSignatureScheme = android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.SIGNING_BLOCK_V2;
        }
        android.content.pm.PackageParser.SigningDetails verified;
        if (skipVerify) {
            // systemDir APKs are already trusted, save time by not verifying
            verified = android.util.apk.ApkSignatureVerifier.unsafeGetCertsWithoutVerification(apkPath, minSignatureScheme);
        } else {
            verified = android.util.apk.ApkSignatureVerifier.verify(apkPath, minSignatureScheme);
        }
        // Verify that entries are signed consistently with the first pkg
        // we encountered. Note that for splits, certificates may have
        // already been populated during an earlier parse of a base APK.
        if (pkg.mSigningDetails == android.content.pm.PackageParser.SigningDetails.UNKNOWN) {
            pkg.mSigningDetails = verified;
        } else {
            if (!android.content.pm.Signature.areExactMatch(pkg.mSigningDetails.signatures, verified.signatures)) {
                throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES, apkPath + " has mismatched certificates");
            }
        }
    }

    private static android.content.res.AssetManager newConfiguredAssetManager() {
        android.content.res.AssetManager assetManager = new android.content.res.AssetManager();
        assetManager.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
        return assetManager;
    }

    /**
     * Utility method that retrieves lightweight details about a single APK
     * file, including package name, split name, and install location.
     *
     * @param apkFile
     * 		path to a single APK
     * @param flags
     * 		optional parse flags, such as
     * 		{@link #PARSE_COLLECT_CERTIFICATES}
     */
    public static android.content.pm.PackageParser.ApkLite parseApkLite(java.io.File apkFile, int flags) throws android.content.pm.PackageParser.PackageParserException {
        return android.content.pm.PackageParser.parseApkLiteInner(apkFile, null, null, flags);
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
     * 		{@link #PARSE_COLLECT_CERTIFICATES}
     */
    public static android.content.pm.PackageParser.ApkLite parseApkLite(java.io.FileDescriptor fd, java.lang.String debugPathName, int flags) throws android.content.pm.PackageParser.PackageParserException {
        return android.content.pm.PackageParser.parseApkLiteInner(null, fd, debugPathName, flags);
    }

    private static android.content.pm.PackageParser.ApkLite parseApkLiteInner(java.io.File apkFile, java.io.FileDescriptor fd, java.lang.String debugPathName, int flags) throws android.content.pm.PackageParser.PackageParserException {
        final java.lang.String apkPath = (fd != null) ? debugPathName : apkFile.getAbsolutePath();
        android.content.res.XmlResourceParser parser = null;
        android.content.res.ApkAssets apkAssets = null;
        try {
            try {
                apkAssets = (fd != null) ? android.content.res.ApkAssets.loadFromFd(fd, debugPathName, false, false) : android.content.res.ApkAssets.loadFromPath(apkPath);
            } catch (java.io.IOException e) {
                throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NOT_APK, "Failed to parse " + apkPath);
            }
            parser = apkAssets.openXml(android.content.pm.PackageParser.ANDROID_MANIFEST_FILENAME);
            final android.content.pm.PackageParser.SigningDetails signingDetails;
            if ((flags & android.content.pm.PackageParser.PARSE_COLLECT_CERTIFICATES) != 0) {
                // TODO: factor signature related items out of Package object
                final android.content.pm.PackageParser.Package tempPkg = new android.content.pm.PackageParser.Package(((java.lang.String) (null)));
                final boolean skipVerify = (flags & android.content.pm.PackageParser.PARSE_IS_SYSTEM_DIR) != 0;
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER, "collectCertificates");
                try {
                    android.content.pm.PackageParser.collectCertificates(tempPkg, apkFile, skipVerify);
                } finally {
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_PACKAGE_MANAGER);
                }
                signingDetails = tempPkg.mSigningDetails;
            } else {
                signingDetails = android.content.pm.PackageParser.SigningDetails.UNKNOWN;
            }
            final android.util.AttributeSet attrs = parser;
            return android.content.pm.PackageParser.parseApkLite(apkPath, parser, attrs, signingDetails);
        } catch (org.xmlpull.v1.XmlPullParserException | java.io.IOException | java.lang.RuntimeException e) {
            android.util.Slog.w(android.content.pm.PackageParser.TAG, "Failed to parse " + apkPath, e);
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e);
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

    private static java.lang.String validateName(java.lang.String name, boolean requireSeparator, boolean requireFilename) {
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
            return ("bad character '" + c) + "'";
        }
        if (requireFilename && (!android.os.FileUtils.isValidExtFilename(name))) {
            return "Invalid filename";
        }
        return hasSep || (!requireSeparator) ? null : "must have at least one '.' separator";
    }

    private static android.util.Pair<java.lang.String, java.lang.String> parsePackageSplitNames(org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs) throws android.content.pm.PackageParser.PackageParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
        } 
        if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No start tag found");
        }
        if (!parser.getName().equals(android.content.pm.PackageParser.TAG_MANIFEST)) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No <manifest> tag");
        }
        final java.lang.String packageName = attrs.getAttributeValue(null, "package");
        if (!"android".equals(packageName)) {
            final java.lang.String error = android.content.pm.PackageParser.validateName(packageName, true, true);
            if (error != null) {
                throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest package: " + error);
            }
        }
        java.lang.String splitName = attrs.getAttributeValue(null, "split");
        if (splitName != null) {
            if (splitName.length() == 0) {
                splitName = null;
            } else {
                final java.lang.String error = android.content.pm.PackageParser.validateName(splitName, false, false);
                if (error != null) {
                    throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest split: " + error);
                }
            }
        }
        return android.util.Pair.create(packageName.intern(), splitName != null ? splitName.intern() : splitName);
    }

    private static android.content.pm.PackageParser.ApkLite parseApkLite(java.lang.String codePath, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.pm.PackageParser.SigningDetails signingDetails) throws android.content.pm.PackageParser.PackageParserException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.util.Pair<java.lang.String, java.lang.String> packageSplit = android.content.pm.PackageParser.parsePackageSplitNames(parser, attrs);
        int installLocation = android.content.pm.PackageParser.PARSE_DEFAULT_INSTALL_LOCATION;
        int versionCode = 0;
        int versionCodeMajor = 0;
        int targetSdkVersion = android.content.pm.PackageParser.DEFAULT_TARGET_SDK_VERSION;
        int minSdkVersion = android.content.pm.PackageParser.DEFAULT_MIN_SDK_VERSION;
        int revisionCode = 0;
        boolean coreApp = false;
        boolean debuggable = false;
        boolean multiArch = false;
        boolean use32bitAbi = false;
        boolean extractNativeLibs = true;
        boolean isolatedSplits = false;
        boolean isFeatureSplit = false;
        boolean isSplitRequired = false;
        boolean useEmbeddedDex = false;
        java.lang.String configForSplit = null;
        java.lang.String usesSplitName = null;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            final java.lang.String attr = attrs.getAttributeName(i);
            if (attr.equals("installLocation")) {
                installLocation = attrs.getAttributeIntValue(i, android.content.pm.PackageParser.PARSE_DEFAULT_INSTALL_LOCATION);
            } else
                if (attr.equals("versionCode")) {
                    versionCode = attrs.getAttributeIntValue(i, 0);
                } else
                    if (attr.equals("versionCodeMajor")) {
                        versionCodeMajor = attrs.getAttributeIntValue(i, 0);
                    } else
                        if (attr.equals("revisionCode")) {
                            revisionCode = attrs.getAttributeIntValue(i, 0);
                        } else
                            if (attr.equals("coreApp")) {
                                coreApp = attrs.getAttributeBooleanValue(i, false);
                            } else
                                if (attr.equals("isolatedSplits")) {
                                    isolatedSplits = attrs.getAttributeBooleanValue(i, false);
                                } else
                                    if (attr.equals("configForSplit")) {
                                        configForSplit = attrs.getAttributeValue(i);
                                    } else
                                        if (attr.equals("isFeatureSplit")) {
                                            isFeatureSplit = attrs.getAttributeBooleanValue(i, false);
                                        } else
                                            if (attr.equals("isSplitRequired")) {
                                                isSplitRequired = attrs.getAttributeBooleanValue(i, false);
                                            }








        }
        // Only search the tree when the tag is the direct child of <manifest> tag
        int type;
        final int searchDepth = parser.getDepth() + 1;
        final java.util.List<android.content.pm.VerifierInfo> verifiers = new java.util.ArrayList<android.content.pm.VerifierInfo>();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() >= searchDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            if (parser.getDepth() != searchDepth) {
                continue;
            }
            if (android.content.pm.PackageParser.TAG_PACKAGE_VERIFIER.equals(parser.getName())) {
                final android.content.pm.VerifierInfo verifier = android.content.pm.PackageParser.parseVerifier(attrs);
                if (verifier != null) {
                    verifiers.add(verifier);
                }
            } else
                if (android.content.pm.PackageParser.TAG_APPLICATION.equals(parser.getName())) {
                    for (int i = 0; i < attrs.getAttributeCount(); ++i) {
                        final java.lang.String attr = attrs.getAttributeName(i);
                        if ("debuggable".equals(attr)) {
                            debuggable = attrs.getAttributeBooleanValue(i, false);
                        }
                        if ("multiArch".equals(attr)) {
                            multiArch = attrs.getAttributeBooleanValue(i, false);
                        }
                        if ("use32bitAbi".equals(attr)) {
                            use32bitAbi = attrs.getAttributeBooleanValue(i, false);
                        }
                        if ("extractNativeLibs".equals(attr)) {
                            extractNativeLibs = attrs.getAttributeBooleanValue(i, true);
                        }
                        if ("useEmbeddedDex".equals(attr)) {
                            useEmbeddedDex = attrs.getAttributeBooleanValue(i, false);
                        }
                    }
                } else
                    if (android.content.pm.PackageParser.TAG_USES_SPLIT.equals(parser.getName())) {
                        if (usesSplitName != null) {
                            android.util.Slog.w(android.content.pm.PackageParser.TAG, "Only one <uses-split> permitted. Ignoring others.");
                            continue;
                        }
                        usesSplitName = attrs.getAttributeValue(android.content.pm.PackageParser.ANDROID_RESOURCES, "name");
                        if (usesSplitName == null) {
                            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<uses-split> tag requires 'android:name' attribute");
                        }
                    } else
                        if (android.content.pm.PackageParser.TAG_USES_SDK.equals(parser.getName())) {
                            for (int i = 0; i < attrs.getAttributeCount(); ++i) {
                                final java.lang.String attr = attrs.getAttributeName(i);
                                if ("targetSdkVersion".equals(attr)) {
                                    targetSdkVersion = attrs.getAttributeIntValue(i, android.content.pm.PackageParser.DEFAULT_TARGET_SDK_VERSION);
                                }
                                if ("minSdkVersion".equals(attr)) {
                                    minSdkVersion = attrs.getAttributeIntValue(i, android.content.pm.PackageParser.DEFAULT_MIN_SDK_VERSION);
                                }
                            }
                        }



        } 
        return new android.content.pm.PackageParser.ApkLite(codePath, packageSplit.first, packageSplit.second, isFeatureSplit, configForSplit, usesSplitName, isSplitRequired, versionCode, versionCodeMajor, revisionCode, installLocation, verifiers, signingDetails, coreApp, debuggable, multiArch, use32bitAbi, useEmbeddedDex, extractNativeLibs, isolatedSplits, minSdkVersion, targetSdkVersion);
    }

    /**
     * Parses a child package and adds it to the parent if successful. If you add
     * new tags that need to be supported by child packages make sure to add them
     * to {@link #CHILD_PACKAGE_TAGS}.
     *
     * @param parentPkg
     * 		The parent that contains the child
     * @param res
     * 		Resources against which to resolve values
     * @param parser
     * 		Parser of the manifest
     * @param flags
     * 		Flags about how to parse
     * @param outError
     * 		Human readable error if parsing fails
     * @return True of parsing succeeded.
     * @throws XmlPullParserException
     * 		
     * @throws IOException
     * 		
     */
    private boolean parseBaseApkChild(android.content.pm.PackageParser.Package parentPkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // Make sure we have a valid child package name
        java.lang.String childPackageName = parser.getAttributeValue(null, "package");
        if (android.content.pm.PackageParser.validateName(childPackageName, true, false) != null) {
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
            return false;
        }
        // Child packages must be unique
        if (childPackageName.equals(parentPkg.packageName)) {
            java.lang.String message = "Child package name cannot be equal to parent package name: " + parentPkg.packageName;
            android.util.Slog.w(android.content.pm.PackageParser.TAG, message);
            outError[0] = message;
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        // Child packages must be unique
        if (parentPkg.hasChildPackage(childPackageName)) {
            java.lang.String message = "Duplicate child package:" + childPackageName;
            android.util.Slog.w(android.content.pm.PackageParser.TAG, message);
            outError[0] = message;
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        // Go ahead and parse the child
        android.content.pm.PackageParser.Package childPkg = new android.content.pm.PackageParser.Package(childPackageName);
        // Child package inherits parent version code/name/target SDK
        childPkg.mVersionCode = parentPkg.mVersionCode;
        childPkg.baseRevisionCode = parentPkg.baseRevisionCode;
        childPkg.mVersionName = parentPkg.mVersionName;
        childPkg.applicationInfo.targetSdkVersion = parentPkg.applicationInfo.targetSdkVersion;
        childPkg.applicationInfo.minSdkVersion = parentPkg.applicationInfo.minSdkVersion;
        childPkg = parseBaseApkCommon(childPkg, android.content.pm.PackageParser.CHILD_PACKAGE_TAGS, res, parser, flags, outError);
        if (childPkg == null) {
            // If we got null then error was set during child parsing
            return false;
        }
        // Set the parent-child relation
        if (parentPkg.childPackages == null) {
            parentPkg.childPackages = new java.util.ArrayList<>();
        }
        parentPkg.childPackages.add(childPkg);
        childPkg.parentPackage = parentPkg;
        return true;
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
     * @param outError
     * 		Human readable error message
     * @return Parsed package or null on error.
     * @throws XmlPullParserException
     * 		
     * @throws IOException
     * 		
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.content.pm.PackageParser.Package parseBaseApk(java.lang.String apkPath, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final java.lang.String splitName;
        final java.lang.String pkgName;
        try {
            android.util.Pair<java.lang.String, java.lang.String> packageSplit = android.content.pm.PackageParser.parsePackageSplitNames(parser, parser);
            pkgName = packageSplit.first;
            splitName = packageSplit.second;
            if (!android.text.TextUtils.isEmpty(splitName)) {
                outError[0] = "Expected base APK, but found split " + splitName;
                mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
                return null;
            }
        } catch (android.content.pm.PackageParser.PackageParserException e) {
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
            return null;
        }
        if (mCallback != null) {
            java.lang.String[] overlayPaths = mCallback.getOverlayPaths(pkgName, apkPath);
            if ((overlayPaths != null) && (overlayPaths.length > 0)) {
                for (java.lang.String overlayPath : overlayPaths) {
                    res.getAssets().addOverlayPath(overlayPath);
                }
            }
        }
        final android.content.pm.PackageParser.Package pkg = new android.content.pm.PackageParser.Package(pkgName);
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifest);
        pkg.mVersionCode = sa.getInteger(com.android.internal.R.styleable.AndroidManifest_versionCode, 0);
        pkg.mVersionCodeMajor = sa.getInteger(com.android.internal.R.styleable.AndroidManifest_versionCodeMajor, 0);
        pkg.applicationInfo.setVersionCode(pkg.getLongVersionCode());
        pkg.baseRevisionCode = sa.getInteger(com.android.internal.R.styleable.AndroidManifest_revisionCode, 0);
        pkg.mVersionName = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifest_versionName, 0);
        if (pkg.mVersionName != null) {
            pkg.mVersionName = pkg.mVersionName.intern();
        }
        pkg.coreApp = parser.getAttributeBooleanValue(null, "coreApp", false);
        pkg.mCompileSdkVersion = sa.getInteger(com.android.internal.R.styleable.AndroidManifest_compileSdkVersion, 0);
        pkg.applicationInfo.compileSdkVersion = pkg.mCompileSdkVersion;
        pkg.mCompileSdkVersionCodename = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifest_compileSdkVersionCodename, 0);
        if (pkg.mCompileSdkVersionCodename != null) {
            pkg.mCompileSdkVersionCodename = pkg.mCompileSdkVersionCodename.intern();
        }
        pkg.applicationInfo.compileSdkVersionCodename = pkg.mCompileSdkVersionCodename;
        sa.recycle();
        return parseBaseApkCommon(pkg, null, res, parser, flags, outError);
    }

    /**
     * This is the common parsing routing for handling parent and child
     * packages in a base APK. The difference between parent and child
     * parsing is that some tags are not supported by child packages as
     * well as some manifest attributes are ignored. The implementation
     * assumes the calling code has already handled the manifest tag if needed
     * (this applies to the parent only).
     *
     * @param pkg
     * 		The package which to populate
     * @param acceptedTags
     * 		Which tags to handle, null to handle all
     * @param res
     * 		Resources against which to resolve values
     * @param parser
     * 		Parser of the manifest
     * @param flags
     * 		Flags about how to parse
     * @param outError
     * 		Human readable error if parsing fails
     * @return The package if parsing succeeded or null.
     * @throws XmlPullParserException
     * 		
     * @throws IOException
     * 		
     */
    private android.content.pm.PackageParser.Package parseBaseApkCommon(android.content.pm.PackageParser.Package pkg, java.util.Set<java.lang.String> acceptedTags, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mParseInstrumentationArgs = null;
        int type;
        boolean foundApp = false;
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifest);
        java.lang.String str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifest_sharedUserId, 0);
        if ((str != null) && (str.length() > 0)) {
            java.lang.String nameError = android.content.pm.PackageParser.validateName(str, true, true);
            if ((nameError != null) && (!"android".equals(pkg.packageName))) {
                outError[0] = (("<manifest> specifies bad sharedUserId name \"" + str) + "\": ") + nameError;
                mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
                return null;
            }
            pkg.mSharedUserId = str.intern();
            pkg.mSharedUserLabel = sa.getResourceId(com.android.internal.R.styleable.AndroidManifest_sharedUserLabel, 0);
        }
        pkg.installLocation = sa.getInteger(com.android.internal.R.styleable.AndroidManifest_installLocation, android.content.pm.PackageParser.PARSE_DEFAULT_INSTALL_LOCATION);
        pkg.applicationInfo.installLocation = pkg.installLocation;
        final int targetSandboxVersion = sa.getInteger(com.android.internal.R.styleable.AndroidManifest_targetSandboxVersion, android.content.pm.PackageParser.PARSE_DEFAULT_TARGET_SANDBOX);
        pkg.applicationInfo.targetSandboxVersion = targetSandboxVersion;
        /* Set the global "on SD card" flag */
        if ((flags & android.content.pm.PackageParser.PARSE_EXTERNAL_STORAGE) != 0) {
            pkg.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifest_isolatedSplits, false)) {
            pkg.applicationInfo.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_ISOLATED_SPLIT_LOADING;
        }
        // Resource boolean are -1, so 1 means we don't know the value.
        int supportsSmallScreens = 1;
        int supportsNormalScreens = 1;
        int supportsLargeScreens = 1;
        int supportsXLargeScreens = 1;
        int resizeable = 1;
        int anyDensity = 1;
        int outerDepth = parser.getDepth();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            java.lang.String tagName = parser.getName();
            if ((acceptedTags != null) && (!acceptedTags.contains(tagName))) {
                android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Skipping unsupported element under <manifest>: " + tagName) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                continue;
            }
            if (tagName.equals(android.content.pm.PackageParser.TAG_APPLICATION)) {
                if (foundApp) {
                    if (android.content.pm.PackageParser.RIGID_PARSER) {
                        outError[0] = "<manifest> has more than one <application>";
                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return null;
                    } else {
                        android.util.Slog.w(android.content.pm.PackageParser.TAG, "<manifest> has more than one <application>");
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        continue;
                    }
                }
                foundApp = true;
                if (!parseBaseApplication(pkg, res, parser, flags, outError)) {
                    return null;
                }
            } else
                if (tagName.equals(android.content.pm.PackageParser.TAG_OVERLAY)) {
                    sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestResourceOverlay);
                    pkg.mOverlayTarget = sa.getString(com.android.internal.R.styleable.AndroidManifestResourceOverlay_targetPackage);
                    pkg.mOverlayTargetName = sa.getString(com.android.internal.R.styleable.AndroidManifestResourceOverlay_targetName);
                    pkg.mOverlayCategory = sa.getString(com.android.internal.R.styleable.AndroidManifestResourceOverlay_category);
                    pkg.mOverlayPriority = sa.getInt(com.android.internal.R.styleable.AndroidManifestResourceOverlay_priority, 0);
                    pkg.mOverlayIsStatic = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestResourceOverlay_isStatic, false);
                    final java.lang.String propName = sa.getString(com.android.internal.R.styleable.AndroidManifestResourceOverlay_requiredSystemPropertyName);
                    final java.lang.String propValue = sa.getString(com.android.internal.R.styleable.AndroidManifestResourceOverlay_requiredSystemPropertyValue);
                    sa.recycle();
                    if (pkg.mOverlayTarget == null) {
                        outError[0] = "<overlay> does not specify a target package";
                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return null;
                    }
                    if ((pkg.mOverlayPriority < 0) || (pkg.mOverlayPriority > 9999)) {
                        outError[0] = "<overlay> priority must be between 0 and 9999";
                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return null;
                    }
                    // check to see if overlay should be excluded based on system property condition
                    if (!checkOverlayRequiredSystemProperty(propName, propValue)) {
                        android.util.Slog.i(android.content.pm.PackageParser.TAG, (((((("Skipping target and overlay pair " + pkg.mOverlayTarget) + " and ") + pkg.baseCodePath) + ": overlay ignored due to required system property: ") + propName) + " with value: ") + propValue);
                        return null;
                    }
                    pkg.applicationInfo.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_IS_RESOURCE_OVERLAY;
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                } else
                    if (tagName.equals(android.content.pm.PackageParser.TAG_KEY_SETS)) {
                        if (!parseKeySets(pkg, res, parser, outError)) {
                            return null;
                        }
                    } else
                        if (tagName.equals(android.content.pm.PackageParser.TAG_PERMISSION_GROUP)) {
                            if (!parsePermissionGroup(pkg, flags, res, parser, outError)) {
                                return null;
                            }
                        } else
                            if (tagName.equals(android.content.pm.PackageParser.TAG_PERMISSION)) {
                                if (!parsePermission(pkg, res, parser, outError)) {
                                    return null;
                                }
                            } else
                                if (tagName.equals(android.content.pm.PackageParser.TAG_PERMISSION_TREE)) {
                                    if (!parsePermissionTree(pkg, res, parser, outError)) {
                                        return null;
                                    }
                                } else
                                    if (tagName.equals(android.content.pm.PackageParser.TAG_USES_PERMISSION)) {
                                        if (!parseUsesPermission(pkg, res, parser)) {
                                            return null;
                                        }
                                    } else
                                        if (tagName.equals(android.content.pm.PackageParser.TAG_USES_PERMISSION_SDK_M) || tagName.equals(android.content.pm.PackageParser.TAG_USES_PERMISSION_SDK_23)) {
                                            if (!parseUsesPermission(pkg, res, parser)) {
                                                return null;
                                            }
                                        } else
                                            if (tagName.equals(android.content.pm.PackageParser.TAG_USES_CONFIGURATION)) {
                                                android.content.pm.ConfigurationInfo cPref = new android.content.pm.ConfigurationInfo();
                                                sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestUsesConfiguration);
                                                cPref.reqTouchScreen = sa.getInt(com.android.internal.R.styleable.AndroidManifestUsesConfiguration_reqTouchScreen, android.content.res.Configuration.TOUCHSCREEN_UNDEFINED);
                                                cPref.reqKeyboardType = sa.getInt(com.android.internal.R.styleable.AndroidManifestUsesConfiguration_reqKeyboardType, android.content.res.Configuration.KEYBOARD_UNDEFINED);
                                                if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestUsesConfiguration_reqHardKeyboard, false)) {
                                                    cPref.reqInputFeatures |= android.content.pm.ConfigurationInfo.INPUT_FEATURE_HARD_KEYBOARD;
                                                }
                                                cPref.reqNavigation = sa.getInt(com.android.internal.R.styleable.AndroidManifestUsesConfiguration_reqNavigation, android.content.res.Configuration.NAVIGATION_UNDEFINED);
                                                if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestUsesConfiguration_reqFiveWayNav, false)) {
                                                    cPref.reqInputFeatures |= android.content.pm.ConfigurationInfo.INPUT_FEATURE_FIVE_WAY_NAV;
                                                }
                                                sa.recycle();
                                                pkg.configPreferences = com.android.internal.util.ArrayUtils.add(pkg.configPreferences, cPref);
                                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                            } else
                                                if (tagName.equals(android.content.pm.PackageParser.TAG_USES_FEATURE)) {
                                                    android.content.pm.FeatureInfo fi = parseUsesFeature(res, parser);
                                                    pkg.reqFeatures = com.android.internal.util.ArrayUtils.add(pkg.reqFeatures, fi);
                                                    if (fi.name == null) {
                                                        android.content.pm.ConfigurationInfo cPref = new android.content.pm.ConfigurationInfo();
                                                        cPref.reqGlEsVersion = fi.reqGlEsVersion;
                                                        pkg.configPreferences = com.android.internal.util.ArrayUtils.add(pkg.configPreferences, cPref);
                                                    }
                                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                } else
                                                    if (tagName.equals(android.content.pm.PackageParser.TAG_FEATURE_GROUP)) {
                                                        android.content.pm.FeatureGroupInfo group = new android.content.pm.FeatureGroupInfo();
                                                        java.util.ArrayList<android.content.pm.FeatureInfo> features = null;
                                                        final int innerDepth = parser.getDepth();
                                                        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
                                                            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                                                                continue;
                                                            }
                                                            final java.lang.String innerTagName = parser.getName();
                                                            if (innerTagName.equals("uses-feature")) {
                                                                android.content.pm.FeatureInfo featureInfo = parseUsesFeature(res, parser);
                                                                // FeatureGroups are stricter and mandate that
                                                                // any <uses-feature> declared are mandatory.
                                                                featureInfo.flags |= android.content.pm.FeatureInfo.FLAG_REQUIRED;
                                                                features = com.android.internal.util.ArrayUtils.add(features, featureInfo);
                                                            } else {
                                                                android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <feature-group>: " + innerTagName) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                                            }
                                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                        } 
                                                        if (features != null) {
                                                            group.features = new android.content.pm.FeatureInfo[features.size()];
                                                            group.features = features.toArray(group.features);
                                                        }
                                                        pkg.featureGroups = com.android.internal.util.ArrayUtils.add(pkg.featureGroups, group);
                                                    } else
                                                        if (tagName.equals(android.content.pm.PackageParser.TAG_USES_SDK)) {
                                                            if (android.content.pm.PackageParser.SDK_VERSION > 0) {
                                                                sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestUsesSdk);
                                                                int minVers = 1;
                                                                java.lang.String minCode = null;
                                                                int targetVers = 0;
                                                                java.lang.String targetCode = null;
                                                                android.util.TypedValue val = sa.peekValue(com.android.internal.R.styleable.AndroidManifestUsesSdk_minSdkVersion);
                                                                if (val != null) {
                                                                    if ((val.type == android.util.TypedValue.TYPE_STRING) && (val.string != null)) {
                                                                        minCode = val.string.toString();
                                                                    } else {
                                                                        // If it's not a string, it's an integer.
                                                                        minVers = val.data;
                                                                    }
                                                                }
                                                                val = sa.peekValue(com.android.internal.R.styleable.AndroidManifestUsesSdk_targetSdkVersion);
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
                                                                sa.recycle();
                                                                final int minSdkVersion = android.content.pm.PackageParser.computeMinSdkVersion(minVers, minCode, android.content.pm.PackageParser.SDK_VERSION, android.content.pm.PackageParser.SDK_CODENAMES, outError);
                                                                if (minSdkVersion < 0) {
                                                                    mParseError = android.content.pm.PackageManager.INSTALL_FAILED_OLDER_SDK;
                                                                    return null;
                                                                }
                                                                final int targetSdkVersion = android.content.pm.PackageParser.computeTargetSdkVersion(targetVers, targetCode, android.content.pm.PackageParser.SDK_CODENAMES, outError);
                                                                if (targetSdkVersion < 0) {
                                                                    mParseError = android.content.pm.PackageManager.INSTALL_FAILED_OLDER_SDK;
                                                                    return null;
                                                                }
                                                                pkg.applicationInfo.minSdkVersion = minSdkVersion;
                                                                pkg.applicationInfo.targetSdkVersion = targetSdkVersion;
                                                            }
                                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                        } else
                                                            if (tagName.equals(android.content.pm.PackageParser.TAG_SUPPORT_SCREENS)) {
                                                                sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestSupportsScreens);
                                                                pkg.applicationInfo.requiresSmallestWidthDp = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_requiresSmallestWidthDp, 0);
                                                                pkg.applicationInfo.compatibleWidthLimitDp = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_compatibleWidthLimitDp, 0);
                                                                pkg.applicationInfo.largestWidthLimitDp = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_largestWidthLimitDp, 0);
                                                                // This is a trick to get a boolean and still able to detect
                                                                // if a value was actually set.
                                                                supportsSmallScreens = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_smallScreens, supportsSmallScreens);
                                                                supportsNormalScreens = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_normalScreens, supportsNormalScreens);
                                                                supportsLargeScreens = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_largeScreens, supportsLargeScreens);
                                                                supportsXLargeScreens = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_xlargeScreens, supportsXLargeScreens);
                                                                resizeable = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_resizeable, resizeable);
                                                                anyDensity = sa.getInteger(com.android.internal.R.styleable.AndroidManifestSupportsScreens_anyDensity, anyDensity);
                                                                sa.recycle();
                                                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                            } else
                                                                if (tagName.equals(android.content.pm.PackageParser.TAG_PROTECTED_BROADCAST)) {
                                                                    sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestProtectedBroadcast);
                                                                    // Note: don't allow this value to be a reference to a resource
                                                                    // that may change.
                                                                    java.lang.String name = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestProtectedBroadcast_name);
                                                                    sa.recycle();
                                                                    if (name != null) {
                                                                        if (pkg.protectedBroadcasts == null) {
                                                                            pkg.protectedBroadcasts = new java.util.ArrayList<java.lang.String>();
                                                                        }
                                                                        if (!pkg.protectedBroadcasts.contains(name)) {
                                                                            pkg.protectedBroadcasts.add(name.intern());
                                                                        }
                                                                    }
                                                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                } else
                                                                    if (tagName.equals(android.content.pm.PackageParser.TAG_INSTRUMENTATION)) {
                                                                        if (parseInstrumentation(pkg, res, parser, outError) == null) {
                                                                            return null;
                                                                        }
                                                                    } else
                                                                        if (tagName.equals(android.content.pm.PackageParser.TAG_ORIGINAL_PACKAGE)) {
                                                                            sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestOriginalPackage);
                                                                            java.lang.String orig = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestOriginalPackage_name, 0);
                                                                            if (!pkg.packageName.equals(orig)) {
                                                                                if (pkg.mOriginalPackages == null) {
                                                                                    pkg.mOriginalPackages = new java.util.ArrayList<java.lang.String>();
                                                                                    pkg.mRealPackage = pkg.packageName;
                                                                                }
                                                                                pkg.mOriginalPackages.add(orig);
                                                                            }
                                                                            sa.recycle();
                                                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                        } else
                                                                            if (tagName.equals(android.content.pm.PackageParser.TAG_ADOPT_PERMISSIONS)) {
                                                                                sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestOriginalPackage);
                                                                                java.lang.String name = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestOriginalPackage_name, 0);
                                                                                sa.recycle();
                                                                                if (name != null) {
                                                                                    if (pkg.mAdoptPermissions == null) {
                                                                                        pkg.mAdoptPermissions = new java.util.ArrayList<java.lang.String>();
                                                                                    }
                                                                                    pkg.mAdoptPermissions.add(name);
                                                                                }
                                                                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                            } else
                                                                                if (tagName.equals(android.content.pm.PackageParser.TAG_USES_GL_TEXTURE)) {
                                                                                    // Just skip this tag
                                                                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                                    continue;
                                                                                } else
                                                                                    if (tagName.equals(android.content.pm.PackageParser.TAG_COMPATIBLE_SCREENS)) {
                                                                                        // Just skip this tag
                                                                                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                                        continue;
                                                                                    } else
                                                                                        if (tagName.equals(android.content.pm.PackageParser.TAG_SUPPORTS_INPUT)) {
                                                                                            // 
                                                                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                                            continue;
                                                                                        } else
                                                                                            if (tagName.equals(android.content.pm.PackageParser.TAG_EAT_COMMENT)) {
                                                                                                // Just skip this tag
                                                                                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                                                continue;
                                                                                            } else
                                                                                                if (tagName.equals(android.content.pm.PackageParser.TAG_PACKAGE)) {
                                                                                                    if (!android.content.pm.PackageParser.MULTI_PACKAGE_APK_ENABLED) {
                                                                                                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                                                        continue;
                                                                                                    }
                                                                                                    if (!parseBaseApkChild(pkg, res, parser, flags, outError)) {
                                                                                                        // If parsing a child failed the error is already set
                                                                                                        return null;
                                                                                                    }
                                                                                                } else
                                                                                                    if (tagName.equals(android.content.pm.PackageParser.TAG_RESTRICT_UPDATE)) {
                                                                                                        if ((flags & android.content.pm.PackageParser.PARSE_IS_SYSTEM_DIR) != 0) {
                                                                                                            sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestRestrictUpdate);
                                                                                                            final java.lang.String hash = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestRestrictUpdate_hash, 0);
                                                                                                            sa.recycle();
                                                                                                            pkg.restrictUpdateHash = null;
                                                                                                            if (hash != null) {
                                                                                                                final int hashLength = hash.length();
                                                                                                                final byte[] hashBytes = new byte[hashLength / 2];
                                                                                                                for (int i = 0; i < hashLength; i += 2) {
                                                                                                                    hashBytes[i / 2] = ((byte) ((java.lang.Character.digit(hash.charAt(i), 16) << 4) + java.lang.Character.digit(hash.charAt(i + 1), 16)));
                                                                                                                }
                                                                                                                pkg.restrictUpdateHash = hashBytes;
                                                                                                            }
                                                                                                        }
                                                                                                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                                                    } else
                                                                                                        if (android.content.pm.PackageParser.RIGID_PARSER) {
                                                                                                            outError[0] = "Bad element under <manifest>: " + parser.getName();
                                                                                                            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                                                                                            return null;
                                                                                                        } else {
                                                                                                            android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <manifest>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                                                                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                                                            continue;
                                                                                                        }























        } 
        if ((!foundApp) && (pkg.instrumentation.size() == 0)) {
            outError[0] = "<manifest> does not contain an <application> or <instrumentation>";
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
        }
        final int NP = android.content.pm.PackageParser.NEW_PERMISSIONS.length;
        java.lang.StringBuilder newPermsMsg = null;
        for (int ip = 0; ip < NP; ip++) {
            final android.content.pm.PackageParser.NewPermissionInfo npi = android.content.pm.PackageParser.NEW_PERMISSIONS[ip];
            if (pkg.applicationInfo.targetSdkVersion >= npi.sdkVersion) {
                break;
            }
            if (!pkg.requestedPermissions.contains(npi.name)) {
                if (newPermsMsg == null) {
                    newPermsMsg = new java.lang.StringBuilder(128);
                    newPermsMsg.append(pkg.packageName);
                    newPermsMsg.append(": compat added ");
                } else {
                    newPermsMsg.append(' ');
                }
                newPermsMsg.append(npi.name);
                pkg.requestedPermissions.add(npi.name);
                pkg.implicitPermissions.add(npi.name);
            }
        }
        if (newPermsMsg != null) {
            android.util.Slog.i(android.content.pm.PackageParser.TAG, newPermsMsg.toString());
        }
        final int NS = PermissionManager.SPLIT_PERMISSIONS.size();
        for (int is = 0; is < NS; is++) {
            final android.permission.PermissionManager.SplitPermissionInfo spi = PermissionManager.SPLIT_PERMISSIONS.get(is);
            if ((pkg.applicationInfo.targetSdkVersion >= spi.getTargetSdk()) || (!pkg.requestedPermissions.contains(spi.getSplitPermission()))) {
                continue;
            }
            final java.util.List<java.lang.String> newPerms = spi.getNewPermissions();
            for (int in = 0; in < newPerms.size(); in++) {
                final java.lang.String perm = newPerms.get(in);
                if (!pkg.requestedPermissions.contains(perm)) {
                    pkg.requestedPermissions.add(perm);
                    pkg.implicitPermissions.add(perm);
                }
            }
        }
        if ((supportsSmallScreens < 0) || ((supportsSmallScreens > 0) && (pkg.applicationInfo.targetSdkVersion >= android.os.Build.VERSION_CODES.DONUT))) {
            pkg.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS;
        }
        if (supportsNormalScreens != 0) {
            pkg.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS;
        }
        if ((supportsLargeScreens < 0) || ((supportsLargeScreens > 0) && (pkg.applicationInfo.targetSdkVersion >= android.os.Build.VERSION_CODES.DONUT))) {
            pkg.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS;
        }
        if ((supportsXLargeScreens < 0) || ((supportsXLargeScreens > 0) && (pkg.applicationInfo.targetSdkVersion >= android.os.Build.VERSION_CODES.GINGERBREAD))) {
            pkg.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS;
        }
        if ((resizeable < 0) || ((resizeable > 0) && (pkg.applicationInfo.targetSdkVersion >= android.os.Build.VERSION_CODES.DONUT))) {
            pkg.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS;
        }
        if ((anyDensity < 0) || ((anyDensity > 0) && (pkg.applicationInfo.targetSdkVersion >= android.os.Build.VERSION_CODES.DONUT))) {
            pkg.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES;
        }
        // At this point we can check if an application is not supporting densities and hence
        // cannot be windowed / resized. Note that an SDK version of 0 is common for
        // pre-Doughnut applications.
        if (pkg.applicationInfo.usesCompatibilityMode()) {
            adjustPackageToBeUnresizeableAndUnpipable(pkg);
        }
        return pkg;
    }

    private boolean checkOverlayRequiredSystemProperty(java.lang.String propName, java.lang.String propValue) {
        if (android.text.TextUtils.isEmpty(propName) || android.text.TextUtils.isEmpty(propValue)) {
            if ((!android.text.TextUtils.isEmpty(propName)) || (!android.text.TextUtils.isEmpty(propValue))) {
                // malformed condition - incomplete
                android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Disabling overlay - incomplete property :'" + propName) + "=") + propValue) + "' - require both requiredSystemPropertyName") + " AND requiredSystemPropertyValue to be specified.");
                return false;
            }
            // no valid condition set - so no exclusion criteria, overlay will be included.
            return true;
        }
        // check property value - make sure it is both set and equal to expected value
        final java.lang.String currValue = android.os.SystemProperties.get(propName);
        return (currValue != null) && currValue.equals(propValue);
    }

    /**
     * This is a pre-density application which will get scaled - instead of being pixel perfect.
     * This type of application is not resizable.
     *
     * @param pkg
     * 		The package which needs to be marked as unresizable.
     */
    private void adjustPackageToBeUnresizeableAndUnpipable(android.content.pm.PackageParser.Package pkg) {
        for (android.content.pm.PackageParser.Activity a : pkg.activities) {
            a.info.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_UNRESIZEABLE;
            a.info.flags &= ~android.content.pm.ActivityInfo.FLAG_SUPPORTS_PICTURE_IN_PICTURE;
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

    /**
     * Computes the targetSdkVersion to use at runtime. If the package is not
     * compatible with this platform, populates {@code outError[0]} with an
     * error message.
     * <p>
     * If {@code targetCode} is not specified, e.g. the value is {@code null},
     * then the {@code targetVers} will be returned unmodified.
     * <p>
     * Otherwise, the behavior varies based on whether the current platform
     * is a pre-release version, e.g. the {@code platformSdkCodenames} array
     * has length > 0:
     * <ul>
     * <li>If this is a pre-release platform and the value specified by
     * {@code targetCode} is contained within the array of allowed pre-release
     * codenames, this method will return {@link Build.VERSION_CODES#CUR_DEVELOPMENT}.
     * <li>If this is a released platform, this method will return -1 to
     * indicate that the package is not compatible with this platform.
     * </ul>
     *
     * @param targetVers
     * 		targetSdkVersion number, if specified in the
     * 		application manifest, or 0 otherwise
     * @param targetCode
     * 		targetSdkVersion code, if specified in the application
     * 		manifest, or {@code null} otherwise
     * @param platformSdkCodenames
     * 		array of allowed pre-release SDK codenames
     * 		for this platform
     * @param outError
     * 		output array to populate with error, if applicable
     * @return the targetSdkVersion to use at runtime, or -1 if the package is
    not compatible with this platform
     * @unknown Exposed for unit testing only.
     */
    @android.annotation.TestApi
    public static int computeTargetSdkVersion(@android.annotation.IntRange(from = 0)
    int targetVers, @android.annotation.Nullable
    java.lang.String targetCode, @android.annotation.NonNull
    java.lang.String[] platformSdkCodenames, @android.annotation.NonNull
    java.lang.String[] outError) {
        // If it's a release SDK, return the version number unmodified.
        if (targetCode == null) {
            return targetVers;
        }
        // If it's a pre-release SDK and the codename matches this platform, it
        // definitely targets this SDK.
        if (android.content.pm.PackageParser.matchTargetCode(platformSdkCodenames, targetCode)) {
            return Build.VERSION_CODES.CUR_DEVELOPMENT;
        }
        // Otherwise, we're looking at an incompatible pre-release SDK.
        if (platformSdkCodenames.length > 0) {
            outError[0] = ((("Requires development platform " + targetCode) + " (current platform is any of ") + java.util.Arrays.toString(platformSdkCodenames)) + ")";
        } else {
            outError[0] = ("Requires development platform " + targetCode) + " but this is a release platform.";
        }
        return -1;
    }

    /**
     * Computes the minSdkVersion to use at runtime. If the package is not
     * compatible with this platform, populates {@code outError[0]} with an
     * error message.
     * <p>
     * If {@code minCode} is not specified, e.g. the value is {@code null},
     * then behavior varies based on the {@code platformSdkVersion}:
     * <ul>
     * <li>If the platform SDK version is greater than or equal to the
     * {@code minVers}, returns the {@code mniVers} unmodified.
     * <li>Otherwise, returns -1 to indicate that the package is not
     * compatible with this platform.
     * </ul>
     * <p>
     * Otherwise, the behavior varies based on whether the current platform
     * is a pre-release version, e.g. the {@code platformSdkCodenames} array
     * has length > 0:
     * <ul>
     * <li>If this is a pre-release platform and the value specified by
     * {@code targetCode} is contained within the array of allowed pre-release
     * codenames, this method will return {@link Build.VERSION_CODES#CUR_DEVELOPMENT}.
     * <li>If this is a released platform, this method will return -1 to
     * indicate that the package is not compatible with this platform.
     * </ul>
     *
     * @param minVers
     * 		minSdkVersion number, if specified in the application
     * 		manifest, or 1 otherwise
     * @param minCode
     * 		minSdkVersion code, if specified in the application
     * 		manifest, or {@code null} otherwise
     * @param platformSdkVersion
     * 		platform SDK version number, typically
     * 		Build.VERSION.SDK_INT
     * @param platformSdkCodenames
     * 		array of allowed prerelease SDK codenames
     * 		for this platform
     * @param outError
     * 		output array to populate with error, if applicable
     * @return the minSdkVersion to use at runtime, or -1 if the package is not
    compatible with this platform
     * @unknown Exposed for unit testing only.
     */
    @android.annotation.TestApi
    public static int computeMinSdkVersion(@android.annotation.IntRange(from = 1)
    int minVers, @android.annotation.Nullable
    java.lang.String minCode, @android.annotation.IntRange(from = 1)
    int platformSdkVersion, @android.annotation.NonNull
    java.lang.String[] platformSdkCodenames, @android.annotation.NonNull
    java.lang.String[] outError) {
        // If it's a release SDK, make sure we meet the minimum SDK requirement.
        if (minCode == null) {
            if (minVers <= platformSdkVersion) {
                return minVers;
            }
            // We don't meet the minimum SDK requirement.
            outError[0] = ((("Requires newer sdk version #" + minVers) + " (current version is #") + platformSdkVersion) + ")";
            return -1;
        }
        // If it's a pre-release SDK and the codename matches this platform, we
        // definitely meet the minimum SDK requirement.
        if (android.content.pm.PackageParser.matchTargetCode(platformSdkCodenames, minCode)) {
            return Build.VERSION_CODES.CUR_DEVELOPMENT;
        }
        // Otherwise, we're looking at an incompatible pre-release SDK.
        if (platformSdkCodenames.length > 0) {
            outError[0] = ((("Requires development platform " + minCode) + " (current platform is any of ") + java.util.Arrays.toString(platformSdkCodenames)) + ")";
        } else {
            outError[0] = ("Requires development platform " + minCode) + " but this is a release platform.";
        }
        return -1;
    }

    private android.content.pm.FeatureInfo parseUsesFeature(android.content.res.Resources res, android.util.AttributeSet attrs) {
        android.content.pm.FeatureInfo fi = new android.content.pm.FeatureInfo();
        android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.AndroidManifestUsesFeature);
        // Note: don't allow this value to be a reference to a resource
        // that may change.
        fi.name = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestUsesFeature_name);
        fi.version = sa.getInt(com.android.internal.R.styleable.AndroidManifestUsesFeature_version, 0);
        if (fi.name == null) {
            fi.reqGlEsVersion = sa.getInt(com.android.internal.R.styleable.AndroidManifestUsesFeature_glEsVersion, android.content.pm.FeatureInfo.GL_ES_VERSION_UNDEFINED);
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestUsesFeature_required, true)) {
            fi.flags |= android.content.pm.FeatureInfo.FLAG_REQUIRED;
        }
        sa.recycle();
        return fi;
    }

    private boolean parseUsesStaticLibrary(android.content.pm.PackageParser.Package pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestUsesStaticLibrary);
        // Note: don't allow this value to be a reference to a resource that may change.
        java.lang.String lname = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestUsesLibrary_name);
        final int version = sa.getInt(com.android.internal.R.styleable.AndroidManifestUsesStaticLibrary_version, -1);
        java.lang.String certSha256Digest = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestUsesStaticLibrary_certDigest);
        sa.recycle();
        // Since an APK providing a static shared lib can only provide the lib - fail if malformed
        if (((lname == null) || (version < 0)) || (certSha256Digest == null)) {
            outError[0] = (((("Bad uses-static-library declaration name: " + lname) + " version: ") + version) + " certDigest") + certSha256Digest;
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            return false;
        }
        // Can depend only on one version of the same library
        if ((pkg.usesStaticLibraries != null) && pkg.usesStaticLibraries.contains(lname)) {
            outError[0] = "Depending on multiple versions of static library " + lname;
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            return false;
        }
        lname = lname.intern();
        // We allow ":" delimiters in the SHA declaration as this is the format
        // emitted by the certtool making it easy for developers to copy/paste.
        certSha256Digest = certSha256Digest.replace(":", "").toLowerCase();
        // Fot apps targeting O-MR1 we require explicit enumeration of all certs.
        java.lang.String[] additionalCertSha256Digests = libcore.util.EmptyArray.STRING;
        if (pkg.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.O_MR1) {
            additionalCertSha256Digests = parseAdditionalCertificates(res, parser, outError);
            if (additionalCertSha256Digests == null) {
                return false;
            }
        } else {
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        }
        final java.lang.String[] certSha256Digests = new java.lang.String[additionalCertSha256Digests.length + 1];
        certSha256Digests[0] = certSha256Digest;
        java.lang.System.arraycopy(additionalCertSha256Digests, 0, certSha256Digests, 1, additionalCertSha256Digests.length);
        pkg.usesStaticLibraries = com.android.internal.util.ArrayUtils.add(pkg.usesStaticLibraries, lname);
        pkg.usesStaticLibrariesVersions = com.android.internal.util.ArrayUtils.appendLong(pkg.usesStaticLibrariesVersions, version, true);
        pkg.usesStaticLibrariesCertDigests = com.android.internal.util.ArrayUtils.appendElement(java.lang.String[].class, pkg.usesStaticLibrariesCertDigests, certSha256Digests, true);
        return true;
    }

    private java.lang.String[] parseAdditionalCertificates(android.content.res.Resources resources, android.content.res.XmlResourceParser parser, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String[] certSha256Digests = libcore.util.EmptyArray.STRING;
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            final java.lang.String nodeName = parser.getName();
            if (nodeName.equals("additional-certificate")) {
                final android.content.res.TypedArray sa = resources.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestAdditionalCertificate);
                java.lang.String certSha256Digest = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestAdditionalCertificate_certDigest);
                sa.recycle();
                if (android.text.TextUtils.isEmpty(certSha256Digest)) {
                    outError[0] = ("Bad additional-certificate declaration with empty" + " certDigest:") + certSha256Digest;
                    mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    sa.recycle();
                    return null;
                }
                // We allow ":" delimiters in the SHA declaration as this is the format
                // emitted by the certtool making it easy for developers to copy/paste.
                certSha256Digest = certSha256Digest.replace(":", "").toLowerCase();
                certSha256Digests = com.android.internal.util.ArrayUtils.appendElement(java.lang.String.class, certSha256Digests, certSha256Digest);
            } else {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        } 
        return certSha256Digests;
    }

    private boolean parseUsesPermission(android.content.pm.PackageParser.Package pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestUsesPermission);
        // Note: don't allow this value to be a reference to a resource
        // that may change.
        java.lang.String name = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestUsesPermission_name);
        int maxSdkVersion = 0;
        android.util.TypedValue val = sa.peekValue(com.android.internal.R.styleable.AndroidManifestUsesPermission_maxSdkVersion);
        if (val != null) {
            if ((val.type >= android.util.TypedValue.TYPE_FIRST_INT) && (val.type <= android.util.TypedValue.TYPE_LAST_INT)) {
                maxSdkVersion = val.data;
            }
        }
        final java.lang.String requiredFeature = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestUsesPermission_requiredFeature, 0);
        final java.lang.String requiredNotfeature = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestUsesPermission_requiredNotFeature, 0);
        sa.recycle();
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        if (name == null) {
            return true;
        }
        if ((maxSdkVersion != 0) && (maxSdkVersion < Build.VERSION.RESOURCES_SDK_INT)) {
            return true;
        }
        // Only allow requesting this permission if the platform supports the given feature.
        if (((requiredFeature != null) && (mCallback != null)) && (!mCallback.hasFeature(requiredFeature))) {
            return true;
        }
        // Only allow requesting this permission if the platform doesn't support the given feature.
        if (((requiredNotfeature != null) && (mCallback != null)) && mCallback.hasFeature(requiredNotfeature)) {
            return true;
        }
        int index = pkg.requestedPermissions.indexOf(name);
        if (index == (-1)) {
            pkg.requestedPermissions.add(name.intern());
        } else {
            android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Ignoring duplicate uses-permissions/uses-permissions-sdk-m: " + name) + " in package: ") + pkg.packageName) + " at: ") + parser.getPositionDescription());
        }
        return true;
    }

    private static java.lang.String buildClassName(java.lang.String pkg, java.lang.CharSequence clsSeq, java.lang.String[] outError) {
        if ((clsSeq == null) || (clsSeq.length() <= 0)) {
            outError[0] = "Empty class name in package " + pkg;
            return null;
        }
        java.lang.String cls = clsSeq.toString();
        char c = cls.charAt(0);
        if (c == '.') {
            return pkg + cls;
        }
        if (cls.indexOf('.') < 0) {
            java.lang.StringBuilder b = new java.lang.StringBuilder(pkg);
            b.append('.');
            b.append(cls);
            return b.toString();
        }
        return cls;
    }

    private static java.lang.String buildCompoundName(java.lang.String pkg, java.lang.CharSequence procSeq, java.lang.String type, java.lang.String[] outError) {
        java.lang.String proc = procSeq.toString();
        char c = proc.charAt(0);
        if ((pkg != null) && (c == ':')) {
            if (proc.length() < 2) {
                outError[0] = ((((("Bad " + type) + " name ") + proc) + " in package ") + pkg) + ": must be at least two characters";
                return null;
            }
            java.lang.String subName = proc.substring(1);
            java.lang.String nameError = android.content.pm.PackageParser.validateName(subName, false, false);
            if (nameError != null) {
                outError[0] = (((((("Invalid " + type) + " name ") + proc) + " in package ") + pkg) + ": ") + nameError;
                return null;
            }
            return pkg + proc;
        }
        java.lang.String nameError = android.content.pm.PackageParser.validateName(proc, true, false);
        if ((nameError != null) && (!"system".equals(proc))) {
            outError[0] = (((((("Invalid " + type) + " name ") + proc) + " in package ") + pkg) + ": ") + nameError;
            return null;
        }
        return proc;
    }

    private static java.lang.String buildProcessName(java.lang.String pkg, java.lang.String defProc, java.lang.CharSequence procSeq, int flags, java.lang.String[] separateProcesses, java.lang.String[] outError) {
        if (((flags & android.content.pm.PackageParser.PARSE_IGNORE_PROCESSES) != 0) && (!"system".equals(procSeq))) {
            return defProc != null ? defProc : pkg;
        }
        if (separateProcesses != null) {
            for (int i = separateProcesses.length - 1; i >= 0; i--) {
                java.lang.String sp = separateProcesses[i];
                if ((sp.equals(pkg) || sp.equals(defProc)) || sp.equals(procSeq)) {
                    return pkg;
                }
            }
        }
        if ((procSeq == null) || (procSeq.length() <= 0)) {
            return defProc;
        }
        return android.text.TextUtils.safeIntern(android.content.pm.PackageParser.buildCompoundName(pkg, procSeq, "process", outError));
    }

    private static java.lang.String buildTaskAffinityName(java.lang.String pkg, java.lang.String defProc, java.lang.CharSequence procSeq, java.lang.String[] outError) {
        if (procSeq == null) {
            return defProc;
        }
        if (procSeq.length() <= 0) {
            return null;
        }
        return android.content.pm.PackageParser.buildCompoundName(pkg, procSeq, "taskAffinity", outError);
    }

    private boolean parseKeySets(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // we've encountered the 'key-sets' tag
        // all the keys and keysets that we want must be defined here
        // so we're going to iterate over the parser and pull out the things we want
        int outerDepth = parser.getDepth();
        int currentKeySetDepth = -1;
        int type;
        java.lang.String currentKeySet = null;
        android.util.ArrayMap<java.lang.String, java.security.PublicKey> publicKeys = new android.util.ArrayMap<java.lang.String, java.security.PublicKey>();
        android.util.ArraySet<java.lang.String> upgradeKeySets = new android.util.ArraySet<java.lang.String>();
        android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> definedKeySets = new android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>>();
        android.util.ArraySet<java.lang.String> improperKeySets = new android.util.ArraySet<java.lang.String>();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if (type == org.xmlpull.v1.XmlPullParser.END_TAG) {
                if (parser.getDepth() == currentKeySetDepth) {
                    currentKeySet = null;
                    currentKeySetDepth = -1;
                }
                continue;
            }
            java.lang.String tagName = parser.getName();
            if (tagName.equals("key-set")) {
                if (currentKeySet != null) {
                    outError[0] = "Improperly nested 'key-set' tag at " + parser.getPositionDescription();
                    mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    return false;
                }
                final android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestKeySet);
                final java.lang.String keysetName = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestKeySet_name);
                definedKeySets.put(keysetName, new android.util.ArraySet<java.lang.String>());
                currentKeySet = keysetName;
                currentKeySetDepth = parser.getDepth();
                sa.recycle();
            } else
                if (tagName.equals("public-key")) {
                    if (currentKeySet == null) {
                        outError[0] = "Improperly nested 'key-set' tag at " + parser.getPositionDescription();
                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return false;
                    }
                    final android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestPublicKey);
                    final java.lang.String publicKeyName = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestPublicKey_name);
                    final java.lang.String encodedKey = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestPublicKey_value);
                    if ((encodedKey == null) && (publicKeys.get(publicKeyName) == null)) {
                        outError[0] = ((("'public-key' " + publicKeyName) + " must define a public-key value") + " on first use at ") + parser.getPositionDescription();
                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        sa.recycle();
                        return false;
                    } else
                        if (encodedKey != null) {
                            java.security.PublicKey currentKey = android.content.pm.PackageParser.parsePublicKey(encodedKey);
                            if (currentKey == null) {
                                android.util.Slog.w(android.content.pm.PackageParser.TAG, ((("No recognized valid key in 'public-key' tag at " + parser.getPositionDescription()) + " key-set ") + currentKeySet) + " will not be added to the package's defined key-sets.");
                                sa.recycle();
                                improperKeySets.add(currentKeySet);
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                continue;
                            }
                            if ((publicKeys.get(publicKeyName) == null) || publicKeys.get(publicKeyName).equals(currentKey)) {
                                /* public-key first definition, or matches old definition */
                                publicKeys.put(publicKeyName, currentKey);
                            } else {
                                outError[0] = (("Value of 'public-key' " + publicKeyName) + " conflicts with previously defined value at ") + parser.getPositionDescription();
                                mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                sa.recycle();
                                return false;
                            }
                        }

                    definedKeySets.get(currentKeySet).add(publicKeyName);
                    sa.recycle();
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                } else
                    if (tagName.equals("upgrade-key-set")) {
                        final android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestUpgradeKeySet);
                        java.lang.String name = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestUpgradeKeySet_name);
                        upgradeKeySets.add(name);
                        sa.recycle();
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    } else
                        if (android.content.pm.PackageParser.RIGID_PARSER) {
                            outError[0] = (((("Bad element under <key-sets>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription();
                            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                            return false;
                        } else {
                            android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <key-sets>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                            continue;
                        }



        } 
        java.util.Set<java.lang.String> publicKeyNames = publicKeys.keySet();
        if (publicKeyNames.removeAll(definedKeySets.keySet())) {
            outError[0] = (("Package" + owner.packageName) + " AndroidManifext.xml ") + "'key-set' and 'public-key' names must be distinct.";
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        owner.mKeySetMapping = new android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.security.PublicKey>>();
        for (ArrayMap.Entry<java.lang.String, android.util.ArraySet<java.lang.String>> e : definedKeySets.entrySet()) {
            final java.lang.String keySetName = e.getKey();
            if (e.getValue().size() == 0) {
                android.util.Slog.w(android.content.pm.PackageParser.TAG, ((((("Package" + owner.packageName) + " AndroidManifext.xml ") + "'key-set' ") + keySetName) + " has no valid associated 'public-key'.") + " Not including in package's defined key-sets.");
                continue;
            } else
                if (improperKeySets.contains(keySetName)) {
                    android.util.Slog.w(android.content.pm.PackageParser.TAG, ((((("Package" + owner.packageName) + " AndroidManifext.xml ") + "'key-set' ") + keySetName) + " contained improper 'public-key'") + " tags. Not including in package's defined key-sets.");
                    continue;
                }

            owner.mKeySetMapping.put(keySetName, new android.util.ArraySet<java.security.PublicKey>());
            for (java.lang.String s : e.getValue()) {
                owner.mKeySetMapping.get(keySetName).add(publicKeys.get(s));
            }
        }
        if (owner.mKeySetMapping.keySet().containsAll(upgradeKeySets)) {
            owner.mUpgradeKeySets = upgradeKeySets;
        } else {
            outError[0] = (("Package" + owner.packageName) + " AndroidManifext.xml ") + "does not define all 'upgrade-key-set's .";
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        return true;
    }

    private boolean parsePermissionGroup(android.content.pm.PackageParser.Package owner, int flags, android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestPermissionGroup);
        int requestDetailResourceId = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestPermissionGroup_requestDetail, 0);
        int backgroundRequestResourceId = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestPermissionGroup_backgroundRequest, 0);
        int backgroundRequestDetailResourceId = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestPermissionGroup_backgroundRequestDetail, 0);
        android.content.pm.PackageParser.PermissionGroup perm = new android.content.pm.PackageParser.PermissionGroup(owner, requestDetailResourceId, backgroundRequestResourceId, backgroundRequestDetailResourceId);
        if (!/* nameRequired */
        android.content.pm.PackageParser.parsePackageItemInfo(owner, perm.info, outError, "<permission-group>", sa, true, com.android.internal.R.styleable.AndroidManifestPermissionGroup_name, com.android.internal.R.styleable.AndroidManifestPermissionGroup_label, com.android.internal.R.styleable.AndroidManifestPermissionGroup_icon, com.android.internal.R.styleable.AndroidManifestPermissionGroup_roundIcon, com.android.internal.R.styleable.AndroidManifestPermissionGroup_logo, com.android.internal.R.styleable.AndroidManifestPermissionGroup_banner)) {
            sa.recycle();
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        perm.info.descriptionRes = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestPermissionGroup_description, 0);
        perm.info.requestRes = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestPermissionGroup_request, 0);
        perm.info.flags = sa.getInt(com.android.internal.R.styleable.AndroidManifestPermissionGroup_permissionGroupFlags, 0);
        perm.info.priority = sa.getInt(com.android.internal.R.styleable.AndroidManifestPermissionGroup_priority, 0);
        sa.recycle();
        if (!parseAllMetaData(res, parser, "<permission-group>", perm, outError)) {
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        owner.permissionGroups.add(perm);
        return true;
    }

    private boolean parsePermission(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestPermission);
        java.lang.String backgroundPermission = null;
        if (sa.hasValue(com.android.internal.R.styleable.AndroidManifestPermission_backgroundPermission)) {
            if ("android".equals(owner.packageName)) {
                backgroundPermission = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestPermission_backgroundPermission);
            } else {
                android.util.Slog.w(android.content.pm.PackageParser.TAG, (owner.packageName + " defines a background permission. Only the ") + "'android' package can do that.");
            }
        }
        android.content.pm.PackageParser.Permission perm = new android.content.pm.PackageParser.Permission(owner, backgroundPermission);
        if (!/* nameRequired */
        android.content.pm.PackageParser.parsePackageItemInfo(owner, perm.info, outError, "<permission>", sa, true, com.android.internal.R.styleable.AndroidManifestPermission_name, com.android.internal.R.styleable.AndroidManifestPermission_label, com.android.internal.R.styleable.AndroidManifestPermission_icon, com.android.internal.R.styleable.AndroidManifestPermission_roundIcon, com.android.internal.R.styleable.AndroidManifestPermission_logo, com.android.internal.R.styleable.AndroidManifestPermission_banner)) {
            sa.recycle();
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        // Note: don't allow this value to be a reference to a resource
        // that may change.
        perm.info.group = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestPermission_permissionGroup);
        if (perm.info.group != null) {
            perm.info.group = perm.info.group.intern();
        }
        perm.info.descriptionRes = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestPermission_description, 0);
        perm.info.requestRes = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestPermission_request, 0);
        perm.info.protectionLevel = sa.getInt(com.android.internal.R.styleable.AndroidManifestPermission_protectionLevel, android.content.pm.PermissionInfo.PROTECTION_NORMAL);
        perm.info.flags = sa.getInt(com.android.internal.R.styleable.AndroidManifestPermission_permissionFlags, 0);
        // For now only platform runtime permissions can be restricted
        if ((!perm.info.isRuntime()) || (!"android".equals(perm.info.packageName))) {
            perm.info.flags &= ~android.content.pm.PermissionInfo.FLAG_HARD_RESTRICTED;
            perm.info.flags &= ~android.content.pm.PermissionInfo.FLAG_SOFT_RESTRICTED;
        } else {
            // The platform does not get to specify conflicting permissions
            if (((perm.info.flags & android.content.pm.PermissionInfo.FLAG_HARD_RESTRICTED) != 0) && ((perm.info.flags & android.content.pm.PermissionInfo.FLAG_SOFT_RESTRICTED) != 0)) {
                throw new java.lang.IllegalStateException(("Permission cannot be both soft and hard" + " restricted: ") + perm.info.name);
            }
        }
        sa.recycle();
        if (perm.info.protectionLevel == (-1)) {
            outError[0] = "<permission> does not specify protectionLevel";
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        perm.info.protectionLevel = android.content.pm.PermissionInfo.fixProtectionLevel(perm.info.protectionLevel);
        if (perm.info.getProtectionFlags() != 0) {
            if ((((perm.info.protectionLevel & android.content.pm.PermissionInfo.PROTECTION_FLAG_INSTANT) == 0) && ((perm.info.protectionLevel & android.content.pm.PermissionInfo.PROTECTION_FLAG_RUNTIME_ONLY) == 0)) && ((perm.info.protectionLevel & android.content.pm.PermissionInfo.PROTECTION_MASK_BASE) != android.content.pm.PermissionInfo.PROTECTION_SIGNATURE)) {
                outError[0] = "<permission>  protectionLevel specifies a non-instant flag but is " + "not based on signature type";
                mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                return false;
            }
        }
        if (!parseAllMetaData(res, parser, "<permission>", perm, outError)) {
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        owner.permissions.add(perm);
        return true;
    }

    private boolean parsePermissionTree(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.PackageParser.Permission perm = new android.content.pm.PackageParser.Permission(owner, ((java.lang.String) (null)));
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestPermissionTree);
        if (!/* nameRequired */
        android.content.pm.PackageParser.parsePackageItemInfo(owner, perm.info, outError, "<permission-tree>", sa, true, com.android.internal.R.styleable.AndroidManifestPermissionTree_name, com.android.internal.R.styleable.AndroidManifestPermissionTree_label, com.android.internal.R.styleable.AndroidManifestPermissionTree_icon, com.android.internal.R.styleable.AndroidManifestPermissionTree_roundIcon, com.android.internal.R.styleable.AndroidManifestPermissionTree_logo, com.android.internal.R.styleable.AndroidManifestPermissionTree_banner)) {
            sa.recycle();
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        sa.recycle();
        int index = perm.info.name.indexOf('.');
        if (index > 0) {
            index = perm.info.name.indexOf('.', index + 1);
        }
        if (index < 0) {
            outError[0] = "<permission-tree> name has less than three segments: " + perm.info.name;
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        perm.info.descriptionRes = 0;
        perm.info.requestRes = 0;
        perm.info.protectionLevel = android.content.pm.PermissionInfo.PROTECTION_NORMAL;
        perm.tree = true;
        if (!parseAllMetaData(res, parser, "<permission-tree>", perm, outError)) {
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        owner.permissions.add(perm);
        return true;
    }

    private android.content.pm.PackageParser.Instrumentation parseInstrumentation(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestInstrumentation);
        if (mParseInstrumentationArgs == null) {
            mParseInstrumentationArgs = new android.content.pm.PackageParser.ParsePackageItemArgs(owner, outError, com.android.internal.R.styleable.AndroidManifestInstrumentation_name, com.android.internal.R.styleable.AndroidManifestInstrumentation_label, com.android.internal.R.styleable.AndroidManifestInstrumentation_icon, com.android.internal.R.styleable.AndroidManifestInstrumentation_roundIcon, com.android.internal.R.styleable.AndroidManifestInstrumentation_logo, com.android.internal.R.styleable.AndroidManifestInstrumentation_banner);
            mParseInstrumentationArgs.tag = "<instrumentation>";
        }
        mParseInstrumentationArgs.sa = sa;
        android.content.pm.PackageParser.Instrumentation a = new android.content.pm.PackageParser.Instrumentation(mParseInstrumentationArgs, new android.content.pm.InstrumentationInfo());
        if (outError[0] != null) {
            sa.recycle();
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        java.lang.String str;
        // Note: don't allow this value to be a reference to a resource
        // that may change.
        str = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestInstrumentation_targetPackage);
        a.info.targetPackage = (str != null) ? str.intern() : null;
        str = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestInstrumentation_targetProcesses);
        a.info.targetProcesses = (str != null) ? str.intern() : null;
        a.info.handleProfiling = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestInstrumentation_handleProfiling, false);
        a.info.functionalTest = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestInstrumentation_functionalTest, false);
        sa.recycle();
        if (a.info.targetPackage == null) {
            outError[0] = "<instrumentation> does not specify targetPackage";
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        if (!parseAllMetaData(res, parser, "<instrumentation>", a, outError)) {
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        owner.instrumentation.add(a);
        return a;
    }

    /**
     * Parse the {@code application} XML tree at the current parse location in a
     * <em>base APK</em> manifest.
     * <p>
     * When adding new features, carefully consider if they should also be
     * supported by split APKs.
     */
    @android.annotation.UnsupportedAppUsage
    private boolean parseBaseApplication(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.pm.ApplicationInfo ai = owner.applicationInfo;
        final java.lang.String pkgName = owner.applicationInfo.packageName;
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestApplication);
        ai.iconRes = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestApplication_icon, 0);
        ai.roundIconRes = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestApplication_roundIcon, 0);
        if (!/* nameRequired */
        android.content.pm.PackageParser.parsePackageItemInfo(owner, ai, outError, "<application>", sa, false, com.android.internal.R.styleable.AndroidManifestApplication_name, com.android.internal.R.styleable.AndroidManifestApplication_label, com.android.internal.R.styleable.AndroidManifestApplication_icon, com.android.internal.R.styleable.AndroidManifestApplication_roundIcon, com.android.internal.R.styleable.AndroidManifestApplication_logo, com.android.internal.R.styleable.AndroidManifestApplication_banner)) {
            sa.recycle();
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        if (ai.name != null) {
            ai.className = ai.name;
        }
        java.lang.String manageSpaceActivity = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestApplication_manageSpaceActivity, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
        if (manageSpaceActivity != null) {
            ai.manageSpaceActivityName = android.content.pm.PackageParser.buildClassName(pkgName, manageSpaceActivity, outError);
        }
        boolean allowBackup = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_allowBackup, true);
        if (allowBackup) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_ALLOW_BACKUP;
            // backupAgent, killAfterRestore, fullBackupContent, backupInForeground,
            // and restoreAnyVersion are only relevant if backup is possible for the
            // given application.
            java.lang.String backupAgent = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestApplication_backupAgent, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
            if (backupAgent != null) {
                ai.backupAgentName = android.content.pm.PackageParser.buildClassName(pkgName, backupAgent, outError);
                if (android.content.pm.PackageParser.DEBUG_BACKUP) {
                    android.util.Slog.v(android.content.pm.PackageParser.TAG, (((("android:backupAgent = " + ai.backupAgentName) + " from ") + pkgName) + "+") + backupAgent);
                }
                if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_killAfterRestore, true)) {
                    ai.flags |= android.content.pm.ApplicationInfo.FLAG_KILL_AFTER_RESTORE;
                }
                if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_restoreAnyVersion, false)) {
                    ai.flags |= android.content.pm.ApplicationInfo.FLAG_RESTORE_ANY_VERSION;
                }
                if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_fullBackupOnly, false)) {
                    ai.flags |= android.content.pm.ApplicationInfo.FLAG_FULL_BACKUP_ONLY;
                }
                if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_backupInForeground, false)) {
                    ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_BACKUP_IN_FOREGROUND;
                }
            }
            android.util.TypedValue v = sa.peekValue(com.android.internal.R.styleable.AndroidManifestApplication_fullBackupContent);
            if ((v != null) && ((ai.fullBackupContent = v.resourceId) == 0)) {
                if (android.content.pm.PackageParser.DEBUG_BACKUP) {
                    android.util.Slog.v(android.content.pm.PackageParser.TAG, "fullBackupContent specified as boolean=" + (v.data == 0 ? "false" : "true"));
                }
                // "false" => -1, "true" => 0
                ai.fullBackupContent = (v.data == 0) ? -1 : 0;
            }
            if (android.content.pm.PackageParser.DEBUG_BACKUP) {
                android.util.Slog.v(android.content.pm.PackageParser.TAG, (("fullBackupContent=" + ai.fullBackupContent) + " for ") + pkgName);
            }
        }
        ai.theme = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestApplication_theme, 0);
        ai.descriptionRes = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestApplication_description, 0);
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_persistent, false)) {
            // Check if persistence is based on a feature being present
            final java.lang.String requiredFeature = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestApplication_persistentWhenFeatureAvailable);
            if ((requiredFeature == null) || mCallback.hasFeature(requiredFeature)) {
                ai.flags |= android.content.pm.ApplicationInfo.FLAG_PERSISTENT;
            }
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_requiredForAllUsers, false)) {
            owner.mRequiredForAllUsers = true;
        }
        java.lang.String restrictedAccountType = sa.getString(com.android.internal.R.styleable.AndroidManifestApplication_restrictedAccountType);
        if ((restrictedAccountType != null) && (restrictedAccountType.length() > 0)) {
            owner.mRestrictedAccountType = restrictedAccountType;
        }
        java.lang.String requiredAccountType = sa.getString(com.android.internal.R.styleable.AndroidManifestApplication_requiredAccountType);
        if ((requiredAccountType != null) && (requiredAccountType.length() > 0)) {
            owner.mRequiredAccountType = requiredAccountType;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_debuggable, false)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE;
            // Debuggable implies profileable
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_PROFILEABLE_BY_SHELL;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_vmSafeMode, false)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_VM_SAFE_MODE;
        }
        owner.baseHardwareAccelerated = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_hardwareAccelerated, owner.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH);
        if (owner.baseHardwareAccelerated) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_HARDWARE_ACCELERATED;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_hasCode, true)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_HAS_CODE;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_allowTaskReparenting, false)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_allowClearUserData, true)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA;
        }
        // The parent package controls installation, hence specify test only installs.
        if (owner.parentPackage == null) {
            if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_testOnly, false)) {
                ai.flags |= android.content.pm.ApplicationInfo.FLAG_TEST_ONLY;
            }
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_largeHeap, false)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_usesCleartextTraffic, owner.applicationInfo.targetSdkVersion < Build.VERSION_CODES.P)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC;
        }
        if (/* default is no RTL support */
        sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_supportsRtl, false)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_SUPPORTS_RTL;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_multiArch, false)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_MULTIARCH;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_extractNativeLibs, true)) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_EXTRACT_NATIVE_LIBS;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestApplication_useEmbeddedDex, false)) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_USE_EMBEDDED_DEX;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestApplication_defaultToDeviceProtectedStorage, false)) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_DEFAULT_TO_DEVICE_PROTECTED_STORAGE;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestApplication_directBootAware, false)) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_DIRECT_BOOT_AWARE;
        }
        if (sa.hasValueOrEmpty(R.styleable.AndroidManifestApplication_resizeableActivity)) {
            if (sa.getBoolean(R.styleable.AndroidManifestApplication_resizeableActivity, true)) {
                ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE;
            } else {
                ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_UNRESIZEABLE;
            }
        } else
            if (owner.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.N) {
                ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION;
            }

        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_allowClearUserDataOnFailedRestore, true)) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_ALLOW_CLEAR_USER_DATA_ON_FAILED_RESTORE;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestApplication_allowAudioPlaybackCapture, owner.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q)) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_ALLOW_AUDIO_PLAYBACK_CAPTURE;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestApplication_requestLegacyExternalStorage, owner.applicationInfo.targetSdkVersion < Build.VERSION_CODES.Q)) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_REQUEST_LEGACY_EXTERNAL_STORAGE;
        }
        ai.maxAspectRatio = sa.getFloat(R.styleable.AndroidManifestApplication_maxAspectRatio, 0);
        ai.minAspectRatio = sa.getFloat(R.styleable.AndroidManifestApplication_minAspectRatio, 0);
        ai.networkSecurityConfigRes = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestApplication_networkSecurityConfig, 0);
        ai.category = sa.getInt(com.android.internal.R.styleable.AndroidManifestApplication_appCategory, android.content.pm.ApplicationInfo.CATEGORY_UNDEFINED);
        java.lang.String str;
        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestApplication_permission, 0);
        ai.permission = ((str != null) && (str.length() > 0)) ? str.intern() : null;
        if (owner.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.FROYO) {
            str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestApplication_taskAffinity, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
        } else {
            // Some older apps have been seen to use a resource reference
            // here that on older builds was ignored (with a warning).  We
            // need to continue to do this for them so they don't break.
            str = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestApplication_taskAffinity);
        }
        ai.taskAffinity = android.content.pm.PackageParser.buildTaskAffinityName(ai.packageName, ai.packageName, str, outError);
        java.lang.String factory = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestApplication_appComponentFactory);
        if (factory != null) {
            ai.appComponentFactory = android.content.pm.PackageParser.buildClassName(ai.packageName, factory, outError);
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_usesNonSdkApi, false)) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_USES_NON_SDK_API;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_hasFragileUserData, false)) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_HAS_FRAGILE_USER_DATA;
        }
        if (outError[0] == null) {
            java.lang.CharSequence pname;
            if (owner.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.FROYO) {
                pname = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestApplication_process, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
            } else {
                // Some older apps have been seen to use a resource reference
                // here that on older builds was ignored (with a warning).  We
                // need to continue to do this for them so they don't break.
                pname = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestApplication_process);
            }
            ai.processName = android.content.pm.PackageParser.buildProcessName(ai.packageName, null, pname, flags, mSeparateProcesses, outError);
            ai.enabled = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_enabled, true);
            if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_isGame, false)) {
                ai.flags |= android.content.pm.ApplicationInfo.FLAG_IS_GAME;
            }
            if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_cantSaveState, false)) {
                ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_CANT_SAVE_STATE;
                // A heavy-weight application can not be in a custom process.
                // We can do direct compare because we intern all strings.
                if ((ai.processName != null) && (!ai.processName.equals(ai.packageName))) {
                    outError[0] = "cantSaveState applications can not use custom processes";
                }
            }
        }
        ai.uiOptions = sa.getInt(com.android.internal.R.styleable.AndroidManifestApplication_uiOptions, 0);
        ai.classLoaderName = sa.getString(com.android.internal.R.styleable.AndroidManifestApplication_classLoader);
        if ((ai.classLoaderName != null) && (!com.android.internal.os.ClassLoaderFactory.isValidClassLoaderName(ai.classLoaderName))) {
            outError[0] = "Invalid class loader name: " + ai.classLoaderName;
        }
        ai.zygotePreloadName = sa.getString(com.android.internal.R.styleable.AndroidManifestApplication_zygotePreloadName);
        sa.recycle();
        if (outError[0] != null) {
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        final int innerDepth = parser.getDepth();
        // IMPORTANT: These must only be cached for a single <application> to avoid components
        // getting added to the wrong package.
        final android.content.pm.PackageParser.CachedComponentArgs cachedArgs = new android.content.pm.PackageParser.CachedComponentArgs();
        int type;
        boolean hasActivityOrder = false;
        boolean hasReceiverOrder = false;
        boolean hasServiceOrder = false;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            java.lang.String tagName = parser.getName();
            if (tagName.equals("activity")) {
                android.content.pm.PackageParser.Activity a = parseActivity(owner, res, parser, flags, outError, cachedArgs, false, owner.baseHardwareAccelerated);
                if (a == null) {
                    mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    return false;
                }
                hasActivityOrder |= a.order != 0;
                owner.activities.add(a);
            } else
                if (tagName.equals("receiver")) {
                    android.content.pm.PackageParser.Activity a = parseActivity(owner, res, parser, flags, outError, cachedArgs, true, false);
                    if (a == null) {
                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return false;
                    }
                    hasReceiverOrder |= a.order != 0;
                    owner.receivers.add(a);
                } else
                    if (tagName.equals("service")) {
                        android.content.pm.PackageParser.Service s = parseService(owner, res, parser, flags, outError, cachedArgs);
                        if (s == null) {
                            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                            return false;
                        }
                        hasServiceOrder |= s.order != 0;
                        owner.services.add(s);
                    } else
                        if (tagName.equals("provider")) {
                            android.content.pm.PackageParser.Provider p = parseProvider(owner, res, parser, flags, outError, cachedArgs);
                            if (p == null) {
                                mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                return false;
                            }
                            owner.providers.add(p);
                        } else
                            if (tagName.equals("activity-alias")) {
                                android.content.pm.PackageParser.Activity a = parseActivityAlias(owner, res, parser, flags, outError, cachedArgs);
                                if (a == null) {
                                    mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                    return false;
                                }
                                hasActivityOrder |= a.order != 0;
                                owner.activities.add(a);
                            } else
                                if (parser.getName().equals("meta-data")) {
                                    // note: application meta-data is stored off to the side, so it can
                                    // remain null in the primary copy (we like to avoid extra copies because
                                    // it can be large)
                                    if ((owner.mAppMetaData = parseMetaData(res, parser, owner.mAppMetaData, outError)) == null) {
                                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                        return false;
                                    }
                                } else
                                    if (tagName.equals("static-library")) {
                                        sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestStaticLibrary);
                                        // Note: don't allow this value to be a reference to a resource
                                        // that may change.
                                        final java.lang.String lname = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestStaticLibrary_name);
                                        final int version = sa.getInt(com.android.internal.R.styleable.AndroidManifestStaticLibrary_version, -1);
                                        final int versionMajor = sa.getInt(com.android.internal.R.styleable.AndroidManifestStaticLibrary_versionMajor, 0);
                                        sa.recycle();
                                        // Since the app canot run without a static lib - fail if malformed
                                        if ((lname == null) || (version < 0)) {
                                            outError[0] = (("Bad static-library declaration name: " + lname) + " version: ") + version;
                                            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                            return false;
                                        }
                                        if (owner.mSharedUserId != null) {
                                            outError[0] = "sharedUserId not allowed in static shared library";
                                            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                            return false;
                                        }
                                        if (owner.staticSharedLibName != null) {
                                            outError[0] = "Multiple static-shared libs for package " + pkgName;
                                            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                            return false;
                                        }
                                        owner.staticSharedLibName = lname.intern();
                                        if (version >= 0) {
                                            owner.staticSharedLibVersion = android.content.pm.PackageInfo.composeLongVersionCode(versionMajor, version);
                                        } else {
                                            owner.staticSharedLibVersion = version;
                                        }
                                        ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_STATIC_SHARED_LIBRARY;
                                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                    } else
                                        if (tagName.equals("library")) {
                                            sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestLibrary);
                                            // Note: don't allow this value to be a reference to a resource
                                            // that may change.
                                            java.lang.String lname = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestLibrary_name);
                                            sa.recycle();
                                            if (lname != null) {
                                                lname = lname.intern();
                                                if (!com.android.internal.util.ArrayUtils.contains(owner.libraryNames, lname)) {
                                                    owner.libraryNames = com.android.internal.util.ArrayUtils.add(owner.libraryNames, lname);
                                                }
                                            }
                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                        } else
                                            if (tagName.equals("uses-static-library")) {
                                                if (!parseUsesStaticLibrary(owner, res, parser, outError)) {
                                                    return false;
                                                }
                                            } else
                                                if (tagName.equals("uses-library")) {
                                                    sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestUsesLibrary);
                                                    // Note: don't allow this value to be a reference to a resource
                                                    // that may change.
                                                    java.lang.String lname = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestUsesLibrary_name);
                                                    boolean req = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestUsesLibrary_required, true);
                                                    sa.recycle();
                                                    if (lname != null) {
                                                        lname = lname.intern();
                                                        if (req) {
                                                            owner.usesLibraries = com.android.internal.util.ArrayUtils.add(owner.usesLibraries, lname);
                                                        } else {
                                                            owner.usesOptionalLibraries = com.android.internal.util.ArrayUtils.add(owner.usesOptionalLibraries, lname);
                                                        }
                                                    }
                                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                } else
                                                    if (tagName.equals("uses-package")) {
                                                        // Dependencies for app installers; we don't currently try to
                                                        // enforce this.
                                                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                    } else
                                                        if (tagName.equals("profileable")) {
                                                            sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestProfileable);
                                                            if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestProfileable_shell, false)) {
                                                                ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_PROFILEABLE_BY_SHELL;
                                                            }
                                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                        } else {
                                                            if (!android.content.pm.PackageParser.RIGID_PARSER) {
                                                                android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <application>: " + tagName) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                                continue;
                                                            } else {
                                                                outError[0] = "Bad element under <application>: " + tagName;
                                                                mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                                                return false;
                                                            }
                                                        }











        } 
        if (android.text.TextUtils.isEmpty(owner.staticSharedLibName)) {
            // Add a hidden app detail activity to normal apps which forwards user to App Details
            // page.
            android.content.pm.PackageParser.Activity a = generateAppDetailsHiddenActivity(owner, flags, outError, owner.baseHardwareAccelerated);
            owner.activities.add(a);
        }
        if (hasActivityOrder) {
            java.util.Collections.sort(owner.activities, ( a1, a2) -> java.lang.Integer.compare(a2.order, a1.order));
        }
        if (hasReceiverOrder) {
            java.util.Collections.sort(owner.receivers, ( r1, r2) -> java.lang.Integer.compare(r2.order, r1.order));
        }
        if (hasServiceOrder) {
            java.util.Collections.sort(owner.services, ( s1, s2) -> java.lang.Integer.compare(s2.order, s1.order));
        }
        // Must be ran after the entire {@link ApplicationInfo} has been fully processed and after
        // every activity info has had a chance to set it from its attributes.
        setMaxAspectRatio(owner);
        setMinAspectRatio(owner);
        if (android.content.pm.PackageParser.hasDomainURLs(owner)) {
            owner.applicationInfo.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_HAS_DOMAIN_URLS;
        } else {
            owner.applicationInfo.privateFlags &= ~android.content.pm.ApplicationInfo.PRIVATE_FLAG_HAS_DOMAIN_URLS;
        }
        return true;
    }

    /**
     * Check if one of the IntentFilter as both actions DEFAULT / VIEW and a HTTP/HTTPS data URI
     */
    private static boolean hasDomainURLs(android.content.pm.PackageParser.Package pkg) {
        if ((pkg == null) || (pkg.activities == null))
            return false;

        final java.util.ArrayList<android.content.pm.PackageParser.Activity> activities = pkg.activities;
        final int countActivities = activities.size();
        for (int n = 0; n < countActivities; n++) {
            android.content.pm.PackageParser.Activity activity = activities.get(n);
            java.util.ArrayList<android.content.pm.PackageParser.ActivityIntentInfo> filters = activity.intents;
            if (filters == null)
                continue;

            final int countFilters = filters.size();
            for (int m = 0; m < countFilters; m++) {
                android.content.pm.PackageParser.ActivityIntentInfo aii = filters.get(m);
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
     * Parse the {@code application} XML tree at the current parse location in a
     * <em>split APK</em> manifest.
     * <p>
     * Note that split APKs have many more restrictions on what they're capable
     * of doing, so many valid features of a base APK have been carefully
     * omitted here.
     */
    private boolean parseSplitApplication(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, int splitIndex, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestApplication);
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestApplication_hasCode, true)) {
            owner.splitFlags[splitIndex] |= android.content.pm.ApplicationInfo.FLAG_HAS_CODE;
        }
        final java.lang.String classLoaderName = sa.getString(com.android.internal.R.styleable.AndroidManifestApplication_classLoader);
        if ((classLoaderName == null) || com.android.internal.os.ClassLoaderFactory.isValidClassLoaderName(classLoaderName)) {
            owner.applicationInfo.splitClassLoaderNames[splitIndex] = classLoaderName;
        } else {
            outError[0] = "Invalid class loader name: " + classLoaderName;
            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        final int innerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            android.content.pm.ComponentInfo parsedComponent = null;
            // IMPORTANT: These must only be cached for a single <application> to avoid components
            // getting added to the wrong package.
            final android.content.pm.PackageParser.CachedComponentArgs cachedArgs = new android.content.pm.PackageParser.CachedComponentArgs();
            java.lang.String tagName = parser.getName();
            if (tagName.equals("activity")) {
                android.content.pm.PackageParser.Activity a = parseActivity(owner, res, parser, flags, outError, cachedArgs, false, owner.baseHardwareAccelerated);
                if (a == null) {
                    mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                    return false;
                }
                owner.activities.add(a);
                parsedComponent = a.info;
            } else
                if (tagName.equals("receiver")) {
                    android.content.pm.PackageParser.Activity a = parseActivity(owner, res, parser, flags, outError, cachedArgs, true, false);
                    if (a == null) {
                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return false;
                    }
                    owner.receivers.add(a);
                    parsedComponent = a.info;
                } else
                    if (tagName.equals("service")) {
                        android.content.pm.PackageParser.Service s = parseService(owner, res, parser, flags, outError, cachedArgs);
                        if (s == null) {
                            mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                            return false;
                        }
                        owner.services.add(s);
                        parsedComponent = s.info;
                    } else
                        if (tagName.equals("provider")) {
                            android.content.pm.PackageParser.Provider p = parseProvider(owner, res, parser, flags, outError, cachedArgs);
                            if (p == null) {
                                mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                return false;
                            }
                            owner.providers.add(p);
                            parsedComponent = p.info;
                        } else
                            if (tagName.equals("activity-alias")) {
                                android.content.pm.PackageParser.Activity a = parseActivityAlias(owner, res, parser, flags, outError, cachedArgs);
                                if (a == null) {
                                    mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                    return false;
                                }
                                owner.activities.add(a);
                                parsedComponent = a.info;
                            } else
                                if (parser.getName().equals("meta-data")) {
                                    // note: application meta-data is stored off to the side, so it can
                                    // remain null in the primary copy (we like to avoid extra copies because
                                    // it can be large)
                                    if ((owner.mAppMetaData = parseMetaData(res, parser, owner.mAppMetaData, outError)) == null) {
                                        mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                        return false;
                                    }
                                } else
                                    if (tagName.equals("uses-static-library")) {
                                        if (!parseUsesStaticLibrary(owner, res, parser, outError)) {
                                            return false;
                                        }
                                    } else
                                        if (tagName.equals("uses-library")) {
                                            sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestUsesLibrary);
                                            // Note: don't allow this value to be a reference to a resource
                                            // that may change.
                                            java.lang.String lname = sa.getNonResourceString(com.android.internal.R.styleable.AndroidManifestUsesLibrary_name);
                                            boolean req = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestUsesLibrary_required, true);
                                            sa.recycle();
                                            if (lname != null) {
                                                lname = lname.intern();
                                                if (req) {
                                                    // Upgrade to treat as stronger constraint
                                                    owner.usesLibraries = com.android.internal.util.ArrayUtils.add(owner.usesLibraries, lname);
                                                    owner.usesOptionalLibraries = com.android.internal.util.ArrayUtils.remove(owner.usesOptionalLibraries, lname);
                                                } else {
                                                    // Ignore if someone already defined as required
                                                    if (!com.android.internal.util.ArrayUtils.contains(owner.usesLibraries, lname)) {
                                                        owner.usesOptionalLibraries = com.android.internal.util.ArrayUtils.add(owner.usesOptionalLibraries, lname);
                                                    }
                                                }
                                            }
                                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                        } else
                                            if (tagName.equals("uses-package")) {
                                                // Dependencies for app installers; we don't currently try to
                                                // enforce this.
                                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                            } else {
                                                if (!android.content.pm.PackageParser.RIGID_PARSER) {
                                                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <application>: " + tagName) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                                    continue;
                                                } else {
                                                    outError[0] = "Bad element under <application>: " + tagName;
                                                    mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                                    return false;
                                                }
                                            }








            if ((parsedComponent != null) && (parsedComponent.splitName == null)) {
                // If the loaded component did not specify a split, inherit the split name
                // based on the split it is defined in.
                // This is used to later load the correct split when starting this
                // component.
                parsedComponent.splitName = owner.splitNames[splitIndex];
            }
        } 
        return true;
    }

    private static boolean parsePackageItemInfo(android.content.pm.PackageParser.Package owner, android.content.pm.PackageItemInfo outInfo, java.lang.String[] outError, java.lang.String tag, android.content.res.TypedArray sa, boolean nameRequired, int nameRes, int labelRes, int iconRes, int roundIconRes, int logoRes, int bannerRes) {
        // This case can only happen in unit tests where we sometimes need to create fakes
        // of various package parser data structures.
        if (sa == null) {
            outError[0] = tag + " does not contain any attributes";
            return false;
        }
        java.lang.String name = sa.getNonConfigurationString(nameRes, 0);
        if (name == null) {
            if (nameRequired) {
                outError[0] = tag + " does not specify android:name";
                return false;
            }
        } else {
            java.lang.String outInfoName = android.content.pm.PackageParser.buildClassName(owner.applicationInfo.packageName, name, outError);
            if (android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(outInfoName)) {
                outError[0] = tag + " invalid android:name";
                return false;
            }
            outInfo.name = outInfoName;
            if (outInfoName == null) {
                return false;
            }
        }
        int roundIconVal = (android.content.pm.PackageParser.sUseRoundIcon) ? sa.getResourceId(roundIconRes, 0) : 0;
        if (roundIconVal != 0) {
            outInfo.icon = roundIconVal;
            outInfo.nonLocalizedLabel = null;
        } else {
            int iconVal = sa.getResourceId(iconRes, 0);
            if (iconVal != 0) {
                outInfo.icon = iconVal;
                outInfo.nonLocalizedLabel = null;
            }
        }
        int logoVal = sa.getResourceId(logoRes, 0);
        if (logoVal != 0) {
            outInfo.logo = logoVal;
        }
        int bannerVal = sa.getResourceId(bannerRes, 0);
        if (bannerVal != 0) {
            outInfo.banner = bannerVal;
        }
        android.util.TypedValue v = sa.peekValue(labelRes);
        if ((v != null) && ((outInfo.labelRes = v.resourceId) == 0)) {
            outInfo.nonLocalizedLabel = v.coerceToString();
        }
        outInfo.packageName = owner.packageName;
        return true;
    }

    /**
     * Generate activity object that forwards user to App Details page automatically.
     * This activity should be invisible to user and user should not know or see it.
     */
    @android.annotation.NonNull
    private android.content.pm.PackageParser.Activity generateAppDetailsHiddenActivity(android.content.pm.PackageParser.Package owner, int flags, java.lang.String[] outError, boolean hardwareAccelerated) {
        // Build custom App Details activity info instead of parsing it from xml
        android.content.pm.PackageParser.Activity a = new android.content.pm.PackageParser.Activity(owner, android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME, new android.content.pm.ActivityInfo());
        a.owner = owner;
        a.setPackageName(owner.packageName);
        a.info.theme = android.content.pm.android.R.style;
        a.info.exported = true;
        a.info.name = android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME;
        a.info.processName = owner.applicationInfo.processName;
        a.info.uiOptions = a.info.applicationInfo.uiOptions;
        a.info.taskAffinity = android.content.pm.PackageParser.buildTaskAffinityName(owner.packageName, owner.packageName, ":app_details", outError);
        a.info.enabled = true;
        a.info.launchMode = android.content.pm.ActivityInfo.LAUNCH_MULTIPLE;
        a.info.documentLaunchMode = android.content.pm.ActivityInfo.DOCUMENT_LAUNCH_NONE;
        a.info.maxRecents = android.app.ActivityTaskManager.getDefaultAppRecentsLimitStatic();
        a.info.configChanges = android.content.pm.PackageParser.getActivityConfigChanges(0, 0);
        a.info.softInputMode = 0;
        a.info.persistableMode = android.content.pm.ActivityInfo.PERSIST_NEVER;
        a.info.screenOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        a.info.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZEABLE;
        a.info.lockTaskLaunchMode = 0;
        a.info.encryptionAware = a.info.directBootAware = false;
        a.info.rotationAnimation = android.view.WindowManager.LayoutParams.ROTATION_ANIMATION_UNSPECIFIED;
        a.info.colorMode = android.content.pm.ActivityInfo.COLOR_MODE_DEFAULT;
        if (hardwareAccelerated) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_HARDWARE_ACCELERATED;
        }
        return a;
    }

    private android.content.pm.PackageParser.Activity parseActivity(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, java.lang.String[] outError, android.content.pm.PackageParser.CachedComponentArgs cachedArgs, boolean receiver, boolean hardwareAccelerated) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestActivity);
        if (cachedArgs.mActivityArgs == null) {
            cachedArgs.mActivityArgs = new android.content.pm.PackageParser.ParseComponentArgs(owner, outError, R.styleable.AndroidManifestActivity_name, R.styleable.AndroidManifestActivity_label, R.styleable.AndroidManifestActivity_icon, R.styleable.AndroidManifestActivity_roundIcon, R.styleable.AndroidManifestActivity_logo, R.styleable.AndroidManifestActivity_banner, mSeparateProcesses, R.styleable.AndroidManifestActivity_process, R.styleable.AndroidManifestActivity_description, R.styleable.AndroidManifestActivity_enabled);
        }
        cachedArgs.mActivityArgs.tag = (receiver) ? "<receiver>" : "<activity>";
        cachedArgs.mActivityArgs.sa = sa;
        cachedArgs.mActivityArgs.flags = flags;
        android.content.pm.PackageParser.Activity a = new android.content.pm.PackageParser.Activity(cachedArgs.mActivityArgs, new android.content.pm.ActivityInfo());
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        boolean setExported = sa.hasValue(R.styleable.AndroidManifestActivity_exported);
        if (setExported) {
            a.info.exported = sa.getBoolean(R.styleable.AndroidManifestActivity_exported, false);
        }
        a.info.theme = sa.getResourceId(R.styleable.AndroidManifestActivity_theme, 0);
        a.info.uiOptions = sa.getInt(R.styleable.AndroidManifestActivity_uiOptions, a.info.applicationInfo.uiOptions);
        java.lang.String parentName = sa.getNonConfigurationString(R.styleable.AndroidManifestActivity_parentActivityName, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
        if (parentName != null) {
            java.lang.String parentClassName = android.content.pm.PackageParser.buildClassName(a.info.packageName, parentName, outError);
            if (outError[0] == null) {
                a.info.parentActivityName = parentClassName;
            } else {
                android.util.Log.e(android.content.pm.PackageParser.TAG, (("Activity " + a.info.name) + " specified invalid parentActivityName ") + parentName);
                outError[0] = null;
            }
        }
        java.lang.String str;
        str = sa.getNonConfigurationString(R.styleable.AndroidManifestActivity_permission, 0);
        if (str == null) {
            a.info.permission = owner.applicationInfo.permission;
        } else {
            a.info.permission = (str.length() > 0) ? str.toString().intern() : null;
        }
        str = sa.getNonConfigurationString(R.styleable.AndroidManifestActivity_taskAffinity, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
        a.info.taskAffinity = android.content.pm.PackageParser.buildTaskAffinityName(owner.applicationInfo.packageName, owner.applicationInfo.taskAffinity, str, outError);
        a.info.splitName = sa.getNonConfigurationString(R.styleable.AndroidManifestActivity_splitName, 0);
        a.info.flags = 0;
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_multiprocess, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_MULTIPROCESS;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_finishOnTaskLaunch, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_FINISH_ON_TASK_LAUNCH;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_clearTaskOnLaunch, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_CLEAR_TASK_ON_LAUNCH;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_noHistory, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_NO_HISTORY;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_alwaysRetainTaskState, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_ALWAYS_RETAIN_TASK_STATE;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_stateNotNeeded, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_STATE_NOT_NEEDED;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_excludeFromRecents, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_EXCLUDE_FROM_RECENTS;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_allowTaskReparenting, (owner.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING) != 0)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_ALLOW_TASK_REPARENTING;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_finishOnCloseSystemDialogs, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_showOnLockScreen, false) || sa.getBoolean(R.styleable.AndroidManifestActivity_showForAllUsers, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_SHOW_FOR_ALL_USERS;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_immersive, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_IMMERSIVE;
        }
        if (sa.getBoolean(R.styleable.AndroidManifestActivity_systemUserOnly, false)) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_SYSTEM_USER_ONLY;
        }
        if (!receiver) {
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_hardwareAccelerated, hardwareAccelerated)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_HARDWARE_ACCELERATED;
            }
            a.info.launchMode = sa.getInt(R.styleable.AndroidManifestActivity_launchMode, android.content.pm.ActivityInfo.LAUNCH_MULTIPLE);
            a.info.documentLaunchMode = sa.getInt(R.styleable.AndroidManifestActivity_documentLaunchMode, android.content.pm.ActivityInfo.DOCUMENT_LAUNCH_NONE);
            a.info.maxRecents = sa.getInt(R.styleable.AndroidManifestActivity_maxRecents, android.app.ActivityTaskManager.getDefaultAppRecentsLimitStatic());
            a.info.configChanges = android.content.pm.PackageParser.getActivityConfigChanges(sa.getInt(R.styleable.AndroidManifestActivity_configChanges, 0), sa.getInt(R.styleable.AndroidManifestActivity_recreateOnConfigChanges, 0));
            a.info.softInputMode = sa.getInt(R.styleable.AndroidManifestActivity_windowSoftInputMode, 0);
            a.info.persistableMode = sa.getInteger(R.styleable.AndroidManifestActivity_persistableMode, android.content.pm.ActivityInfo.PERSIST_ROOT_ONLY);
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_allowEmbedded, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_ALLOW_EMBEDDED;
            }
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_autoRemoveFromRecents, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_AUTO_REMOVE_FROM_RECENTS;
            }
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_relinquishTaskIdentity, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_RELINQUISH_TASK_IDENTITY;
            }
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_resumeWhilePausing, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_RESUME_WHILE_PAUSING;
            }
            a.info.screenOrientation = sa.getInt(R.styleable.AndroidManifestActivity_screenOrientation, android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            setActivityResizeMode(a.info, sa, owner);
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_supportsPictureInPicture, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_SUPPORTS_PICTURE_IN_PICTURE;
            }
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_alwaysFocusable, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_ALWAYS_FOCUSABLE;
            }
            if (sa.hasValue(R.styleable.AndroidManifestActivity_maxAspectRatio) && (sa.getType(R.styleable.AndroidManifestActivity_maxAspectRatio) == android.util.TypedValue.TYPE_FLOAT)) {
                a.setMaxAspectRatio(/* default */
                sa.getFloat(R.styleable.AndroidManifestActivity_maxAspectRatio, 0));
            }
            if (sa.hasValue(R.styleable.AndroidManifestActivity_minAspectRatio) && (sa.getType(R.styleable.AndroidManifestActivity_minAspectRatio) == android.util.TypedValue.TYPE_FLOAT)) {
                a.setMinAspectRatio(/* default */
                sa.getFloat(R.styleable.AndroidManifestActivity_minAspectRatio, 0));
            }
            a.info.lockTaskLaunchMode = sa.getInt(R.styleable.AndroidManifestActivity_lockTaskMode, 0);
            a.info.encryptionAware = a.info.directBootAware = sa.getBoolean(R.styleable.AndroidManifestActivity_directBootAware, false);
            a.info.requestedVrComponent = sa.getString(R.styleable.AndroidManifestActivity_enableVrMode);
            a.info.rotationAnimation = sa.getInt(R.styleable.AndroidManifestActivity_rotationAnimation, android.view.WindowManager.LayoutParams.ROTATION_ANIMATION_UNSPECIFIED);
            a.info.colorMode = sa.getInt(R.styleable.AndroidManifestActivity_colorMode, android.content.pm.ActivityInfo.COLOR_MODE_DEFAULT);
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_showWhenLocked, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_SHOW_WHEN_LOCKED;
            }
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_turnScreenOn, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_TURN_SCREEN_ON;
            }
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_inheritShowWhenLocked, false)) {
                a.info.privateFlags |= android.content.pm.ActivityInfo.FLAG_INHERIT_SHOW_WHEN_LOCKED;
            }
        } else {
            a.info.launchMode = android.content.pm.ActivityInfo.LAUNCH_MULTIPLE;
            a.info.configChanges = 0;
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_singleUser, false)) {
                a.info.flags |= android.content.pm.ActivityInfo.FLAG_SINGLE_USER;
            }
            a.info.encryptionAware = a.info.directBootAware = sa.getBoolean(R.styleable.AndroidManifestActivity_directBootAware, false);
        }
        if (a.info.directBootAware) {
            owner.applicationInfo.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_PARTIALLY_DIRECT_BOOT_AWARE;
        }
        // can't make this final; we may set it later via meta-data
        boolean visibleToEphemeral = sa.getBoolean(R.styleable.AndroidManifestActivity_visibleToInstantApps, false);
        if (visibleToEphemeral) {
            a.info.flags |= android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP;
            owner.visibleToInstantApps = true;
        }
        sa.recycle();
        if (receiver && ((owner.applicationInfo.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_CANT_SAVE_STATE) != 0)) {
            // A heavy-weight application can not have receives in its main process
            // We can do direct compare because we intern all strings.
            if (a.info.processName == owner.packageName) {
                outError[0] = "Heavy-weight applications can not have receivers in main process";
            }
        }
        if (outError[0] != null) {
            return null;
        }
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            if (parser.getName().equals("intent-filter")) {
                android.content.pm.PackageParser.ActivityIntentInfo intent = new android.content.pm.PackageParser.ActivityIntentInfo(a);
                if (!/* allowGlobs */
                /* allowAutoVerify */
                parseIntent(res, parser, true, true, intent, outError)) {
                    return null;
                }
                if (intent.countActions() == 0) {
                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (("No actions in intent filter at " + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                } else {
                    a.order = java.lang.Math.max(intent.getOrder(), a.order);
                    a.intents.add(intent);
                }
                // adjust activity flags when we implicitly expose it via a browsable filter
                final int visibility = (visibleToEphemeral) ? android.content.IntentFilter.VISIBILITY_EXPLICIT : (!receiver) && isImplicitlyExposedIntent(intent) ? android.content.IntentFilter.VISIBILITY_IMPLICIT : android.content.IntentFilter.VISIBILITY_NONE;
                intent.setVisibilityToInstantApp(visibility);
                if (intent.isVisibleToInstantApp()) {
                    a.info.flags |= android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP;
                }
                if (intent.isImplicitlyVisibleToInstantApp()) {
                    a.info.flags |= android.content.pm.ActivityInfo.FLAG_IMPLICITLY_VISIBLE_TO_INSTANT_APP;
                }
                if ((android.content.pm.PackageParser.LOG_UNSAFE_BROADCASTS && receiver) && (owner.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.O)) {
                    for (int i = 0; i < intent.countActions(); i++) {
                        final java.lang.String action = intent.getAction(i);
                        if ((action == null) || (!action.startsWith("android.")))
                            continue;

                        if (!android.content.pm.PackageParser.SAFE_BROADCASTS.contains(action)) {
                            android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Broadcast " + action) + " may never be delivered to ") + owner.packageName) + " as requested at: ") + parser.getPositionDescription());
                        }
                    }
                }
            } else
                if ((!receiver) && parser.getName().equals("preferred")) {
                    android.content.pm.PackageParser.ActivityIntentInfo intent = new android.content.pm.PackageParser.ActivityIntentInfo(a);
                    if (!/* allowGlobs */
                    /* allowAutoVerify */
                    parseIntent(res, parser, false, false, intent, outError)) {
                        return null;
                    }
                    if (intent.countActions() == 0) {
                        android.util.Slog.w(android.content.pm.PackageParser.TAG, (("No actions in preferred at " + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                    } else {
                        if (owner.preferredActivityFilters == null) {
                            owner.preferredActivityFilters = new java.util.ArrayList<android.content.pm.PackageParser.ActivityIntentInfo>();
                        }
                        owner.preferredActivityFilters.add(intent);
                    }
                    // adjust activity flags when we implicitly expose it via a browsable filter
                    final int visibility = (visibleToEphemeral) ? android.content.IntentFilter.VISIBILITY_EXPLICIT : (!receiver) && isImplicitlyExposedIntent(intent) ? android.content.IntentFilter.VISIBILITY_IMPLICIT : android.content.IntentFilter.VISIBILITY_NONE;
                    intent.setVisibilityToInstantApp(visibility);
                    if (intent.isVisibleToInstantApp()) {
                        a.info.flags |= android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP;
                    }
                    if (intent.isImplicitlyVisibleToInstantApp()) {
                        a.info.flags |= android.content.pm.ActivityInfo.FLAG_IMPLICITLY_VISIBLE_TO_INSTANT_APP;
                    }
                } else
                    if (parser.getName().equals("meta-data")) {
                        if ((a.metaData = parseMetaData(res, parser, a.metaData, outError)) == null) {
                            return null;
                        }
                    } else
                        if ((!receiver) && parser.getName().equals("layout")) {
                            parseLayout(res, parser, a);
                        } else {
                            if (!android.content.pm.PackageParser.RIGID_PARSER) {
                                android.util.Slog.w(android.content.pm.PackageParser.TAG, ("Problem in package " + mArchiveSourcePath) + ":");
                                if (receiver) {
                                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <receiver>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                } else {
                                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <activity>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                }
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                continue;
                            } else {
                                if (receiver) {
                                    outError[0] = "Bad element under <receiver>: " + parser.getName();
                                } else {
                                    outError[0] = "Bad element under <activity>: " + parser.getName();
                                }
                                return null;
                            }
                        }



        } 
        if (!setExported) {
            a.info.exported = a.intents.size() > 0;
        }
        return a;
    }

    private void setActivityResizeMode(android.content.pm.ActivityInfo aInfo, android.content.res.TypedArray sa, android.content.pm.PackageParser.Package owner) {
        final boolean appExplicitDefault = (owner.applicationInfo.privateFlags & (android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE | android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_UNRESIZEABLE)) != 0;
        if (sa.hasValue(R.styleable.AndroidManifestActivity_resizeableActivity) || appExplicitDefault) {
            // Activity or app explicitly set if it is resizeable or not;
            final boolean appResizeable = (owner.applicationInfo.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE) != 0;
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_resizeableActivity, appResizeable)) {
                aInfo.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE;
            } else {
                aInfo.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_UNRESIZEABLE;
            }
            return;
        }
        if ((owner.applicationInfo.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION) != 0) {
            // The activity or app didn't explicitly set the resizing option, however we want to
            // make it resize due to the sdk version it is targeting.
            aInfo.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION;
            return;
        }
        // resize preference isn't set and target sdk version doesn't support resizing apps by
        // default. For the app to be resizeable if it isn't fixed orientation or immersive.
        if (aInfo.isFixedOrientationPortrait()) {
            aInfo.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZABLE_PORTRAIT_ONLY;
        } else
            if (aInfo.isFixedOrientationLandscape()) {
                aInfo.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZABLE_LANDSCAPE_ONLY;
            } else
                if (aInfo.isFixedOrientation()) {
                    aInfo.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZABLE_PRESERVE_ORIENTATION;
                } else {
                    aInfo.resizeMode = android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZEABLE;
                }


    }

    /**
     * Sets every the max aspect ratio of every child activity that doesn't already have an aspect
     * ratio set.
     */
    private void setMaxAspectRatio(android.content.pm.PackageParser.Package owner) {
        // Default to (1.86) 16.7:9 aspect ratio for pre-O apps and unset for O and greater.
        // NOTE: 16.7:9 was the max aspect ratio Android devices can support pre-O per the CDD.
        float maxAspectRatio = (owner.applicationInfo.targetSdkVersion < android.os.Build.VERSION_CODES.O) ? android.content.pm.PackageParser.DEFAULT_PRE_O_MAX_ASPECT_RATIO : 0;
        if (owner.applicationInfo.maxAspectRatio != 0) {
            // Use the application max aspect ration as default if set.
            maxAspectRatio = owner.applicationInfo.maxAspectRatio;
        } else
            if ((owner.mAppMetaData != null) && owner.mAppMetaData.containsKey(android.content.pm.PackageParser.METADATA_MAX_ASPECT_RATIO)) {
                maxAspectRatio = owner.mAppMetaData.getFloat(android.content.pm.PackageParser.METADATA_MAX_ASPECT_RATIO, maxAspectRatio);
            }

        for (android.content.pm.PackageParser.Activity activity : owner.activities) {
            // If the max aspect ratio for the activity has already been set, skip.
            if (activity.hasMaxAspectRatio()) {
                continue;
            }
            // By default we prefer to use a values defined on the activity directly than values
            // defined on the application. We do not check the styled attributes on the activity
            // as it would have already been set when we processed the activity. We wait to process
            // the meta data here since this method is called at the end of processing the
            // application and all meta data is guaranteed.
            final float activityAspectRatio = (activity.metaData != null) ? activity.metaData.getFloat(android.content.pm.PackageParser.METADATA_MAX_ASPECT_RATIO, maxAspectRatio) : maxAspectRatio;
            activity.setMaxAspectRatio(activityAspectRatio);
        }
    }

    /**
     * Sets every the min aspect ratio of every child activity that doesn't already have an aspect
     * ratio set.
     */
    private void setMinAspectRatio(android.content.pm.PackageParser.Package owner) {
        final float minAspectRatio;
        if (owner.applicationInfo.minAspectRatio != 0) {
            // Use the application max aspect ration as default if set.
            minAspectRatio = owner.applicationInfo.minAspectRatio;
        } else {
            // Default to (1.33) 4:3 aspect ratio for pre-Q apps and unset for Q and greater.
            // NOTE: 4:3 was the min aspect ratio Android devices can support pre-Q per the CDD,
            // except for watches which always supported 1:1.
            minAspectRatio = (owner.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q) ? 0 : (mCallback != null) && mCallback.hasFeature(android.content.pm.PackageManager.FEATURE_WATCH) ? android.content.pm.PackageParser.DEFAULT_PRE_Q_MIN_ASPECT_RATIO_WATCH : android.content.pm.PackageParser.DEFAULT_PRE_Q_MIN_ASPECT_RATIO;
        }
        for (android.content.pm.PackageParser.Activity activity : owner.activities) {
            if (activity.hasMinAspectRatio()) {
                continue;
            }
            activity.setMinAspectRatio(minAspectRatio);
        }
    }

    /**
     *
     *
     * @param configChanges
     * 		The bit mask of configChanges fetched from AndroidManifest.xml.
     * @param recreateOnConfigChanges
     * 		The bit mask recreateOnConfigChanges fetched from
     * 		AndroidManifest.xml.
     * @unknown Exposed for unit testing only.
     */
    @android.annotation.TestApi
    public static int getActivityConfigChanges(int configChanges, int recreateOnConfigChanges) {
        return configChanges | ((~recreateOnConfigChanges) & android.content.pm.PackageParser.RECREATE_ON_CONFIG_CHANGES_MASK);
    }

    private void parseLayout(android.content.res.Resources res, android.util.AttributeSet attrs, android.content.pm.PackageParser.Activity a) {
        android.content.res.TypedArray sw = res.obtainAttributes(attrs, com.android.internal.R.styleable.AndroidManifestLayout);
        int width = -1;
        float widthFraction = -1.0F;
        int height = -1;
        float heightFraction = -1.0F;
        final int widthType = sw.getType(com.android.internal.R.styleable.AndroidManifestLayout_defaultWidth);
        if (widthType == android.util.TypedValue.TYPE_FRACTION) {
            widthFraction = sw.getFraction(com.android.internal.R.styleable.AndroidManifestLayout_defaultWidth, 1, 1, -1);
        } else
            if (widthType == android.util.TypedValue.TYPE_DIMENSION) {
                width = sw.getDimensionPixelSize(com.android.internal.R.styleable.AndroidManifestLayout_defaultWidth, -1);
            }

        final int heightType = sw.getType(com.android.internal.R.styleable.AndroidManifestLayout_defaultHeight);
        if (heightType == android.util.TypedValue.TYPE_FRACTION) {
            heightFraction = sw.getFraction(com.android.internal.R.styleable.AndroidManifestLayout_defaultHeight, 1, 1, -1);
        } else
            if (heightType == android.util.TypedValue.TYPE_DIMENSION) {
                height = sw.getDimensionPixelSize(com.android.internal.R.styleable.AndroidManifestLayout_defaultHeight, -1);
            }

        int gravity = sw.getInt(com.android.internal.R.styleable.AndroidManifestLayout_gravity, android.view.Gravity.CENTER);
        int minWidth = sw.getDimensionPixelSize(com.android.internal.R.styleable.AndroidManifestLayout_minWidth, -1);
        int minHeight = sw.getDimensionPixelSize(com.android.internal.R.styleable.AndroidManifestLayout_minHeight, -1);
        sw.recycle();
        a.info.windowLayout = new android.content.pm.ActivityInfo.WindowLayout(width, widthFraction, height, heightFraction, gravity, minWidth, minHeight);
    }

    private android.content.pm.PackageParser.Activity parseActivityAlias(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, java.lang.String[] outError, android.content.pm.PackageParser.CachedComponentArgs cachedArgs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestActivityAlias);
        java.lang.String targetActivity = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestActivityAlias_targetActivity, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
        if (targetActivity == null) {
            outError[0] = "<activity-alias> does not specify android:targetActivity";
            sa.recycle();
            return null;
        }
        targetActivity = android.content.pm.PackageParser.buildClassName(owner.applicationInfo.packageName, targetActivity, outError);
        if (targetActivity == null) {
            sa.recycle();
            return null;
        }
        if (cachedArgs.mActivityAliasArgs == null) {
            cachedArgs.mActivityAliasArgs = new android.content.pm.PackageParser.ParseComponentArgs(owner, outError, com.android.internal.R.styleable.AndroidManifestActivityAlias_name, com.android.internal.R.styleable.AndroidManifestActivityAlias_label, com.android.internal.R.styleable.AndroidManifestActivityAlias_icon, com.android.internal.R.styleable.AndroidManifestActivityAlias_roundIcon, com.android.internal.R.styleable.AndroidManifestActivityAlias_logo, com.android.internal.R.styleable.AndroidManifestActivityAlias_banner, mSeparateProcesses, 0, com.android.internal.R.styleable.AndroidManifestActivityAlias_description, com.android.internal.R.styleable.AndroidManifestActivityAlias_enabled);
            cachedArgs.mActivityAliasArgs.tag = "<activity-alias>";
        }
        cachedArgs.mActivityAliasArgs.sa = sa;
        cachedArgs.mActivityAliasArgs.flags = flags;
        android.content.pm.PackageParser.Activity target = null;
        final int NA = owner.activities.size();
        for (int i = 0; i < NA; i++) {
            android.content.pm.PackageParser.Activity t = owner.activities.get(i);
            if (targetActivity.equals(t.info.name)) {
                target = t;
                break;
            }
        }
        if (target == null) {
            outError[0] = ("<activity-alias> target activity " + targetActivity) + " not found in manifest";
            sa.recycle();
            return null;
        }
        android.content.pm.ActivityInfo info = new android.content.pm.ActivityInfo();
        info.targetActivity = targetActivity;
        info.configChanges = target.info.configChanges;
        info.flags = target.info.flags;
        info.privateFlags = target.info.privateFlags;
        info.icon = target.info.icon;
        info.logo = target.info.logo;
        info.banner = target.info.banner;
        info.labelRes = target.info.labelRes;
        info.nonLocalizedLabel = target.info.nonLocalizedLabel;
        info.launchMode = target.info.launchMode;
        info.lockTaskLaunchMode = target.info.lockTaskLaunchMode;
        info.processName = target.info.processName;
        if (info.descriptionRes == 0) {
            info.descriptionRes = target.info.descriptionRes;
        }
        info.screenOrientation = target.info.screenOrientation;
        info.taskAffinity = target.info.taskAffinity;
        info.theme = target.info.theme;
        info.softInputMode = target.info.softInputMode;
        info.uiOptions = target.info.uiOptions;
        info.parentActivityName = target.info.parentActivityName;
        info.maxRecents = target.info.maxRecents;
        info.windowLayout = target.info.windowLayout;
        info.resizeMode = target.info.resizeMode;
        info.maxAspectRatio = target.info.maxAspectRatio;
        info.minAspectRatio = target.info.minAspectRatio;
        info.requestedVrComponent = target.info.requestedVrComponent;
        info.encryptionAware = info.directBootAware = target.info.directBootAware;
        android.content.pm.PackageParser.Activity a = new android.content.pm.PackageParser.Activity(cachedArgs.mActivityAliasArgs, info);
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        final boolean setExported = sa.hasValue(com.android.internal.R.styleable.AndroidManifestActivityAlias_exported);
        if (setExported) {
            a.info.exported = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestActivityAlias_exported, false);
        }
        java.lang.String str;
        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestActivityAlias_permission, 0);
        if (str != null) {
            a.info.permission = (str.length() > 0) ? str.toString().intern() : null;
        }
        java.lang.String parentName = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestActivityAlias_parentActivityName, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
        if (parentName != null) {
            java.lang.String parentClassName = android.content.pm.PackageParser.buildClassName(a.info.packageName, parentName, outError);
            if (outError[0] == null) {
                a.info.parentActivityName = parentClassName;
            } else {
                android.util.Log.e(android.content.pm.PackageParser.TAG, (("Activity alias " + a.info.name) + " specified invalid parentActivityName ") + parentName);
                outError[0] = null;
            }
        }
        // TODO add visibleToInstantApps attribute to activity alias
        final boolean visibleToEphemeral = (a.info.flags & android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP) != 0;
        sa.recycle();
        if (outError[0] != null) {
            return null;
        }
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            if (parser.getName().equals("intent-filter")) {
                android.content.pm.PackageParser.ActivityIntentInfo intent = new android.content.pm.PackageParser.ActivityIntentInfo(a);
                if (!/* allowGlobs */
                /* allowAutoVerify */
                parseIntent(res, parser, true, true, intent, outError)) {
                    return null;
                }
                if (intent.countActions() == 0) {
                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (("No actions in intent filter at " + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                } else {
                    a.order = java.lang.Math.max(intent.getOrder(), a.order);
                    a.intents.add(intent);
                }
                // adjust activity flags when we implicitly expose it via a browsable filter
                final int visibility = (visibleToEphemeral) ? android.content.IntentFilter.VISIBILITY_EXPLICIT : isImplicitlyExposedIntent(intent) ? android.content.IntentFilter.VISIBILITY_IMPLICIT : android.content.IntentFilter.VISIBILITY_NONE;
                intent.setVisibilityToInstantApp(visibility);
                if (intent.isVisibleToInstantApp()) {
                    a.info.flags |= android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP;
                }
                if (intent.isImplicitlyVisibleToInstantApp()) {
                    a.info.flags |= android.content.pm.ActivityInfo.FLAG_IMPLICITLY_VISIBLE_TO_INSTANT_APP;
                }
            } else
                if (parser.getName().equals("meta-data")) {
                    if ((a.metaData = parseMetaData(res, parser, a.metaData, outError)) == null) {
                        return null;
                    }
                } else {
                    if (!android.content.pm.PackageParser.RIGID_PARSER) {
                        android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <activity-alias>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        continue;
                    } else {
                        outError[0] = "Bad element under <activity-alias>: " + parser.getName();
                        return null;
                    }
                }

        } 
        if (!setExported) {
            a.info.exported = a.intents.size() > 0;
        }
        return a;
    }

    private android.content.pm.PackageParser.Provider parseProvider(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, java.lang.String[] outError, android.content.pm.PackageParser.CachedComponentArgs cachedArgs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestProvider);
        if (cachedArgs.mProviderArgs == null) {
            cachedArgs.mProviderArgs = new android.content.pm.PackageParser.ParseComponentArgs(owner, outError, com.android.internal.R.styleable.AndroidManifestProvider_name, com.android.internal.R.styleable.AndroidManifestProvider_label, com.android.internal.R.styleable.AndroidManifestProvider_icon, com.android.internal.R.styleable.AndroidManifestProvider_roundIcon, com.android.internal.R.styleable.AndroidManifestProvider_logo, com.android.internal.R.styleable.AndroidManifestProvider_banner, mSeparateProcesses, com.android.internal.R.styleable.AndroidManifestProvider_process, com.android.internal.R.styleable.AndroidManifestProvider_description, com.android.internal.R.styleable.AndroidManifestProvider_enabled);
            cachedArgs.mProviderArgs.tag = "<provider>";
        }
        cachedArgs.mProviderArgs.sa = sa;
        cachedArgs.mProviderArgs.flags = flags;
        android.content.pm.PackageParser.Provider p = new android.content.pm.PackageParser.Provider(cachedArgs.mProviderArgs, new android.content.pm.ProviderInfo());
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        boolean providerExportedDefault = false;
        if (owner.applicationInfo.targetSdkVersion < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // For compatibility, applications targeting API level 16 or lower
            // should have their content providers exported by default, unless they
            // specify otherwise.
            providerExportedDefault = true;
        }
        p.info.exported = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestProvider_exported, providerExportedDefault);
        java.lang.String cpname = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestProvider_authorities, 0);
        p.info.isSyncable = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestProvider_syncable, false);
        java.lang.String permission = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestProvider_permission, 0);
        java.lang.String str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestProvider_readPermission, 0);
        if (str == null) {
            str = permission;
        }
        if (str == null) {
            p.info.readPermission = owner.applicationInfo.permission;
        } else {
            p.info.readPermission = (str.length() > 0) ? str.toString().intern() : null;
        }
        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestProvider_writePermission, 0);
        if (str == null) {
            str = permission;
        }
        if (str == null) {
            p.info.writePermission = owner.applicationInfo.permission;
        } else {
            p.info.writePermission = (str.length() > 0) ? str.toString().intern() : null;
        }
        p.info.grantUriPermissions = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestProvider_grantUriPermissions, false);
        p.info.forceUriPermissions = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestProvider_forceUriPermissions, false);
        p.info.multiprocess = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestProvider_multiprocess, false);
        p.info.initOrder = sa.getInt(com.android.internal.R.styleable.AndroidManifestProvider_initOrder, 0);
        p.info.splitName = sa.getNonConfigurationString(R.styleable.AndroidManifestProvider_splitName, 0);
        p.info.flags = 0;
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestProvider_singleUser, false)) {
            p.info.flags |= android.content.pm.ProviderInfo.FLAG_SINGLE_USER;
        }
        p.info.encryptionAware = p.info.directBootAware = sa.getBoolean(R.styleable.AndroidManifestProvider_directBootAware, false);
        if (p.info.directBootAware) {
            owner.applicationInfo.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_PARTIALLY_DIRECT_BOOT_AWARE;
        }
        final boolean visibleToEphemeral = sa.getBoolean(R.styleable.AndroidManifestProvider_visibleToInstantApps, false);
        if (visibleToEphemeral) {
            p.info.flags |= android.content.pm.ProviderInfo.FLAG_VISIBLE_TO_INSTANT_APP;
            owner.visibleToInstantApps = true;
        }
        sa.recycle();
        if ((owner.applicationInfo.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_CANT_SAVE_STATE) != 0) {
            // A heavy-weight application can not have providers in its main process
            // We can do direct compare because we intern all strings.
            if (p.info.processName == owner.packageName) {
                outError[0] = "Heavy-weight applications can not have providers in main process";
                return null;
            }
        }
        if (cpname == null) {
            outError[0] = "<provider> does not include authorities attribute";
            return null;
        }
        if (cpname.length() <= 0) {
            outError[0] = "<provider> has empty authorities attribute";
            return null;
        }
        p.info.authority = cpname.intern();
        if (!parseProviderTags(res, parser, visibleToEphemeral, p, outError)) {
            return null;
        }
        return p;
    }

    private boolean parseProviderTags(android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean visibleToEphemeral, android.content.pm.PackageParser.Provider outInfo, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            if (parser.getName().equals("intent-filter")) {
                android.content.pm.PackageParser.ProviderIntentInfo intent = new android.content.pm.PackageParser.ProviderIntentInfo(outInfo);
                if (!/* allowGlobs */
                /* allowAutoVerify */
                parseIntent(res, parser, true, false, intent, outError)) {
                    return false;
                }
                if (visibleToEphemeral) {
                    intent.setVisibilityToInstantApp(android.content.IntentFilter.VISIBILITY_EXPLICIT);
                    outInfo.info.flags |= android.content.pm.ProviderInfo.FLAG_VISIBLE_TO_INSTANT_APP;
                }
                outInfo.order = java.lang.Math.max(intent.getOrder(), outInfo.order);
                outInfo.intents.add(intent);
            } else
                if (parser.getName().equals("meta-data")) {
                    if ((outInfo.metaData = parseMetaData(res, parser, outInfo.metaData, outError)) == null) {
                        return false;
                    }
                } else
                    if (parser.getName().equals("grant-uri-permission")) {
                        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestGrantUriPermission);
                        android.os.PatternMatcher pa = null;
                        java.lang.String str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestGrantUriPermission_path, 0);
                        if (str != null) {
                            pa = new android.os.PatternMatcher(str, android.os.PatternMatcher.PATTERN_LITERAL);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestGrantUriPermission_pathPrefix, 0);
                        if (str != null) {
                            pa = new android.os.PatternMatcher(str, android.os.PatternMatcher.PATTERN_PREFIX);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestGrantUriPermission_pathPattern, 0);
                        if (str != null) {
                            pa = new android.os.PatternMatcher(str, android.os.PatternMatcher.PATTERN_SIMPLE_GLOB);
                        }
                        sa.recycle();
                        if (pa != null) {
                            if (outInfo.info.uriPermissionPatterns == null) {
                                outInfo.info.uriPermissionPatterns = new android.os.PatternMatcher[1];
                                outInfo.info.uriPermissionPatterns[0] = pa;
                            } else {
                                final int N = outInfo.info.uriPermissionPatterns.length;
                                android.os.PatternMatcher[] newp = new android.os.PatternMatcher[N + 1];
                                java.lang.System.arraycopy(outInfo.info.uriPermissionPatterns, 0, newp, 0, N);
                                newp[N] = pa;
                                outInfo.info.uriPermissionPatterns = newp;
                            }
                            outInfo.info.grantUriPermissions = true;
                        } else {
                            if (!android.content.pm.PackageParser.RIGID_PARSER) {
                                android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <path-permission>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                continue;
                            } else {
                                outError[0] = "No path, pathPrefix, or pathPattern for <path-permission>";
                                return false;
                            }
                        }
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    } else
                        if (parser.getName().equals("path-permission")) {
                            android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestPathPermission);
                            android.content.pm.PathPermission pa = null;
                            java.lang.String permission = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestPathPermission_permission, 0);
                            java.lang.String readPermission = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestPathPermission_readPermission, 0);
                            if (readPermission == null) {
                                readPermission = permission;
                            }
                            java.lang.String writePermission = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestPathPermission_writePermission, 0);
                            if (writePermission == null) {
                                writePermission = permission;
                            }
                            boolean havePerm = false;
                            if (readPermission != null) {
                                readPermission = readPermission.intern();
                                havePerm = true;
                            }
                            if (writePermission != null) {
                                writePermission = writePermission.intern();
                                havePerm = true;
                            }
                            if (!havePerm) {
                                if (!android.content.pm.PackageParser.RIGID_PARSER) {
                                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("No readPermission or writePermssion for <path-permission>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                    continue;
                                } else {
                                    outError[0] = "No readPermission or writePermssion for <path-permission>";
                                    return false;
                                }
                            }
                            java.lang.String path = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestPathPermission_path, 0);
                            if (path != null) {
                                pa = new android.content.pm.PathPermission(path, android.os.PatternMatcher.PATTERN_LITERAL, readPermission, writePermission);
                            }
                            path = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestPathPermission_pathPrefix, 0);
                            if (path != null) {
                                pa = new android.content.pm.PathPermission(path, android.os.PatternMatcher.PATTERN_PREFIX, readPermission, writePermission);
                            }
                            path = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestPathPermission_pathPattern, 0);
                            if (path != null) {
                                pa = new android.content.pm.PathPermission(path, android.os.PatternMatcher.PATTERN_SIMPLE_GLOB, readPermission, writePermission);
                            }
                            path = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestPathPermission_pathAdvancedPattern, 0);
                            if (path != null) {
                                pa = new android.content.pm.PathPermission(path, android.os.PatternMatcher.PATTERN_ADVANCED_GLOB, readPermission, writePermission);
                            }
                            sa.recycle();
                            if (pa != null) {
                                if (outInfo.info.pathPermissions == null) {
                                    outInfo.info.pathPermissions = new android.content.pm.PathPermission[1];
                                    outInfo.info.pathPermissions[0] = pa;
                                } else {
                                    final int N = outInfo.info.pathPermissions.length;
                                    android.content.pm.PathPermission[] newp = new android.content.pm.PathPermission[N + 1];
                                    java.lang.System.arraycopy(outInfo.info.pathPermissions, 0, newp, 0, N);
                                    newp[N] = pa;
                                    outInfo.info.pathPermissions = newp;
                                }
                            } else {
                                if (!android.content.pm.PackageParser.RIGID_PARSER) {
                                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("No path, pathPrefix, or pathPattern for <path-permission>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                    continue;
                                }
                                outError[0] = "No path, pathPrefix, or pathPattern for <path-permission>";
                                return false;
                            }
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        } else {
                            if (!android.content.pm.PackageParser.RIGID_PARSER) {
                                android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <provider>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                continue;
                            } else {
                                outError[0] = "Bad element under <provider>: " + parser.getName();
                                return false;
                            }
                        }



        } 
        return true;
    }

    private android.content.pm.PackageParser.Service parseService(android.content.pm.PackageParser.Package owner, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, java.lang.String[] outError, android.content.pm.PackageParser.CachedComponentArgs cachedArgs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestService);
        if (cachedArgs.mServiceArgs == null) {
            cachedArgs.mServiceArgs = new android.content.pm.PackageParser.ParseComponentArgs(owner, outError, com.android.internal.R.styleable.AndroidManifestService_name, com.android.internal.R.styleable.AndroidManifestService_label, com.android.internal.R.styleable.AndroidManifestService_icon, com.android.internal.R.styleable.AndroidManifestService_roundIcon, com.android.internal.R.styleable.AndroidManifestService_logo, com.android.internal.R.styleable.AndroidManifestService_banner, mSeparateProcesses, com.android.internal.R.styleable.AndroidManifestService_process, com.android.internal.R.styleable.AndroidManifestService_description, com.android.internal.R.styleable.AndroidManifestService_enabled);
            cachedArgs.mServiceArgs.tag = "<service>";
        }
        cachedArgs.mServiceArgs.sa = sa;
        cachedArgs.mServiceArgs.flags = flags;
        android.content.pm.PackageParser.Service s = new android.content.pm.PackageParser.Service(cachedArgs.mServiceArgs, new android.content.pm.ServiceInfo());
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        boolean setExported = sa.hasValue(com.android.internal.R.styleable.AndroidManifestService_exported);
        if (setExported) {
            s.info.exported = sa.getBoolean(com.android.internal.R.styleable.AndroidManifestService_exported, false);
        }
        java.lang.String str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestService_permission, 0);
        if (str == null) {
            s.info.permission = owner.applicationInfo.permission;
        } else {
            s.info.permission = (str.length() > 0) ? str.toString().intern() : null;
        }
        s.info.splitName = sa.getNonConfigurationString(R.styleable.AndroidManifestService_splitName, 0);
        s.info.mForegroundServiceType = sa.getInt(com.android.internal.R.styleable.AndroidManifestService_foregroundServiceType, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE);
        s.info.flags = 0;
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestService_stopWithTask, false)) {
            s.info.flags |= android.content.pm.ServiceInfo.FLAG_STOP_WITH_TASK;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestService_isolatedProcess, false)) {
            s.info.flags |= android.content.pm.ServiceInfo.FLAG_ISOLATED_PROCESS;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestService_externalService, false)) {
            s.info.flags |= android.content.pm.ServiceInfo.FLAG_EXTERNAL_SERVICE;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestService_useAppZygote, false)) {
            s.info.flags |= android.content.pm.ServiceInfo.FLAG_USE_APP_ZYGOTE;
        }
        if (sa.getBoolean(com.android.internal.R.styleable.AndroidManifestService_singleUser, false)) {
            s.info.flags |= android.content.pm.ServiceInfo.FLAG_SINGLE_USER;
        }
        s.info.encryptionAware = s.info.directBootAware = sa.getBoolean(R.styleable.AndroidManifestService_directBootAware, false);
        if (s.info.directBootAware) {
            owner.applicationInfo.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_PARTIALLY_DIRECT_BOOT_AWARE;
        }
        boolean visibleToEphemeral = sa.getBoolean(R.styleable.AndroidManifestService_visibleToInstantApps, false);
        if (visibleToEphemeral) {
            s.info.flags |= android.content.pm.ServiceInfo.FLAG_VISIBLE_TO_INSTANT_APP;
            owner.visibleToInstantApps = true;
        }
        sa.recycle();
        if ((owner.applicationInfo.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_CANT_SAVE_STATE) != 0) {
            // A heavy-weight application can not have services in its main process
            // We can do direct compare because we intern all strings.
            if (s.info.processName == owner.packageName) {
                outError[0] = "Heavy-weight applications can not have services in main process";
                return null;
            }
        }
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            if (parser.getName().equals("intent-filter")) {
                android.content.pm.PackageParser.ServiceIntentInfo intent = new android.content.pm.PackageParser.ServiceIntentInfo(s);
                if (!/* allowGlobs */
                /* allowAutoVerify */
                parseIntent(res, parser, true, false, intent, outError)) {
                    return null;
                }
                if (visibleToEphemeral) {
                    intent.setVisibilityToInstantApp(android.content.IntentFilter.VISIBILITY_EXPLICIT);
                    s.info.flags |= android.content.pm.ServiceInfo.FLAG_VISIBLE_TO_INSTANT_APP;
                }
                s.order = java.lang.Math.max(intent.getOrder(), s.order);
                s.intents.add(intent);
            } else
                if (parser.getName().equals("meta-data")) {
                    if ((s.metaData = parseMetaData(res, parser, s.metaData, outError)) == null) {
                        return null;
                    }
                } else {
                    if (!android.content.pm.PackageParser.RIGID_PARSER) {
                        android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <service>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        continue;
                    } else {
                        outError[0] = "Bad element under <service>: " + parser.getName();
                        return null;
                    }
                }

        } 
        if (!setExported) {
            s.info.exported = s.intents.size() > 0;
        }
        return s;
    }

    private boolean isImplicitlyExposedIntent(android.content.pm.PackageParser.IntentInfo intent) {
        return ((intent.hasCategory(android.content.Intent.CATEGORY_BROWSABLE) || intent.hasAction(android.content.Intent.ACTION_SEND)) || intent.hasAction(android.content.Intent.ACTION_SENDTO)) || intent.hasAction(android.content.Intent.ACTION_SEND_MULTIPLE);
    }

    private boolean parseAllMetaData(android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String tag, android.content.pm.PackageParser.Component<?> outInfo, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            if (parser.getName().equals("meta-data")) {
                if ((outInfo.metaData = parseMetaData(res, parser, outInfo.metaData, outError)) == null) {
                    return false;
                }
            } else {
                if (!android.content.pm.PackageParser.RIGID_PARSER) {
                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (((((("Unknown element under " + tag) + ": ") + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    continue;
                } else {
                    outError[0] = (("Bad element under " + tag) + ": ") + parser.getName();
                    return false;
                }
            }
        } 
        return true;
    }

    private android.os.Bundle parseMetaData(android.content.res.Resources res, android.content.res.XmlResourceParser parser, android.os.Bundle data, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestMetaData);
        if (data == null) {
            data = new android.os.Bundle();
        }
        java.lang.String name = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestMetaData_name, 0);
        if (name == null) {
            outError[0] = "<meta-data> requires an android:name attribute";
            sa.recycle();
            return null;
        }
        name = name.intern();
        android.util.TypedValue v = sa.peekValue(com.android.internal.R.styleable.AndroidManifestMetaData_resource);
        if ((v != null) && (v.resourceId != 0)) {
            // Slog.i(TAG, "Meta data ref " + name + ": " + v);
            data.putInt(name, v.resourceId);
        } else {
            v = sa.peekValue(com.android.internal.R.styleable.AndroidManifestMetaData_value);
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
                                    android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("<meta-data> only supports string, integer, float, color, boolean, and resource reference types: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                                } else {
                                    outError[0] = "<meta-data> only supports string, integer, float, color, boolean, and resource reference types";
                                    data = null;
                                }
                            }



            } else {
                outError[0] = "<meta-data> requires an android:value or android:resource attribute";
                data = null;
            }
        }
        sa.recycle();
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        return data;
    }

    private static android.content.pm.VerifierInfo parseVerifier(android.util.AttributeSet attrs) {
        java.lang.String packageName = null;
        java.lang.String encodedPublicKey = null;
        final int attrCount = attrs.getAttributeCount();
        for (int i = 0; i < attrCount; i++) {
            final int attrResId = attrs.getAttributeNameResource(i);
            switch (attrResId) {
                case com.android.internal.R.attr.name :
                    packageName = attrs.getAttributeValue(i);
                    break;
                case com.android.internal.R.attr.publicKey :
                    encodedPublicKey = attrs.getAttributeValue(i);
                    break;
            }
        }
        if ((packageName == null) || (packageName.length() == 0)) {
            android.util.Slog.i(android.content.pm.PackageParser.TAG, "verifier package name was null; skipping");
            return null;
        }
        final java.security.PublicKey publicKey = android.content.pm.PackageParser.parsePublicKey(encodedPublicKey);
        if (publicKey == null) {
            android.util.Slog.i(android.content.pm.PackageParser.TAG, "Unable to parse verifier public key for " + packageName);
            return null;
        }
        return new android.content.pm.VerifierInfo(packageName, publicKey);
    }

    public static final java.security.PublicKey parsePublicKey(final java.lang.String encodedPublicKey) {
        if (encodedPublicKey == null) {
            android.util.Slog.w(android.content.pm.PackageParser.TAG, "Could not parse null public key");
            return null;
        }
        java.security.spec.EncodedKeySpec keySpec;
        try {
            final byte[] encoded = android.util.Base64.decode(encodedPublicKey, Base64.DEFAULT);
            keySpec = new java.security.spec.X509EncodedKeySpec(encoded);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.w(android.content.pm.PackageParser.TAG, "Could not parse verifier public key; invalid Base64");
            return null;
        }
        /* First try the key as an RSA key. */
        try {
            final java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (java.security.NoSuchAlgorithmException e) {
            android.util.Slog.wtf(android.content.pm.PackageParser.TAG, "Could not parse public key: RSA KeyFactory not included in build");
        } catch (java.security.spec.InvalidKeySpecException e) {
            // Not a RSA public key.
        }
        /* Now try it as a ECDSA key. */
        try {
            final java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("EC");
            return keyFactory.generatePublic(keySpec);
        } catch (java.security.NoSuchAlgorithmException e) {
            android.util.Slog.wtf(android.content.pm.PackageParser.TAG, "Could not parse public key: EC KeyFactory not included in build");
        } catch (java.security.spec.InvalidKeySpecException e) {
            // Not a ECDSA public key.
        }
        /* Now try it as a DSA key. */
        try {
            final java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("DSA");
            return keyFactory.generatePublic(keySpec);
        } catch (java.security.NoSuchAlgorithmException e) {
            android.util.Slog.wtf(android.content.pm.PackageParser.TAG, "Could not parse public key: DSA KeyFactory not included in build");
        } catch (java.security.spec.InvalidKeySpecException e) {
            // Not a DSA public key.
        }
        /* Not a supported key type */
        return null;
    }

    private static final java.lang.String ANDROID_RESOURCES = "http://schemas.android.com/apk/res/android";

    private boolean parseIntent(android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean allowGlobs, boolean allowAutoVerify, android.content.pm.PackageParser.IntentInfo outInfo, java.lang.String[] outError) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestIntentFilter);
        int priority = sa.getInt(com.android.internal.R.styleable.AndroidManifestIntentFilter_priority, 0);
        outInfo.setPriority(priority);
        int order = sa.getInt(com.android.internal.R.styleable.AndroidManifestIntentFilter_order, 0);
        outInfo.setOrder(order);
        android.util.TypedValue v = sa.peekValue(com.android.internal.R.styleable.AndroidManifestIntentFilter_label);
        if ((v != null) && ((outInfo.labelRes = v.resourceId) == 0)) {
            outInfo.nonLocalizedLabel = v.coerceToString();
        }
        int roundIconVal = (android.content.pm.PackageParser.sUseRoundIcon) ? sa.getResourceId(com.android.internal.R.styleable.AndroidManifestIntentFilter_roundIcon, 0) : 0;
        if (roundIconVal != 0) {
            outInfo.icon = roundIconVal;
        } else {
            outInfo.icon = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestIntentFilter_icon, 0);
        }
        outInfo.logo = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestIntentFilter_logo, 0);
        outInfo.banner = sa.getResourceId(com.android.internal.R.styleable.AndroidManifestIntentFilter_banner, 0);
        if (allowAutoVerify) {
            outInfo.setAutoVerify(sa.getBoolean(com.android.internal.R.styleable.AndroidManifestIntentFilter_autoVerify, false));
        }
        sa.recycle();
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            java.lang.String nodeName = parser.getName();
            if (nodeName.equals("action")) {
                java.lang.String value = parser.getAttributeValue(android.content.pm.PackageParser.ANDROID_RESOURCES, "name");
                if ((value == null) || (value == "")) {
                    outError[0] = "No value supplied for <android:name>";
                    return false;
                }
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                outInfo.addAction(value);
            } else
                if (nodeName.equals("category")) {
                    java.lang.String value = parser.getAttributeValue(android.content.pm.PackageParser.ANDROID_RESOURCES, "name");
                    if ((value == null) || (value == "")) {
                        outError[0] = "No value supplied for <android:name>";
                        return false;
                    }
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    outInfo.addCategory(value);
                } else
                    if (nodeName.equals("data")) {
                        sa = res.obtainAttributes(parser, com.android.internal.R.styleable.AndroidManifestData);
                        java.lang.String str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_mimeType, 0);
                        if (str != null) {
                            try {
                                outInfo.addDataType(str);
                            } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
                                outError[0] = e.toString();
                                sa.recycle();
                                return false;
                            }
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_scheme, 0);
                        if (str != null) {
                            outInfo.addDataScheme(str);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_ssp, 0);
                        if (str != null) {
                            outInfo.addDataSchemeSpecificPart(str, PatternMatcher.PATTERN_LITERAL);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_sspPrefix, 0);
                        if (str != null) {
                            outInfo.addDataSchemeSpecificPart(str, PatternMatcher.PATTERN_PREFIX);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_sspPattern, 0);
                        if (str != null) {
                            if (!allowGlobs) {
                                outError[0] = "sspPattern not allowed here; ssp must be literal";
                                return false;
                            }
                            outInfo.addDataSchemeSpecificPart(str, PatternMatcher.PATTERN_SIMPLE_GLOB);
                        }
                        java.lang.String host = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_host, 0);
                        java.lang.String port = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_port, 0);
                        if (host != null) {
                            outInfo.addDataAuthority(host, port);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_path, 0);
                        if (str != null) {
                            outInfo.addDataPath(str, PatternMatcher.PATTERN_LITERAL);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_pathPrefix, 0);
                        if (str != null) {
                            outInfo.addDataPath(str, PatternMatcher.PATTERN_PREFIX);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_pathPattern, 0);
                        if (str != null) {
                            if (!allowGlobs) {
                                outError[0] = "pathPattern not allowed here; path must be literal";
                                return false;
                            }
                            outInfo.addDataPath(str, PatternMatcher.PATTERN_SIMPLE_GLOB);
                        }
                        str = sa.getNonConfigurationString(com.android.internal.R.styleable.AndroidManifestData_pathAdvancedPattern, 0);
                        if (str != null) {
                            if (!allowGlobs) {
                                outError[0] = "pathAdvancedPattern not allowed here; path must be literal";
                                return false;
                            }
                            outInfo.addDataPath(str, PatternMatcher.PATTERN_ADVANCED_GLOB);
                        }
                        sa.recycle();
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    } else
                        if (!android.content.pm.PackageParser.RIGID_PARSER) {
                            android.util.Slog.w(android.content.pm.PackageParser.TAG, (((("Unknown element under <intent-filter>: " + parser.getName()) + " at ") + mArchiveSourcePath) + " ") + parser.getPositionDescription());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        } else {
                            outError[0] = "Bad element under <intent-filter>: " + parser.getName();
                            return false;
                        }



        } 
        outInfo.hasDefault = outInfo.hasCategory(android.content.Intent.CATEGORY_DEFAULT);
        if (android.content.pm.PackageParser.DEBUG_PARSER) {
            final java.lang.StringBuilder cats = new java.lang.StringBuilder("Intent d=");
            cats.append(outInfo.hasDefault);
            cats.append(", cat=");
            final java.util.Iterator<java.lang.String> it = outInfo.categoriesIterator();
            if (it != null) {
                while (it.hasNext()) {
                    cats.append(' ');
                    cats.append(it.next());
                } 
            }
            android.util.Slog.d(android.content.pm.PackageParser.TAG, cats.toString());
        }
        return true;
    }

    /**
     * A container for signing-related data of an application package.
     *
     * @unknown 
     */
    public static final class SigningDetails implements android.os.Parcelable {
        @android.annotation.IntDef({ android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.UNKNOWN, android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.JAR, android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.SIGNING_BLOCK_V2, android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.SIGNING_BLOCK_V3 })
        public @interface SignatureSchemeVersion {
            int UNKNOWN = 0;

            int JAR = 1;

            int SIGNING_BLOCK_V2 = 2;

            int SIGNING_BLOCK_V3 = 3;
        }

        @android.annotation.Nullable
        @android.annotation.UnsupportedAppUsage
        public final android.content.pm.Signature[] signatures;

        @android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion
        public final int signatureSchemeVersion;

        @android.annotation.Nullable
        public final android.util.ArraySet<java.security.PublicKey> publicKeys;

        /**
         * APK Signature Scheme v3 includes support for adding a proof-of-rotation record that
         * contains two pieces of information:
         *   1) the past signing certificates
         *   2) the flags that APK wants to assign to each of the past signing certificates.
         *
         * This collection of {@code Signature} objects, each of which is formed from a former
         * signing certificate of this APK before it was changed by signing certificate rotation,
         * represents the first piece of information.  It is the APK saying to the rest of the
         * world: "hey if you trust the old cert, you can trust me!"  This is useful, if for
         * instance, the platform would like to determine whether or not to allow this APK to do
         * something it would've allowed it to do under the old cert (like upgrade).
         */
        @android.annotation.Nullable
        public final android.content.pm.Signature[] pastSigningCertificates;

        /**
         * special value used to see if cert is in package - not exposed to callers
         */
        private static final int PAST_CERT_EXISTS = 0;

        @android.annotation.IntDef(flag = true, value = { android.content.pm.PackageParser.SigningDetails.CertCapabilities.INSTALLED_DATA, android.content.pm.PackageParser.SigningDetails.CertCapabilities.SHARED_USER_ID, android.content.pm.PackageParser.SigningDetails.CertCapabilities.PERMISSION, android.content.pm.PackageParser.SigningDetails.CertCapabilities.ROLLBACK })
        public @interface CertCapabilities {
            /**
             * accept data from already installed pkg with this cert
             */
            int INSTALLED_DATA = 1;

            /**
             * accept sharedUserId with pkg with this cert
             */
            int SHARED_USER_ID = 2;

            /**
             * grant SIGNATURE permissions to pkgs with this cert
             */
            int PERMISSION = 4;

            /**
             * allow pkg to update to one signed by this certificate
             */
            int ROLLBACK = 8;

            /**
             * allow pkg to continue to have auth access gated by this cert
             */
            int AUTH = 16;
        }

        /**
         * A representation of unknown signing details. Use instead of null.
         */
        public static final android.content.pm.PackageParser.SigningDetails UNKNOWN = new android.content.pm.PackageParser.SigningDetails(null, android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.UNKNOWN, null, null);

        @com.android.internal.annotations.VisibleForTesting
        public SigningDetails(android.content.pm.Signature[] signatures, @android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion
        int signatureSchemeVersion, android.util.ArraySet<java.security.PublicKey> keys, android.content.pm.Signature[] pastSigningCertificates) {
            this.signatures = signatures;
            this.signatureSchemeVersion = signatureSchemeVersion;
            this.publicKeys = keys;
            this.pastSigningCertificates = pastSigningCertificates;
        }

        public SigningDetails(android.content.pm.Signature[] signatures, @android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion
        int signatureSchemeVersion, android.content.pm.Signature[] pastSigningCertificates) throws java.security.cert.CertificateException {
            this(signatures, signatureSchemeVersion, android.content.pm.PackageParser.toSigningKeys(signatures), pastSigningCertificates);
        }

        public SigningDetails(android.content.pm.Signature[] signatures, @android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion
        int signatureSchemeVersion) throws java.security.cert.CertificateException {
            this(signatures, signatureSchemeVersion, null);
        }

        public SigningDetails(android.content.pm.PackageParser.SigningDetails orig) {
            if (orig != null) {
                if (orig.signatures != null) {
                    this.signatures = orig.signatures.clone();
                } else {
                    this.signatures = null;
                }
                this.signatureSchemeVersion = orig.signatureSchemeVersion;
                this.publicKeys = new android.util.ArraySet(orig.publicKeys);
                if (orig.pastSigningCertificates != null) {
                    this.pastSigningCertificates = orig.pastSigningCertificates.clone();
                } else {
                    this.pastSigningCertificates = null;
                }
            } else {
                this.signatures = null;
                this.signatureSchemeVersion = android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.UNKNOWN;
                this.publicKeys = null;
                this.pastSigningCertificates = null;
            }
        }

        /**
         * Returns true if the signing details have one or more signatures.
         */
        public boolean hasSignatures() {
            return (signatures != null) && (signatures.length > 0);
        }

        /**
         * Returns true if the signing details have past signing certificates.
         */
        public boolean hasPastSigningCertificates() {
            return (pastSigningCertificates != null) && (pastSigningCertificates.length > 0);
        }

        /**
         * Determines if the provided {@code oldDetails} is an ancestor of or the same as this one.
         * If the {@code oldDetails} signing certificate appears in our pastSigningCertificates,
         * then that means it has authorized a signing certificate rotation, which eventually leads
         * to our certificate, and thus can be trusted. If this method evaluates to true, this
         * SigningDetails object should be trusted if the previous one is.
         */
        public boolean hasAncestorOrSelf(android.content.pm.PackageParser.SigningDetails oldDetails) {
            if ((this == android.content.pm.PackageParser.SigningDetails.UNKNOWN) || (oldDetails == android.content.pm.PackageParser.SigningDetails.UNKNOWN)) {
                return false;
            }
            if (oldDetails.signatures.length > 1) {
                // multiple-signer packages cannot rotate signing certs, so we just compare current
                // signers for an exact match
                return signaturesMatchExactly(oldDetails);
            } else {
                // we may have signing certificate rotation history, check to see if the oldDetails
                // was one of our old signing certificates
                return hasCertificate(oldDetails.signatures[0]);
            }
        }

        /**
         * Similar to {@code hasAncestorOrSelf}.  Returns true only if this {@code SigningDetails}
         * is a descendant of {@code oldDetails}, not if they're the same.  This is used to
         * determine if this object is newer than the provided one.
         */
        public boolean hasAncestor(android.content.pm.PackageParser.SigningDetails oldDetails) {
            if ((this == android.content.pm.PackageParser.SigningDetails.UNKNOWN) || (oldDetails == android.content.pm.PackageParser.SigningDetails.UNKNOWN)) {
                return false;
            }
            if (this.hasPastSigningCertificates() && (oldDetails.signatures.length == 1)) {
                // the last entry in pastSigningCertificates is the current signer, ignore it
                for (int i = 0; i < (pastSigningCertificates.length - 1); i++) {
                    if (pastSigningCertificates[i].equals(oldDetails.signatures[i])) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Determines if the provided {@code oldDetails} is an ancestor of this one, and whether or
         * not this one grants it the provided capability, represented by the {@code flags}
         * parameter.  In the event of signing certificate rotation, a package may still interact
         * with entities signed by its old signing certificate and not want to break previously
         * functioning behavior.  The {@code flags} value determines which capabilities the app
         * signed by the newer signing certificate would like to continue to give to its previous
         * signing certificate(s).
         */
        public boolean checkCapability(android.content.pm.PackageParser.SigningDetails oldDetails, @android.content.pm.PackageParser.SigningDetails.CertCapabilities
        int flags) {
            if ((this == android.content.pm.PackageParser.SigningDetails.UNKNOWN) || (oldDetails == android.content.pm.PackageParser.SigningDetails.UNKNOWN)) {
                return false;
            }
            if (oldDetails.signatures.length > 1) {
                // multiple-signer packages cannot rotate signing certs, so we must have an exact
                // match, which also means all capabilities are granted
                return signaturesMatchExactly(oldDetails);
            } else {
                // we may have signing certificate rotation history, check to see if the oldDetails
                // was one of our old signing certificates, and if we grant it the capability it's
                // requesting
                return hasCertificate(oldDetails.signatures[0], flags);
            }
        }

        /**
         * A special case of {@code checkCapability} which re-encodes both sets of signing
         * certificates to counteract a previous re-encoding.
         */
        public boolean checkCapabilityRecover(android.content.pm.PackageParser.SigningDetails oldDetails, @android.content.pm.PackageParser.SigningDetails.CertCapabilities
        int flags) throws java.security.cert.CertificateException {
            if ((oldDetails == android.content.pm.PackageParser.SigningDetails.UNKNOWN) || (this == android.content.pm.PackageParser.SigningDetails.UNKNOWN)) {
                return false;
            }
            if (hasPastSigningCertificates() && (oldDetails.signatures.length == 1)) {
                // signing certificates may have rotated, check entire history for effective match
                for (int i = 0; i < pastSigningCertificates.length; i++) {
                    if (android.content.pm.Signature.areEffectiveMatch(oldDetails.signatures[0], pastSigningCertificates[i]) && (pastSigningCertificates[i].getFlags() == flags)) {
                        return true;
                    }
                }
            } else {
                return android.content.pm.Signature.areEffectiveMatch(oldDetails.signatures, signatures);
            }
            return false;
        }

        /**
         * Determine if {@code signature} is in this SigningDetails' signing certificate history,
         * including the current signer.  Automatically returns false if this object has multiple
         * signing certificates, since rotation is only supported for single-signers; this is
         * enforced by {@code hasCertificateInternal}.
         */
        public boolean hasCertificate(android.content.pm.Signature signature) {
            return hasCertificateInternal(signature, android.content.pm.PackageParser.SigningDetails.PAST_CERT_EXISTS);
        }

        /**
         * Determine if {@code signature} is in this SigningDetails' signing certificate history,
         * including the current signer, and whether or not it has the given permission.
         * Certificates which match our current signer automatically get all capabilities.
         * Automatically returns false if this object has multiple signing certificates, since
         * rotation is only supported for single-signers.
         */
        public boolean hasCertificate(android.content.pm.Signature signature, @android.content.pm.PackageParser.SigningDetails.CertCapabilities
        int flags) {
            return hasCertificateInternal(signature, flags);
        }

        /**
         * Convenient wrapper for calling {@code hasCertificate} with certificate's raw bytes.
         */
        public boolean hasCertificate(byte[] certificate) {
            android.content.pm.Signature signature = new android.content.pm.Signature(certificate);
            return hasCertificate(signature);
        }

        private boolean hasCertificateInternal(android.content.pm.Signature signature, int flags) {
            if (this == android.content.pm.PackageParser.SigningDetails.UNKNOWN) {
                return false;
            }
            // only single-signed apps can have pastSigningCertificates
            if (hasPastSigningCertificates()) {
                // check all past certs, except for the current one, which automatically gets all
                // capabilities, since it is the same as the current signature
                for (int i = 0; i < (pastSigningCertificates.length - 1); i++) {
                    if (pastSigningCertificates[i].equals(signature)) {
                        if ((flags == android.content.pm.PackageParser.SigningDetails.PAST_CERT_EXISTS) || ((flags & pastSigningCertificates[i].getFlags()) == flags)) {
                            return true;
                        }
                    }
                }
            }
            // not in previous certs signing history, just check the current signer and make sure
            // we are singly-signed
            return (signatures.length == 1) && signatures[0].equals(signature);
        }

        /**
         * Determines if the provided {@code sha256String} is an ancestor of this one, and whether
         * or not this one grants it the provided capability, represented by the {@code flags}
         * parameter.  In the event of signing certificate rotation, a package may still interact
         * with entities signed by its old signing certificate and not want to break previously
         * functioning behavior.  The {@code flags} value determines which capabilities the app
         * signed by the newer signing certificate would like to continue to give to its previous
         * signing certificate(s).
         *
         * @param sha256String
         * 		A hex-encoded representation of a sha256 digest.  In the case of an
         * 		app with multiple signers, this represents the hex-encoded sha256
         * 		digest of the combined hex-encoded sha256 digests of each individual
         * 		signing certificate according to {@link PackageUtils#computeSignaturesSha256Digest(Signature[])}
         */
        public boolean checkCapability(java.lang.String sha256String, @android.content.pm.PackageParser.SigningDetails.CertCapabilities
        int flags) {
            if (this == android.content.pm.PackageParser.SigningDetails.UNKNOWN) {
                return false;
            }
            // first see if the hash represents a single-signer in our signing history
            byte[] sha256Bytes = android.util.ByteStringUtils.fromHexToByteArray(sha256String);
            if (hasSha256Certificate(sha256Bytes, flags)) {
                return true;
            }
            // Not in signing history, either represents multiple signatures or not a match.
            // Multiple signers can't rotate, so no need to check flags, just see if the SHAs match.
            // We already check the single-signer case above as part of hasSha256Certificate, so no
            // need to verify we have multiple signers, just run the old check
            // just consider current signing certs
            final java.lang.String[] mSignaturesSha256Digests = android.util.PackageUtils.computeSignaturesSha256Digests(signatures);
            final java.lang.String mSignaturesSha256Digest = android.util.PackageUtils.computeSignaturesSha256Digest(mSignaturesSha256Digests);
            return mSignaturesSha256Digest.equals(sha256String);
        }

        /**
         * Determine if the {@code sha256Certificate} is in this SigningDetails' signing certificate
         * history, including the current signer.  Automatically returns false if this object has
         * multiple signing certificates, since rotation is only supported for single-signers.
         */
        public boolean hasSha256Certificate(byte[] sha256Certificate) {
            return hasSha256CertificateInternal(sha256Certificate, android.content.pm.PackageParser.SigningDetails.PAST_CERT_EXISTS);
        }

        /**
         * Determine if the {@code sha256Certificate} certificate hash corresponds to a signing
         * certificate in this SigningDetails' signing certificate history, including the current
         * signer, and whether or not it has the given permission.  Certificates which match our
         * current signer automatically get all capabilities. Automatically returns false if this
         * object has multiple signing certificates, since rotation is only supported for
         * single-signers.
         */
        public boolean hasSha256Certificate(byte[] sha256Certificate, @android.content.pm.PackageParser.SigningDetails.CertCapabilities
        int flags) {
            return hasSha256CertificateInternal(sha256Certificate, flags);
        }

        private boolean hasSha256CertificateInternal(byte[] sha256Certificate, int flags) {
            if (this == android.content.pm.PackageParser.SigningDetails.UNKNOWN) {
                return false;
            }
            if (hasPastSigningCertificates()) {
                // check all past certs, except for the last one, which automatically gets all
                // capabilities, since it is the same as the current signature, and is checked below
                for (int i = 0; i < (pastSigningCertificates.length - 1); i++) {
                    byte[] digest = android.util.PackageUtils.computeSha256DigestBytes(pastSigningCertificates[i].toByteArray());
                    if (java.util.Arrays.equals(sha256Certificate, digest)) {
                        if ((flags == android.content.pm.PackageParser.SigningDetails.PAST_CERT_EXISTS) || ((flags & pastSigningCertificates[i].getFlags()) == flags)) {
                            return true;
                        }
                    }
                }
            }
            // not in previous certs signing history, just check the current signer
            if (signatures.length == 1) {
                byte[] digest = android.util.PackageUtils.computeSha256DigestBytes(signatures[0].toByteArray());
                return java.util.Arrays.equals(sha256Certificate, digest);
            }
            return false;
        }

        /**
         * Returns true if the signatures in this and other match exactly.
         */
        public boolean signaturesMatchExactly(android.content.pm.PackageParser.SigningDetails other) {
            return android.content.pm.Signature.areExactMatch(this.signatures, other.signatures);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            boolean isUnknown = android.content.pm.PackageParser.SigningDetails.UNKNOWN == this;
            dest.writeBoolean(isUnknown);
            if (isUnknown) {
                return;
            }
            dest.writeTypedArray(this.signatures, flags);
            dest.writeInt(this.signatureSchemeVersion);
            dest.writeArraySet(this.publicKeys);
            dest.writeTypedArray(this.pastSigningCertificates, flags);
        }

        protected SigningDetails(android.os.Parcel in) {
            final java.lang.ClassLoader boot = java.lang.Object.class.getClassLoader();
            this.signatures = in.createTypedArray(android.content.pm.Signature.this.CREATOR);
            this.signatureSchemeVersion = in.readInt();
            this.publicKeys = ((android.util.ArraySet<java.security.PublicKey>) (in.readArraySet(boot)));
            this.pastSigningCertificates = in.createTypedArray(android.content.pm.Signature.this.CREATOR);
        }

        @android.annotation.NonNull
        public static final android.content.pm.Creator<android.content.pm.PackageParser.SigningDetails> CREATOR = new android.content.pm.Creator<android.content.pm.PackageParser.SigningDetails>() {
            @java.lang.Override
            public android.content.pm.PackageParser.SigningDetails createFromParcel(android.os.Parcel source) {
                if (source.readBoolean()) {
                    return android.content.pm.PackageParser.SigningDetails.UNKNOWN;
                }
                return new android.content.pm.PackageParser.SigningDetails(source);
            }

            @java.lang.Override
            public android.content.pm.PackageParser.SigningDetails[] newArray(int size) {
                return new android.content.pm.PackageParser.SigningDetails[size];
            }
        };

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if (!(o instanceof android.content.pm.PackageParser.SigningDetails))
                return false;

            android.content.pm.PackageParser.SigningDetails that = ((android.content.pm.PackageParser.SigningDetails) (o));
            if (signatureSchemeVersion != that.signatureSchemeVersion)
                return false;

            if (!android.content.pm.Signature.areExactMatch(signatures, that.signatures))
                return false;

            if (publicKeys != null) {
                if (!publicKeys.equals(that.publicKeys)) {
                    return false;
                }
            } else
                if (that.publicKeys != null) {
                    return false;
                }

            // can't use Signature.areExactMatch() because order matters with the past signing certs
            if (!java.util.Arrays.equals(pastSigningCertificates, that.pastSigningCertificates)) {
                return false;
            }
            return true;
        }

        @java.lang.Override
        public int hashCode() {
            int result = +java.util.Arrays.hashCode(signatures);
            result = (31 * result) + signatureSchemeVersion;
            result = (31 * result) + (publicKeys != null ? publicKeys.hashCode() : 0);
            result = (31 * result) + java.util.Arrays.hashCode(pastSigningCertificates);
            return result;
        }

        /**
         * Builder of {@code SigningDetails} instances.
         */
        public static class Builder {
            private android.content.pm.Signature[] mSignatures;

            private int mSignatureSchemeVersion = android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion.UNKNOWN;

            private android.content.pm.Signature[] mPastSigningCertificates;

            @android.annotation.UnsupportedAppUsage
            public Builder() {
            }

            /**
             * get signing certificates used to sign the current APK
             */
            @android.annotation.UnsupportedAppUsage
            public android.content.pm.PackageParser.SigningDetails.Builder setSignatures(android.content.pm.Signature[] signatures) {
                mSignatures = signatures;
                return this;
            }

            /**
             * set the signature scheme version used to sign the APK
             */
            @android.annotation.UnsupportedAppUsage
            public android.content.pm.PackageParser.SigningDetails.Builder setSignatureSchemeVersion(int signatureSchemeVersion) {
                mSignatureSchemeVersion = signatureSchemeVersion;
                return this;
            }

            /**
             * set the signing certificates by which the APK proved it can be authenticated
             */
            @android.annotation.UnsupportedAppUsage
            public android.content.pm.PackageParser.SigningDetails.Builder setPastSigningCertificates(android.content.pm.Signature[] pastSigningCertificates) {
                mPastSigningCertificates = pastSigningCertificates;
                return this;
            }

            private void checkInvariants() {
                // must have signatures and scheme version set
                if (mSignatures == null) {
                    throw new java.lang.IllegalStateException("SigningDetails requires the current signing" + " certificates.");
                }
            }

            /**
             * build a {@code SigningDetails} object
             */
            @android.annotation.UnsupportedAppUsage
            public android.content.pm.PackageParser.SigningDetails build() throws java.security.cert.CertificateException {
                checkInvariants();
                return new android.content.pm.PackageParser.SigningDetails(mSignatures, mSignatureSchemeVersion, mPastSigningCertificates);
            }
        }
    }

    /**
     * Representation of a full package parsed from APK files on disk. A package
     * consists of a single base APK, and zero or more split APKs.
     */
    public static final class Package implements android.os.Parcelable {
        @android.annotation.UnsupportedAppUsage
        public java.lang.String packageName;

        // The package name declared in the manifest as the package can be
        // renamed, for example static shared libs use synthetic package names.
        public java.lang.String manifestPackageName;

        /**
         * Names of any split APKs, ordered by parsed splitName
         */
        public java.lang.String[] splitNames;

        // TODO: work towards making these paths invariant
        public java.lang.String volumeUuid;

        /**
         * Path where this package was found on disk. For monolithic packages
         * this is path to single base APK file; for cluster packages this is
         * path to the cluster directory.
         */
        public java.lang.String codePath;

        /**
         * Path of base APK
         */
        public java.lang.String baseCodePath;

        /**
         * Paths of any split APKs, ordered by parsed splitName
         */
        public java.lang.String[] splitCodePaths;

        /**
         * Revision code of base APK
         */
        public int baseRevisionCode;

        /**
         * Revision codes of any split APKs, ordered by parsed splitName
         */
        public int[] splitRevisionCodes;

        /**
         * Flags of any split APKs; ordered by parsed splitName
         */
        public int[] splitFlags;

        /**
         * Private flags of any split APKs; ordered by parsed splitName.
         *
         * {@hide }
         */
        public int[] splitPrivateFlags;

        public boolean baseHardwareAccelerated;

        // For now we only support one application per package.
        @android.annotation.UnsupportedAppUsage
        public android.content.pm.ApplicationInfo applicationInfo = new android.content.pm.ApplicationInfo();

        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<android.content.pm.PackageParser.Permission> permissions = new java.util.ArrayList<android.content.pm.PackageParser.Permission>(0);

        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<android.content.pm.PackageParser.PermissionGroup> permissionGroups = new java.util.ArrayList<android.content.pm.PackageParser.PermissionGroup>(0);

        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<android.content.pm.PackageParser.Activity> activities = new java.util.ArrayList<android.content.pm.PackageParser.Activity>(0);

        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<android.content.pm.PackageParser.Activity> receivers = new java.util.ArrayList<android.content.pm.PackageParser.Activity>(0);

        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<android.content.pm.PackageParser.Provider> providers = new java.util.ArrayList<android.content.pm.PackageParser.Provider>(0);

        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<android.content.pm.PackageParser.Service> services = new java.util.ArrayList<android.content.pm.PackageParser.Service>(0);

        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<android.content.pm.PackageParser.Instrumentation> instrumentation = new java.util.ArrayList<android.content.pm.PackageParser.Instrumentation>(0);

        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<java.lang.String> requestedPermissions = new java.util.ArrayList<java.lang.String>();

        /**
         * Permissions requested but not in the manifest.
         */
        public final java.util.ArrayList<java.lang.String> implicitPermissions = new java.util.ArrayList<>();

        @android.annotation.UnsupportedAppUsage
        public java.util.ArrayList<java.lang.String> protectedBroadcasts;

        public android.content.pm.PackageParser.Package parentPackage;

        public java.util.ArrayList<android.content.pm.PackageParser.Package> childPackages;

        public java.lang.String staticSharedLibName = null;

        public long staticSharedLibVersion = 0;

        public java.util.ArrayList<java.lang.String> libraryNames = null;

        @android.annotation.UnsupportedAppUsage
        public java.util.ArrayList<java.lang.String> usesLibraries = null;

        public java.util.ArrayList<java.lang.String> usesStaticLibraries = null;

        public long[] usesStaticLibrariesVersions = null;

        public java.lang.String[][] usesStaticLibrariesCertDigests = null;

        @android.annotation.UnsupportedAppUsage
        public java.util.ArrayList<java.lang.String> usesOptionalLibraries = null;

        @android.annotation.UnsupportedAppUsage
        public java.lang.String[] usesLibraryFiles = null;

        public java.util.ArrayList<android.content.pm.SharedLibraryInfo> usesLibraryInfos = null;

        public java.util.ArrayList<android.content.pm.PackageParser.ActivityIntentInfo> preferredActivityFilters = null;

        public java.util.ArrayList<java.lang.String> mOriginalPackages = null;

        public java.lang.String mRealPackage = null;

        public java.util.ArrayList<java.lang.String> mAdoptPermissions = null;

        // We store the application meta-data independently to avoid multiple unwanted references
        @android.annotation.UnsupportedAppUsage
        public android.os.Bundle mAppMetaData = null;

        // The version code declared for this package.
        @android.annotation.UnsupportedAppUsage
        public int mVersionCode;

        // The major version code declared for this package.
        public int mVersionCodeMajor;

        // Return long containing mVersionCode and mVersionCodeMajor.
        public long getLongVersionCode() {
            return android.content.pm.PackageInfo.composeLongVersionCode(mVersionCodeMajor, mVersionCode);
        }

        // The version name declared for this package.
        @android.annotation.UnsupportedAppUsage
        public java.lang.String mVersionName;

        // The shared user id that this package wants to use.
        @android.annotation.UnsupportedAppUsage
        public java.lang.String mSharedUserId;

        // The shared user label that this package wants to use.
        @android.annotation.UnsupportedAppUsage
        public int mSharedUserLabel;

        // Signatures that were read from the package.
        @android.annotation.UnsupportedAppUsage
        @android.annotation.NonNull
        public android.content.pm.PackageParser.SigningDetails mSigningDetails = android.content.pm.PackageParser.SigningDetails.UNKNOWN;

        // For use by package manager service for quick lookup of
        // preferred up order.
        @android.annotation.UnsupportedAppUsage
        public int mPreferredOrder = 0;

        // For use by package manager to keep track of when a package was last used.
        public long[] mLastPackageUsageTimeInMills = new long[android.content.pm.PackageManager.NOTIFY_PACKAGE_USE_REASONS_COUNT];

        // // User set enabled state.
        // public int mSetEnabled = PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
        // 
        // // Whether the package has been stopped.
        // public boolean mSetStopped = false;
        // Additional data supplied by callers.
        @android.annotation.UnsupportedAppUsage
        public java.lang.Object mExtras;

        // Applications hardware preferences
        @android.annotation.UnsupportedAppUsage
        public java.util.ArrayList<android.content.pm.ConfigurationInfo> configPreferences = null;

        // Applications requested features
        @android.annotation.UnsupportedAppUsage
        public java.util.ArrayList<android.content.pm.FeatureInfo> reqFeatures = null;

        // Applications requested feature groups
        public java.util.ArrayList<android.content.pm.FeatureGroupInfo> featureGroups = null;

        @android.annotation.UnsupportedAppUsage
        public int installLocation;

        public boolean coreApp;

        /* An app that's required for all users and cannot be uninstalled for a user */
        public boolean mRequiredForAllUsers;

        /* The restricted account authenticator type that is used by this application */
        public java.lang.String mRestrictedAccountType;

        /* The required account type without which this application will not function */
        public java.lang.String mRequiredAccountType;

        public java.lang.String mOverlayTarget;

        public java.lang.String mOverlayTargetName;

        public java.lang.String mOverlayCategory;

        public int mOverlayPriority;

        public boolean mOverlayIsStatic;

        public int mCompileSdkVersion;

        public java.lang.String mCompileSdkVersionCodename;

        /**
         * Data used to feed the KeySetManagerService
         */
        @android.annotation.UnsupportedAppUsage
        public android.util.ArraySet<java.lang.String> mUpgradeKeySets;

        @android.annotation.UnsupportedAppUsage
        public android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.security.PublicKey>> mKeySetMapping;

        /**
         * The install time abi override for this package, if any.
         *
         * TODO: This seems like a horrible place to put the abiOverride because
         * this isn't something the packageParser parsers. However, this fits in with
         * the rest of the PackageManager where package scanning randomly pushes
         * and prods fields out of {@code this.applicationInfo}.
         */
        public java.lang.String cpuAbiOverride;

        /**
         * The install time abi override to choose 32bit abi's when multiple abi's
         * are present. This is only meaningfull for multiarch applications.
         * The use32bitAbi attribute is ignored if cpuAbiOverride is also set.
         */
        public boolean use32bitAbi;

        public byte[] restrictUpdateHash;

        /**
         * Set if the app or any of its components are visible to instant applications.
         */
        public boolean visibleToInstantApps;

        /**
         * Whether or not the package is a stub and must be replaced by the full version.
         */
        public boolean isStub;

        @android.annotation.UnsupportedAppUsage
        public Package(java.lang.String packageName) {
            this.packageName = packageName;
            this.manifestPackageName = packageName;
            applicationInfo.packageName = packageName;
            applicationInfo.uid = -1;
        }

        public void setApplicationVolumeUuid(java.lang.String volumeUuid) {
            final java.util.UUID storageUuid = android.os.storage.StorageManager.convert(volumeUuid);
            this.applicationInfo.volumeUuid = volumeUuid;
            this.applicationInfo.storageUuid = storageUuid;
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).applicationInfo.volumeUuid = volumeUuid;
                    childPackages.get(i).applicationInfo.storageUuid = storageUuid;
                }
            }
        }

        public void setApplicationInfoCodePath(java.lang.String codePath) {
            this.applicationInfo.setCodePath(codePath);
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).applicationInfo.setCodePath(codePath);
                }
            }
        }

        /**
         *
         *
         * @deprecated Forward locked apps no longer supported. Resource path not needed.
         */
        @java.lang.Deprecated
        public void setApplicationInfoResourcePath(java.lang.String resourcePath) {
            this.applicationInfo.setResourcePath(resourcePath);
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).applicationInfo.setResourcePath(resourcePath);
                }
            }
        }

        /**
         *
         *
         * @deprecated Forward locked apps no longer supported. Resource path not needed.
         */
        @java.lang.Deprecated
        public void setApplicationInfoBaseResourcePath(java.lang.String resourcePath) {
            this.applicationInfo.setBaseResourcePath(resourcePath);
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).applicationInfo.setBaseResourcePath(resourcePath);
                }
            }
        }

        public void setApplicationInfoBaseCodePath(java.lang.String baseCodePath) {
            this.applicationInfo.setBaseCodePath(baseCodePath);
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).applicationInfo.setBaseCodePath(baseCodePath);
                }
            }
        }

        public java.util.List<java.lang.String> getChildPackageNames() {
            if (childPackages == null) {
                return null;
            }
            final int childCount = childPackages.size();
            final java.util.List<java.lang.String> childPackageNames = new java.util.ArrayList<>(childCount);
            for (int i = 0; i < childCount; i++) {
                java.lang.String childPackageName = childPackages.get(i).packageName;
                childPackageNames.add(childPackageName);
            }
            return childPackageNames;
        }

        public boolean hasChildPackage(java.lang.String packageName) {
            final int childCount = (childPackages != null) ? childPackages.size() : 0;
            for (int i = 0; i < childCount; i++) {
                if (childPackages.get(i).packageName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }

        public void setApplicationInfoSplitCodePaths(java.lang.String[] splitCodePaths) {
            this.applicationInfo.setSplitCodePaths(splitCodePaths);
            // Children have no splits
        }

        /**
         *
         *
         * @deprecated Forward locked apps no longer supported. Resource path not needed.
         */
        @java.lang.Deprecated
        public void setApplicationInfoSplitResourcePaths(java.lang.String[] resroucePaths) {
            this.applicationInfo.setSplitResourcePaths(resroucePaths);
            // Children have no splits
        }

        public void setSplitCodePaths(java.lang.String[] codePaths) {
            this.splitCodePaths = codePaths;
        }

        public void setCodePath(java.lang.String codePath) {
            this.codePath = codePath;
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).codePath = codePath;
                }
            }
        }

        public void setBaseCodePath(java.lang.String baseCodePath) {
            this.baseCodePath = baseCodePath;
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).baseCodePath = baseCodePath;
                }
            }
        }

        /**
         * Sets signing details on the package and any of its children.
         */
        public void setSigningDetails(@android.annotation.NonNull
        android.content.pm.PackageParser.SigningDetails signingDetails) {
            mSigningDetails = signingDetails;
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).mSigningDetails = signingDetails;
                }
            }
        }

        public void setVolumeUuid(java.lang.String volumeUuid) {
            this.volumeUuid = volumeUuid;
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).volumeUuid = volumeUuid;
                }
            }
        }

        public void setApplicationInfoFlags(int mask, int flags) {
            applicationInfo.flags = (applicationInfo.flags & (~mask)) | (mask & flags);
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).applicationInfo.flags = (applicationInfo.flags & (~mask)) | (mask & flags);
                }
            }
        }

        public void setUse32bitAbi(boolean use32bitAbi) {
            this.use32bitAbi = use32bitAbi;
            if (childPackages != null) {
                final int packageCount = childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    childPackages.get(i).use32bitAbi = use32bitAbi;
                }
            }
        }

        public boolean isLibrary() {
            return (staticSharedLibName != null) || (!com.android.internal.util.ArrayUtils.isEmpty(libraryNames));
        }

        public java.util.List<java.lang.String> getAllCodePaths() {
            java.util.ArrayList<java.lang.String> paths = new java.util.ArrayList<>();
            paths.add(baseCodePath);
            if (!com.android.internal.util.ArrayUtils.isEmpty(splitCodePaths)) {
                java.util.Collections.addAll(paths, splitCodePaths);
            }
            return paths;
        }

        /**
         * Filtered set of {@link #getAllCodePaths()} that excludes
         * resource-only APKs.
         */
        public java.util.List<java.lang.String> getAllCodePathsExcludingResourceOnly() {
            java.util.ArrayList<java.lang.String> paths = new java.util.ArrayList<>();
            if ((applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_HAS_CODE) != 0) {
                paths.add(baseCodePath);
            }
            if (!com.android.internal.util.ArrayUtils.isEmpty(splitCodePaths)) {
                for (int i = 0; i < splitCodePaths.length; i++) {
                    if ((splitFlags[i] & android.content.pm.ApplicationInfo.FLAG_HAS_CODE) != 0) {
                        paths.add(splitCodePaths[i]);
                    }
                }
            }
            return paths;
        }

        @android.annotation.UnsupportedAppUsage
        public void setPackageName(java.lang.String newName) {
            packageName = newName;
            applicationInfo.packageName = newName;
            for (int i = permissions.size() - 1; i >= 0; i--) {
                permissions.get(i).setPackageName(newName);
            }
            for (int i = permissionGroups.size() - 1; i >= 0; i--) {
                permissionGroups.get(i).setPackageName(newName);
            }
            for (int i = activities.size() - 1; i >= 0; i--) {
                activities.get(i).setPackageName(newName);
            }
            for (int i = receivers.size() - 1; i >= 0; i--) {
                receivers.get(i).setPackageName(newName);
            }
            for (int i = providers.size() - 1; i >= 0; i--) {
                providers.get(i).setPackageName(newName);
            }
            for (int i = services.size() - 1; i >= 0; i--) {
                services.get(i).setPackageName(newName);
            }
            for (int i = instrumentation.size() - 1; i >= 0; i--) {
                instrumentation.get(i).setPackageName(newName);
            }
        }

        public boolean hasComponentClassName(java.lang.String name) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (name.equals(activities.get(i).className)) {
                    return true;
                }
            }
            for (int i = receivers.size() - 1; i >= 0; i--) {
                if (name.equals(receivers.get(i).className)) {
                    return true;
                }
            }
            for (int i = providers.size() - 1; i >= 0; i--) {
                if (name.equals(providers.get(i).className)) {
                    return true;
                }
            }
            for (int i = services.size() - 1; i >= 0; i--) {
                if (name.equals(services.get(i).className)) {
                    return true;
                }
            }
            for (int i = instrumentation.size() - 1; i >= 0; i--) {
                if (name.equals(instrumentation.get(i).className)) {
                    return true;
                }
            }
            return false;
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isExternal() {
            return applicationInfo.isExternal();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isForwardLocked() {
            return false;
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isOem() {
            return applicationInfo.isOem();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isVendor() {
            return applicationInfo.isVendor();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isProduct() {
            return applicationInfo.isProduct();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isProductServices() {
            return applicationInfo.isProductServices();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isOdm() {
            return applicationInfo.isOdm();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isPrivileged() {
            return applicationInfo.isPrivilegedApp();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isSystem() {
            return applicationInfo.isSystemApp();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isUpdatedSystemApp() {
            return applicationInfo.isUpdatedSystemApp();
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean canHaveOatDir() {
            // The following app types CANNOT have oat directory
            // - non-updated system apps
            return (!isSystem()) || isUpdatedSystemApp();
        }

        public boolean isMatch(int flags) {
            if ((flags & android.content.pm.PackageManager.MATCH_SYSTEM_ONLY) != 0) {
                return isSystem();
            }
            return true;
        }

        public long getLatestPackageUseTimeInMills() {
            long latestUse = 0L;
            for (long use : mLastPackageUsageTimeInMills) {
                latestUse = java.lang.Math.max(latestUse, use);
            }
            return latestUse;
        }

        public long getLatestForegroundPackageUseTimeInMills() {
            int[] foregroundReasons = new int[]{ android.content.pm.PackageManager.NOTIFY_PACKAGE_USE_ACTIVITY, android.content.pm.PackageManager.NOTIFY_PACKAGE_USE_FOREGROUND_SERVICE };
            long latestUse = 0L;
            for (int reason : foregroundReasons) {
                latestUse = java.lang.Math.max(latestUse, mLastPackageUsageTimeInMills[reason]);
            }
            return latestUse;
        }

        public java.lang.String toString() {
            return ((("Package{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + packageName) + "}";
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        public Package(android.os.Parcel dest) {
            // We use the boot classloader for all classes that we load.
            final java.lang.ClassLoader boot = java.lang.Object.class.getClassLoader();
            packageName = dest.readString().intern();
            manifestPackageName = dest.readString();
            splitNames = dest.readStringArray();
            volumeUuid = dest.readString();
            codePath = dest.readString();
            baseCodePath = dest.readString();
            splitCodePaths = dest.readStringArray();
            baseRevisionCode = dest.readInt();
            splitRevisionCodes = dest.createIntArray();
            splitFlags = dest.createIntArray();
            splitPrivateFlags = dest.createIntArray();
            baseHardwareAccelerated = dest.readInt() == 1;
            applicationInfo = dest.readParcelable(boot);
            if (applicationInfo.permission != null) {
                applicationInfo.permission = applicationInfo.permission.intern();
            }
            // We don't serialize the "owner" package and the application info object for each of
            // these components, in order to save space and to avoid circular dependencies while
            // serialization. We need to fix them all up here.
            dest.readParcelableList(permissions, boot);
            fixupOwner(permissions);
            dest.readParcelableList(permissionGroups, boot);
            fixupOwner(permissionGroups);
            dest.readParcelableList(activities, boot);
            fixupOwner(activities);
            dest.readParcelableList(receivers, boot);
            fixupOwner(receivers);
            dest.readParcelableList(providers, boot);
            fixupOwner(providers);
            dest.readParcelableList(services, boot);
            fixupOwner(services);
            dest.readParcelableList(instrumentation, boot);
            fixupOwner(instrumentation);
            dest.readStringList(requestedPermissions);
            android.content.pm.PackageParser.Package.internStringArrayList(requestedPermissions);
            dest.readStringList(implicitPermissions);
            android.content.pm.PackageParser.Package.internStringArrayList(implicitPermissions);
            protectedBroadcasts = dest.createStringArrayList();
            android.content.pm.PackageParser.Package.internStringArrayList(protectedBroadcasts);
            parentPackage = dest.readParcelable(boot);
            childPackages = new java.util.ArrayList<>();
            dest.readParcelableList(childPackages, boot);
            if (childPackages.size() == 0) {
                childPackages = null;
            }
            staticSharedLibName = dest.readString();
            if (staticSharedLibName != null) {
                staticSharedLibName = staticSharedLibName.intern();
            }
            staticSharedLibVersion = dest.readLong();
            libraryNames = dest.createStringArrayList();
            android.content.pm.PackageParser.Package.internStringArrayList(libraryNames);
            usesLibraries = dest.createStringArrayList();
            android.content.pm.PackageParser.Package.internStringArrayList(usesLibraries);
            usesOptionalLibraries = dest.createStringArrayList();
            android.content.pm.PackageParser.Package.internStringArrayList(usesOptionalLibraries);
            usesLibraryFiles = dest.readStringArray();
            usesLibraryInfos = dest.createTypedArrayList(android.content.pm.SharedLibraryInfo.this.CREATOR);
            final int libCount = dest.readInt();
            if (libCount > 0) {
                usesStaticLibraries = new java.util.ArrayList<>(libCount);
                dest.readStringList(usesStaticLibraries);
                android.content.pm.PackageParser.Package.internStringArrayList(usesStaticLibraries);
                usesStaticLibrariesVersions = new long[libCount];
                dest.readLongArray(usesStaticLibrariesVersions);
                usesStaticLibrariesCertDigests = new java.lang.String[libCount][];
                for (int i = 0; i < libCount; i++) {
                    usesStaticLibrariesCertDigests[i] = dest.createStringArray();
                }
            }
            preferredActivityFilters = new java.util.ArrayList<>();
            dest.readParcelableList(preferredActivityFilters, boot);
            if (preferredActivityFilters.size() == 0) {
                preferredActivityFilters = null;
            }
            mOriginalPackages = dest.createStringArrayList();
            mRealPackage = dest.readString();
            mAdoptPermissions = dest.createStringArrayList();
            mAppMetaData = dest.readBundle();
            mVersionCode = dest.readInt();
            mVersionCodeMajor = dest.readInt();
            mVersionName = dest.readString();
            if (mVersionName != null) {
                mVersionName = mVersionName.intern();
            }
            mSharedUserId = dest.readString();
            if (mSharedUserId != null) {
                mSharedUserId = mSharedUserId.intern();
            }
            mSharedUserLabel = dest.readInt();
            mSigningDetails = dest.readParcelable(boot);
            mPreferredOrder = dest.readInt();
            // long[] packageUsageTimeMillis is not persisted because it isn't information that
            // is parsed from the APK.
            // Object mExtras is not persisted because it is not information that is read from
            // the APK, rather, it is supplied by callers.
            configPreferences = new java.util.ArrayList<>();
            dest.readParcelableList(configPreferences, boot);
            if (configPreferences.size() == 0) {
                configPreferences = null;
            }
            reqFeatures = new java.util.ArrayList<>();
            dest.readParcelableList(reqFeatures, boot);
            if (reqFeatures.size() == 0) {
                reqFeatures = null;
            }
            featureGroups = new java.util.ArrayList<>();
            dest.readParcelableList(featureGroups, boot);
            if (featureGroups.size() == 0) {
                featureGroups = null;
            }
            installLocation = dest.readInt();
            coreApp = dest.readInt() == 1;
            mRequiredForAllUsers = dest.readInt() == 1;
            mRestrictedAccountType = dest.readString();
            mRequiredAccountType = dest.readString();
            mOverlayTarget = dest.readString();
            mOverlayTargetName = dest.readString();
            mOverlayCategory = dest.readString();
            mOverlayPriority = dest.readInt();
            mOverlayIsStatic = dest.readInt() == 1;
            mCompileSdkVersion = dest.readInt();
            mCompileSdkVersionCodename = dest.readString();
            mUpgradeKeySets = ((android.util.ArraySet<java.lang.String>) (dest.readArraySet(boot)));
            mKeySetMapping = android.content.pm.PackageParser.Package.readKeySetMapping(dest);
            cpuAbiOverride = dest.readString();
            use32bitAbi = dest.readInt() == 1;
            restrictUpdateHash = dest.createByteArray();
            visibleToInstantApps = dest.readInt() == 1;
        }

        private static void internStringArrayList(java.util.List<java.lang.String> list) {
            if (list != null) {
                final int N = list.size();
                for (int i = 0; i < N; ++i) {
                    list.set(i, list.get(i).intern());
                }
            }
        }

        /**
         * Sets the package owner and the the {@code applicationInfo} for every component
         * owner by this package.
         */
        private void fixupOwner(java.util.List<? extends android.content.pm.PackageParser.Component<?>> list) {
            if (list != null) {
                for (android.content.pm.PackageParser.Component<?> c : list) {
                    c.owner = this;
                    if (c instanceof android.content.pm.PackageParser.Activity) {
                        ((android.content.pm.PackageParser.Activity) (c)).info.applicationInfo = this.applicationInfo;
                    } else
                        if (c instanceof android.content.pm.PackageParser.Service) {
                            ((android.content.pm.PackageParser.Service) (c)).info.applicationInfo = this.applicationInfo;
                        } else
                            if (c instanceof android.content.pm.PackageParser.Provider) {
                                ((android.content.pm.PackageParser.Provider) (c)).info.applicationInfo = this.applicationInfo;
                            }


                }
            }
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(packageName);
            dest.writeString(manifestPackageName);
            dest.writeStringArray(splitNames);
            dest.writeString(volumeUuid);
            dest.writeString(codePath);
            dest.writeString(baseCodePath);
            dest.writeStringArray(splitCodePaths);
            dest.writeInt(baseRevisionCode);
            dest.writeIntArray(splitRevisionCodes);
            dest.writeIntArray(splitFlags);
            dest.writeIntArray(splitPrivateFlags);
            dest.writeInt(baseHardwareAccelerated ? 1 : 0);
            dest.writeParcelable(applicationInfo, flags);
            dest.writeParcelableList(permissions, flags);
            dest.writeParcelableList(permissionGroups, flags);
            dest.writeParcelableList(activities, flags);
            dest.writeParcelableList(receivers, flags);
            dest.writeParcelableList(providers, flags);
            dest.writeParcelableList(services, flags);
            dest.writeParcelableList(instrumentation, flags);
            dest.writeStringList(requestedPermissions);
            dest.writeStringList(implicitPermissions);
            dest.writeStringList(protectedBroadcasts);
            // TODO: This doesn't work: b/64295061
            dest.writeParcelable(parentPackage, flags);
            dest.writeParcelableList(childPackages, flags);
            dest.writeString(staticSharedLibName);
            dest.writeLong(staticSharedLibVersion);
            dest.writeStringList(libraryNames);
            dest.writeStringList(usesLibraries);
            dest.writeStringList(usesOptionalLibraries);
            dest.writeStringArray(usesLibraryFiles);
            dest.writeTypedList(usesLibraryInfos);
            if (com.android.internal.util.ArrayUtils.isEmpty(usesStaticLibraries)) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(usesStaticLibraries.size());
                dest.writeStringList(usesStaticLibraries);
                dest.writeLongArray(usesStaticLibrariesVersions);
                for (java.lang.String[] usesStaticLibrariesCertDigest : usesStaticLibrariesCertDigests) {
                    dest.writeStringArray(usesStaticLibrariesCertDigest);
                }
            }
            dest.writeParcelableList(preferredActivityFilters, flags);
            dest.writeStringList(mOriginalPackages);
            dest.writeString(mRealPackage);
            dest.writeStringList(mAdoptPermissions);
            dest.writeBundle(mAppMetaData);
            dest.writeInt(mVersionCode);
            dest.writeInt(mVersionCodeMajor);
            dest.writeString(mVersionName);
            dest.writeString(mSharedUserId);
            dest.writeInt(mSharedUserLabel);
            dest.writeParcelable(mSigningDetails, flags);
            dest.writeInt(mPreferredOrder);
            // long[] packageUsageTimeMillis is not persisted because it isn't information that
            // is parsed from the APK.
            // Object mExtras is not persisted because it is not information that is read from
            // the APK, rather, it is supplied by callers.
            dest.writeParcelableList(configPreferences, flags);
            dest.writeParcelableList(reqFeatures, flags);
            dest.writeParcelableList(featureGroups, flags);
            dest.writeInt(installLocation);
            dest.writeInt(coreApp ? 1 : 0);
            dest.writeInt(mRequiredForAllUsers ? 1 : 0);
            dest.writeString(mRestrictedAccountType);
            dest.writeString(mRequiredAccountType);
            dest.writeString(mOverlayTarget);
            dest.writeString(mOverlayTargetName);
            dest.writeString(mOverlayCategory);
            dest.writeInt(mOverlayPriority);
            dest.writeInt(mOverlayIsStatic ? 1 : 0);
            dest.writeInt(mCompileSdkVersion);
            dest.writeString(mCompileSdkVersionCodename);
            dest.writeArraySet(mUpgradeKeySets);
            android.content.pm.PackageParser.Package.writeKeySetMapping(dest, mKeySetMapping);
            dest.writeString(cpuAbiOverride);
            dest.writeInt(use32bitAbi ? 1 : 0);
            dest.writeByteArray(restrictUpdateHash);
            dest.writeInt(visibleToInstantApps ? 1 : 0);
        }

        /**
         * Writes the keyset mapping to the provided package. {@code null} mappings are permitted.
         */
        private static void writeKeySetMapping(android.os.Parcel dest, android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.security.PublicKey>> keySetMapping) {
            if (keySetMapping == null) {
                dest.writeInt(-1);
                return;
            }
            final int N = keySetMapping.size();
            dest.writeInt(N);
            for (int i = 0; i < N; i++) {
                dest.writeString(keySetMapping.keyAt(i));
                android.util.ArraySet<java.security.PublicKey> keys = keySetMapping.valueAt(i);
                if (keys == null) {
                    dest.writeInt(-1);
                    continue;
                }
                final int M = keys.size();
                dest.writeInt(M);
                for (int j = 0; j < M; j++) {
                    dest.writeSerializable(keys.valueAt(j));
                }
            }
        }

        /**
         * Reads a keyset mapping from the given parcel at the given data position. May return
         * {@code null} if the serialized mapping was {@code null}.
         */
        private static android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.security.PublicKey>> readKeySetMapping(android.os.Parcel in) {
            final int N = in.readInt();
            if (N == (-1)) {
                return null;
            }
            android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.security.PublicKey>> keySetMapping = new android.util.ArrayMap();
            for (int i = 0; i < N; ++i) {
                java.lang.String key = in.readString();
                final int M = in.readInt();
                if (M == (-1)) {
                    keySetMapping.put(key, null);
                    continue;
                }
                android.util.ArraySet<java.security.PublicKey> keys = new android.util.ArraySet(M);
                for (int j = 0; j < M; ++j) {
                    java.security.PublicKey pk = ((java.security.PublicKey) (in.readSerializable()));
                    keys.add(pk);
                }
                keySetMapping.put(key, keys);
            }
            return keySetMapping;
        }

        public static final Parcelable.Creator CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageParser.Package>() {
            public java.lang.Package createFromParcel(android.os.Parcel in) {
                return new java.lang.Package(in);
            }

            public java.lang.Package[] newArray(int size) {
                return new java.lang.Package[size];
            }
        };
    }

    public static abstract class Component<II extends android.content.pm.PackageParser.IntentInfo> {
        @android.annotation.UnsupportedAppUsage
        public final java.util.ArrayList<II> intents;

        @android.annotation.UnsupportedAppUsage
        public final java.lang.String className;

        @android.annotation.UnsupportedAppUsage
        public android.os.Bundle metaData;

        @android.annotation.UnsupportedAppUsage
        public android.content.pm.PackageParser.Package owner;

        /**
         * The order of this component in relation to its peers
         */
        public int order;

        android.content.ComponentName componentName;

        java.lang.String componentShortName;

        public Component(android.content.pm.PackageParser.Package owner, java.util.ArrayList<II> intents, java.lang.String className) {
            this.owner = owner;
            this.intents = intents;
            this.className = className;
        }

        public Component(android.content.pm.PackageParser.Package owner) {
            this.owner = owner;
            this.intents = null;
            this.className = null;
        }

        public Component(final android.content.pm.PackageParser.ParsePackageItemArgs args, final android.content.pm.PackageItemInfo outInfo) {
            owner = args.owner;
            intents = new java.util.ArrayList<II>(0);
            if (/* nameRequired */
            android.content.pm.PackageParser.parsePackageItemInfo(args.owner, outInfo, args.outError, args.tag, args.sa, true, args.nameRes, args.labelRes, args.iconRes, args.roundIconRes, args.logoRes, args.bannerRes)) {
                className = outInfo.name;
            } else {
                className = null;
            }
        }

        public Component(final android.content.pm.PackageParser.ParseComponentArgs args, final android.content.pm.ComponentInfo outInfo) {
            this(args, ((android.content.pm.PackageItemInfo) (outInfo)));
            if (args.outError[0] != null) {
                return;
            }
            if (args.processRes != 0) {
                java.lang.CharSequence pname;
                if (owner.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.FROYO) {
                    pname = args.sa.getNonConfigurationString(args.processRes, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
                } else {
                    // Some older apps have been seen to use a resource reference
                    // here that on older builds was ignored (with a warning).  We
                    // need to continue to do this for them so they don't break.
                    pname = args.sa.getNonResourceString(args.processRes);
                }
                outInfo.processName = android.content.pm.PackageParser.buildProcessName(owner.applicationInfo.packageName, owner.applicationInfo.processName, pname, args.flags, args.sepProcesses, args.outError);
            }
            if (args.descriptionRes != 0) {
                outInfo.descriptionRes = args.sa.getResourceId(args.descriptionRes, 0);
            }
            outInfo.enabled = args.sa.getBoolean(args.enabledRes, true);
        }

        public Component(android.content.pm.PackageParser.Component<II> clone) {
            owner = clone.owner;
            intents = clone.intents;
            className = clone.className;
            componentName = clone.componentName;
            componentShortName = clone.componentShortName;
        }

        @android.annotation.UnsupportedAppUsage
        public android.content.ComponentName getComponentName() {
            if (componentName != null) {
                return componentName;
            }
            if (className != null) {
                componentName = new android.content.ComponentName(owner.applicationInfo.packageName, className);
            }
            return componentName;
        }

        protected Component(android.os.Parcel in) {
            className = in.readString();
            metaData = in.readBundle();
            intents = android.content.pm.PackageParser.Component.createIntentsList(in);
            owner = null;
        }

        protected void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(className);
            dest.writeBundle(metaData);
            android.content.pm.PackageParser.Component.writeIntentsList(intents, dest, flags);
        }

        /**
         * <p>
         * Implementation note: The serialized form for the intent list also contains the name
         * of the concrete class that's stored in the list, and assumes that every element of the
         * list is of the same type. This is very similar to the original parcelable mechanism.
         * We cannot use that directly because IntentInfo extends IntentFilter, which is parcelable
         * and is public API. It also declares Parcelable related methods as final which means
         * we can't extend them. The approach of using composition instead of inheritance leads to
         * a large set of cascading changes in the PackageManagerService, which seem undesirable.
         *
         * <p>
         * <b>WARNING: </b> The list of objects returned by this function might need to be fixed up
         * to make sure their owner fields are consistent. See {@code fixupOwner}.
         */
        private static void writeIntentsList(java.util.ArrayList<? extends android.content.pm.PackageParser.IntentInfo> list, android.os.Parcel out, int flags) {
            if (list == null) {
                out.writeInt(-1);
                return;
            }
            final int N = list.size();
            out.writeInt(N);
            // Don't bother writing the component name if the list is empty.
            if (N > 0) {
                android.content.pm.PackageParser.IntentInfo info = list.get(0);
                out.writeString(info.getClass().getName());
                for (int i = 0; i < N; i++) {
                    list.get(i).writeIntentInfoToParcel(out, flags);
                }
            }
        }

        private static <T extends android.content.pm.PackageParser.IntentInfo> java.util.ArrayList<T> createIntentsList(android.os.Parcel in) {
            int N = in.readInt();
            if (N == (-1)) {
                return null;
            }
            if (N == 0) {
                return new java.util.ArrayList<>(0);
            }
            java.lang.String componentName = in.readString();
            final java.util.ArrayList<T> intentsList;
            try {
                final java.lang.Class<T> cls = ((java.lang.Class<T>) (java.lang.Class.forName(componentName)));
                final java.lang.reflect.Constructor<T> cons = cls.getConstructor(android.os.Parcel.class);
                intentsList = new java.util.ArrayList<>(N);
                for (int i = 0; i < N; ++i) {
                    intentsList.add(cons.newInstance(in));
                }
            } catch (java.lang.ReflectiveOperationException ree) {
                throw new java.lang.AssertionError("Unable to construct intent list for: " + componentName);
            }
            return intentsList;
        }

        public void appendComponentShortName(java.lang.StringBuilder sb) {
            android.content.ComponentName.appendShortString(sb, owner.applicationInfo.packageName, className);
        }

        public void printComponentShortName(java.io.PrintWriter pw) {
            android.content.ComponentName.printShortString(pw, owner.applicationInfo.packageName, className);
        }

        public void setPackageName(java.lang.String packageName) {
            componentName = null;
            componentShortName = null;
        }
    }

    public static final class Permission extends android.content.pm.PackageParser.Component<android.content.pm.PackageParser.IntentInfo> implements android.os.Parcelable {
        @android.annotation.UnsupportedAppUsage
        public final android.content.pm.PermissionInfo info;

        @android.annotation.UnsupportedAppUsage
        public boolean tree;

        @android.annotation.UnsupportedAppUsage
        public android.content.pm.PackageParser.PermissionGroup group;

        /**
         *
         *
         * @unknown 
         */
        public Permission(android.content.pm.PackageParser.Package owner, @android.annotation.Nullable
        java.lang.String backgroundPermission) {
            super(owner);
            info = new android.content.pm.PermissionInfo(backgroundPermission);
        }

        @android.annotation.UnsupportedAppUsage
        public Permission(android.content.pm.PackageParser.Package _owner, android.content.pm.PermissionInfo _info) {
            super(_owner);
            info = _info;
        }

        public void setPackageName(java.lang.String packageName) {
            super.setPackageName(packageName);
            info.packageName = packageName;
        }

        public java.lang.String toString() {
            return ((("Permission{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + info.name) + "}";
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(info, flags);
            dest.writeInt(tree ? 1 : 0);
            dest.writeParcelable(group, flags);
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isAppOp() {
            return info.isAppOp();
        }

        private Permission(android.os.Parcel in) {
            super(in);
            final java.lang.ClassLoader boot = java.lang.Object.class.getClassLoader();
            info = in.readParcelable(boot);
            if (info.group != null) {
                info.group = info.group.intern();
            }
            tree = in.readInt() == 1;
            group = in.readParcelable(boot);
        }

        public static final Parcelable.Creator CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageParser.Permission>() {
            public android.content.pm.Permission createFromParcel(android.os.Parcel in) {
                return new android.content.pm.Permission(in);
            }

            public android.content.pm.Permission[] newArray(int size) {
                return new android.content.pm.Permission[size];
            }
        };
    }

    public static final class PermissionGroup extends android.content.pm.PackageParser.Component<android.content.pm.PackageParser.IntentInfo> implements android.os.Parcelable {
        @android.annotation.UnsupportedAppUsage
        public final android.content.pm.PermissionGroupInfo info;

        public PermissionGroup(android.content.pm.PackageParser.Package owner, @android.annotation.StringRes
        int requestDetailResourceId, @android.annotation.StringRes
        int backgroundRequestResourceId, @android.annotation.StringRes
        int backgroundRequestDetailResourceId) {
            super(owner);
            info = new android.content.pm.PermissionGroupInfo(requestDetailResourceId, backgroundRequestResourceId, backgroundRequestDetailResourceId);
        }

        public PermissionGroup(android.content.pm.PackageParser.Package _owner, android.content.pm.PermissionGroupInfo _info) {
            super(_owner);
            info = _info;
        }

        public void setPackageName(java.lang.String packageName) {
            super.setPackageName(packageName);
            info.packageName = packageName;
        }

        public java.lang.String toString() {
            return ((("PermissionGroup{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + info.name) + "}";
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(info, flags);
        }

        private PermissionGroup(android.os.Parcel in) {
            super(in);
            info = in.readParcelable(java.lang.Object.class.getClassLoader());
        }

        public static final Parcelable.Creator CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageParser.PermissionGroup>() {
            public android.content.pm.PermissionGroup createFromParcel(android.os.Parcel in) {
                return new android.content.pm.PermissionGroup(in);
            }

            public android.content.pm.PermissionGroup[] newArray(int size) {
                return new android.content.pm.PermissionGroup[size];
            }
        };
    }

    private static boolean copyNeeded(int flags, android.content.pm.PackageParser.Package p, android.content.pm.PackageUserState state, android.os.Bundle metaData, int userId) {
        if (userId != android.os.UserHandle.USER_SYSTEM) {
            // We always need to copy for other users, since we need
            // to fix up the uid.
            return true;
        }
        if (state.enabled != android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
            boolean enabled = state.enabled == android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
            if (p.applicationInfo.enabled != enabled) {
                return true;
            }
        }
        boolean suspended = (p.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SUSPENDED) != 0;
        if (state.suspended != suspended) {
            return true;
        }
        if ((!state.installed) || state.hidden) {
            return true;
        }
        if (state.stopped) {
            return true;
        }
        if (state.instantApp != p.applicationInfo.isInstantApp()) {
            return true;
        }
        if (((flags & android.content.pm.PackageManager.GET_META_DATA) != 0) && ((metaData != null) || (p.mAppMetaData != null))) {
            return true;
        }
        if (((flags & android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES) != 0) && (p.usesLibraryFiles != null)) {
            return true;
        }
        if (((flags & android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES) != 0) && (p.usesLibraryInfos != null)) {
            return true;
        }
        if (p.staticSharedLibName != null) {
            return true;
        }
        return false;
    }

    @android.annotation.UnsupportedAppUsage
    public static android.content.pm.ApplicationInfo generateApplicationInfo(android.content.pm.PackageParser.Package p, int flags, android.content.pm.PackageUserState state) {
        return android.content.pm.PackageParser.generateApplicationInfo(p, flags, state, android.os.UserHandle.getCallingUserId());
    }

    private static void updateApplicationInfo(android.content.pm.ApplicationInfo ai, int flags, android.content.pm.PackageUserState state) {
        // CompatibilityMode is global state.
        if (!android.content.pm.PackageParser.sCompatibilityModeEnabled) {
            ai.disableCompatibilityMode();
        }
        if (state.installed) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_INSTALLED;
        } else {
            ai.flags &= ~android.content.pm.ApplicationInfo.FLAG_INSTALLED;
        }
        if (state.suspended) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_SUSPENDED;
        } else {
            ai.flags &= ~android.content.pm.ApplicationInfo.FLAG_SUSPENDED;
        }
        if (state.instantApp) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_INSTANT;
        } else {
            ai.privateFlags &= ~android.content.pm.ApplicationInfo.PRIVATE_FLAG_INSTANT;
        }
        if (state.virtualPreload) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_VIRTUAL_PRELOAD;
        } else {
            ai.privateFlags &= ~android.content.pm.ApplicationInfo.PRIVATE_FLAG_VIRTUAL_PRELOAD;
        }
        if (state.hidden) {
            ai.privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_HIDDEN;
        } else {
            ai.privateFlags &= ~android.content.pm.ApplicationInfo.PRIVATE_FLAG_HIDDEN;
        }
        if (state.enabled == android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            ai.enabled = true;
        } else
            if (state.enabled == android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                ai.enabled = (flags & android.content.pm.PackageManager.GET_DISABLED_UNTIL_USED_COMPONENTS) != 0;
            } else
                if ((state.enabled == android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED) || (state.enabled == android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER)) {
                    ai.enabled = false;
                }


        ai.enabledSetting = state.enabled;
        if (ai.category == android.content.pm.ApplicationInfo.CATEGORY_UNDEFINED) {
            ai.category = state.categoryHint;
        }
        if (ai.category == android.content.pm.ApplicationInfo.CATEGORY_UNDEFINED) {
            ai.category = android.content.pm.FallbackCategoryProvider.getFallbackCategory(ai.packageName);
        }
        ai.seInfoUser = android.content.pm.SELinuxUtil.assignSeinfoUser(state);
        ai.resourceDirs = state.overlayPaths;
        ai.icon = (android.content.pm.PackageParser.sUseRoundIcon && (ai.roundIconRes != 0)) ? ai.roundIconRes : ai.iconRes;
    }

    @android.annotation.UnsupportedAppUsage
    public static android.content.pm.ApplicationInfo generateApplicationInfo(android.content.pm.PackageParser.Package p, int flags, android.content.pm.PackageUserState state, int userId) {
        if (p == null)
            return null;

        if ((!android.content.pm.PackageParser.checkUseInstalledOrHidden(flags, state, p.applicationInfo)) || (!p.isMatch(flags))) {
            return null;
        }
        if ((!android.content.pm.PackageParser.copyNeeded(flags, p, state, null, userId)) && (((flags & android.content.pm.PackageManager.GET_DISABLED_UNTIL_USED_COMPONENTS) == 0) || (state.enabled != android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED))) {
            // In this case it is safe to directly modify the internal ApplicationInfo state:
            // - CompatibilityMode is global state, so will be the same for every call.
            // - We only come in to here if the app should reported as installed; this is the
            // default state, and we will do a copy otherwise.
            // - The enable state will always be reported the same for the application across
            // calls; the only exception is for the UNTIL_USED mode, and in that case we will
            // be doing a copy.
            android.content.pm.PackageParser.updateApplicationInfo(p.applicationInfo, flags, state);
            return p.applicationInfo;
        }
        // Make shallow copy so we can store the metadata/libraries safely
        android.content.pm.ApplicationInfo ai = new android.content.pm.ApplicationInfo(p.applicationInfo);
        ai.initForUser(userId);
        if ((flags & android.content.pm.PackageManager.GET_META_DATA) != 0) {
            ai.metaData = p.mAppMetaData;
        }
        if ((flags & android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES) != 0) {
            ai.sharedLibraryFiles = p.usesLibraryFiles;
            ai.sharedLibraryInfos = p.usesLibraryInfos;
        }
        if (state.stopped) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_STOPPED;
        } else {
            ai.flags &= ~android.content.pm.ApplicationInfo.FLAG_STOPPED;
        }
        android.content.pm.PackageParser.updateApplicationInfo(ai, flags, state);
        return ai;
    }

    public static android.content.pm.ApplicationInfo generateApplicationInfo(android.content.pm.ApplicationInfo ai, int flags, android.content.pm.PackageUserState state, int userId) {
        if (ai == null)
            return null;

        if (!android.content.pm.PackageParser.checkUseInstalledOrHidden(flags, state, ai)) {
            return null;
        }
        // This is only used to return the ResolverActivity; we will just always
        // make a copy.
        ai = new android.content.pm.ApplicationInfo(ai);
        ai.initForUser(userId);
        if (state.stopped) {
            ai.flags |= android.content.pm.ApplicationInfo.FLAG_STOPPED;
        } else {
            ai.flags &= ~android.content.pm.ApplicationInfo.FLAG_STOPPED;
        }
        android.content.pm.PackageParser.updateApplicationInfo(ai, flags, state);
        return ai;
    }

    @android.annotation.UnsupportedAppUsage
    public static final android.content.pm.PermissionInfo generatePermissionInfo(android.content.pm.PackageParser.Permission p, int flags) {
        if (p == null)
            return null;

        if ((flags & android.content.pm.PackageManager.GET_META_DATA) == 0) {
            return p.info;
        }
        android.content.pm.PermissionInfo pi = new android.content.pm.PermissionInfo(p.info);
        pi.metaData = p.metaData;
        return pi;
    }

    @android.annotation.UnsupportedAppUsage
    public static final android.content.pm.PermissionGroupInfo generatePermissionGroupInfo(android.content.pm.PackageParser.PermissionGroup pg, int flags) {
        if (pg == null)
            return null;

        if ((flags & android.content.pm.PackageManager.GET_META_DATA) == 0) {
            return pg.info;
        }
        android.content.pm.PermissionGroupInfo pgi = new android.content.pm.PermissionGroupInfo(pg.info);
        pgi.metaData = pg.metaData;
        return pgi;
    }

    public static final class Activity extends android.content.pm.PackageParser.Component<android.content.pm.PackageParser.ActivityIntentInfo> implements android.os.Parcelable {
        @android.annotation.UnsupportedAppUsage
        public final android.content.pm.ActivityInfo info;

        private boolean mHasMaxAspectRatio;

        private boolean mHasMinAspectRatio;

        private boolean hasMaxAspectRatio() {
            return mHasMaxAspectRatio;
        }

        private boolean hasMinAspectRatio() {
            return mHasMinAspectRatio;
        }

        // To construct custom activity which does not exist in manifest
        Activity(final android.content.pm.PackageParser.Package owner, final java.lang.String className, final android.content.pm.ActivityInfo info) {
            super(owner, new java.util.ArrayList<>(0), className);
            this.info = info;
            this.info.applicationInfo = owner.applicationInfo;
        }

        public Activity(final android.content.pm.PackageParser.ParseComponentArgs args, final android.content.pm.ActivityInfo _info) {
            super(args, _info);
            info = _info;
            info.applicationInfo = args.owner.applicationInfo;
        }

        public void setPackageName(java.lang.String packageName) {
            super.setPackageName(packageName);
            info.packageName = packageName;
        }

        private void setMaxAspectRatio(float maxAspectRatio) {
            if ((info.resizeMode == android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE) || (info.resizeMode == android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION)) {
                // Resizeable activities can be put in any aspect ratio.
                return;
            }
            if ((maxAspectRatio < 1.0F) && (maxAspectRatio != 0)) {
                // Ignore any value lesser than 1.0.
                return;
            }
            info.maxAspectRatio = maxAspectRatio;
            mHasMaxAspectRatio = true;
        }

        private void setMinAspectRatio(float minAspectRatio) {
            if ((info.resizeMode == android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE) || (info.resizeMode == android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION)) {
                // Resizeable activities can be put in any aspect ratio.
                return;
            }
            if ((minAspectRatio < 1.0F) && (minAspectRatio != 0)) {
                // Ignore any value lesser than 1.0.
                return;
            }
            info.minAspectRatio = minAspectRatio;
            mHasMinAspectRatio = true;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("Activity{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(info, flags | android.os.Parcelable.PARCELABLE_ELIDE_DUPLICATES);
            dest.writeBoolean(mHasMaxAspectRatio);
            dest.writeBoolean(mHasMinAspectRatio);
        }

        private Activity(android.os.Parcel in) {
            super(in);
            info = in.readParcelable(java.lang.Object.class.getClassLoader());
            mHasMaxAspectRatio = in.readBoolean();
            mHasMinAspectRatio = in.readBoolean();
            for (android.content.pm.PackageParser.ActivityIntentInfo aii : intents) {
                aii.activity = this;
                order = java.lang.Math.max(aii.getOrder(), order);
            }
            if (info.permission != null) {
                info.permission = info.permission.intern();
            }
        }

        public static final Parcelable.Creator CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageParser.Activity>() {
            public android.content.pm.Activity createFromParcel(android.os.Parcel in) {
                return new android.content.pm.Activity(in);
            }

            public android.content.pm.Activity[] newArray(int size) {
                return new android.content.pm.Activity[size];
            }
        };
    }

    @android.annotation.UnsupportedAppUsage
    public static final android.content.pm.ActivityInfo generateActivityInfo(android.content.pm.PackageParser.Activity a, int flags, android.content.pm.PackageUserState state, int userId) {
        if (a == null)
            return null;

        if (!android.content.pm.PackageParser.checkUseInstalledOrHidden(flags, state, a.owner.applicationInfo)) {
            return null;
        }
        if (!android.content.pm.PackageParser.copyNeeded(flags, a.owner, state, a.metaData, userId)) {
            android.content.pm.PackageParser.updateApplicationInfo(a.info.applicationInfo, flags, state);
            return a.info;
        }
        // Make shallow copies so we can store the metadata safely
        android.content.pm.ActivityInfo ai = new android.content.pm.ActivityInfo(a.info);
        ai.metaData = a.metaData;
        ai.applicationInfo = android.content.pm.PackageParser.generateApplicationInfo(a.owner, flags, state, userId);
        return ai;
    }

    public static final android.content.pm.ActivityInfo generateActivityInfo(android.content.pm.ActivityInfo ai, int flags, android.content.pm.PackageUserState state, int userId) {
        if (ai == null)
            return null;

        if (!android.content.pm.PackageParser.checkUseInstalledOrHidden(flags, state, ai.applicationInfo)) {
            return null;
        }
        // This is only used to return the ResolverActivity; we will just always
        // make a copy.
        ai = new android.content.pm.ActivityInfo(ai);
        ai.applicationInfo = android.content.pm.PackageParser.generateApplicationInfo(ai.applicationInfo, flags, state, userId);
        return ai;
    }

    public static final class Service extends android.content.pm.PackageParser.Component<android.content.pm.PackageParser.ServiceIntentInfo> implements android.os.Parcelable {
        @android.annotation.UnsupportedAppUsage
        public final android.content.pm.ServiceInfo info;

        public Service(final android.content.pm.PackageParser.ParseComponentArgs args, final android.content.pm.ServiceInfo _info) {
            super(args, _info);
            info = _info;
            info.applicationInfo = args.owner.applicationInfo;
        }

        public void setPackageName(java.lang.String packageName) {
            super.setPackageName(packageName);
            info.packageName = packageName;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("Service{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(info, flags | android.os.Parcelable.PARCELABLE_ELIDE_DUPLICATES);
        }

        private Service(android.os.Parcel in) {
            super(in);
            info = in.readParcelable(java.lang.Object.class.getClassLoader());
            for (android.content.pm.PackageParser.ServiceIntentInfo aii : intents) {
                aii.service = this;
                order = java.lang.Math.max(aii.getOrder(), order);
            }
            if (info.permission != null) {
                info.permission = info.permission.intern();
            }
        }

        public static final Parcelable.Creator CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageParser.Service>() {
            public android.content.pm.Service createFromParcel(android.os.Parcel in) {
                return new android.content.pm.Service(in);
            }

            public android.content.pm.Service[] newArray(int size) {
                return new android.content.pm.Service[size];
            }
        };
    }

    @android.annotation.UnsupportedAppUsage
    public static final android.content.pm.ServiceInfo generateServiceInfo(android.content.pm.PackageParser.Service s, int flags, android.content.pm.PackageUserState state, int userId) {
        if (s == null)
            return null;

        if (!android.content.pm.PackageParser.checkUseInstalledOrHidden(flags, state, s.owner.applicationInfo)) {
            return null;
        }
        if (!android.content.pm.PackageParser.copyNeeded(flags, s.owner, state, s.metaData, userId)) {
            android.content.pm.PackageParser.updateApplicationInfo(s.info.applicationInfo, flags, state);
            return s.info;
        }
        // Make shallow copies so we can store the metadata safely
        android.content.pm.ServiceInfo si = new android.content.pm.ServiceInfo(s.info);
        si.metaData = s.metaData;
        si.applicationInfo = android.content.pm.PackageParser.generateApplicationInfo(s.owner, flags, state, userId);
        return si;
    }

    public static final class Provider extends android.content.pm.PackageParser.Component<android.content.pm.PackageParser.ProviderIntentInfo> implements android.os.Parcelable {
        @android.annotation.UnsupportedAppUsage
        public final android.content.pm.ProviderInfo info;

        @android.annotation.UnsupportedAppUsage
        public boolean syncable;

        public Provider(final android.content.pm.PackageParser.ParseComponentArgs args, final android.content.pm.ProviderInfo _info) {
            super(args, _info);
            info = _info;
            info.applicationInfo = args.owner.applicationInfo;
            syncable = false;
        }

        @android.annotation.UnsupportedAppUsage
        public Provider(android.content.pm.PackageParser.Provider existingProvider) {
            super(existingProvider);
            this.info = existingProvider.info;
            this.syncable = existingProvider.syncable;
        }

        public void setPackageName(java.lang.String packageName) {
            super.setPackageName(packageName);
            info.packageName = packageName;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("Provider{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(info, flags | android.os.Parcelable.PARCELABLE_ELIDE_DUPLICATES);
            dest.writeInt(syncable ? 1 : 0);
        }

        private Provider(android.os.Parcel in) {
            super(in);
            info = in.readParcelable(java.lang.Object.class.getClassLoader());
            syncable = in.readInt() == 1;
            for (android.content.pm.PackageParser.ProviderIntentInfo aii : intents) {
                aii.provider = this;
            }
            if (info.readPermission != null) {
                info.readPermission = info.readPermission.intern();
            }
            if (info.writePermission != null) {
                info.writePermission = info.writePermission.intern();
            }
            if (info.authority != null) {
                info.authority = info.authority.intern();
            }
        }

        public static final Parcelable.Creator CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageParser.Provider>() {
            public android.content.pm.Provider createFromParcel(android.os.Parcel in) {
                return new android.content.pm.Provider(in);
            }

            public android.content.pm.Provider[] newArray(int size) {
                return new android.content.pm.Provider[size];
            }
        };
    }

    @android.annotation.UnsupportedAppUsage
    public static final android.content.pm.ProviderInfo generateProviderInfo(android.content.pm.PackageParser.Provider p, int flags, android.content.pm.PackageUserState state, int userId) {
        if (p == null)
            return null;

        if (!android.content.pm.PackageParser.checkUseInstalledOrHidden(flags, state, p.owner.applicationInfo)) {
            return null;
        }
        if ((!android.content.pm.PackageParser.copyNeeded(flags, p.owner, state, p.metaData, userId)) && (((flags & android.content.pm.PackageManager.GET_URI_PERMISSION_PATTERNS) != 0) || (p.info.uriPermissionPatterns == null))) {
            android.content.pm.PackageParser.updateApplicationInfo(p.info.applicationInfo, flags, state);
            return p.info;
        }
        // Make shallow copies so we can store the metadata safely
        android.content.pm.ProviderInfo pi = new android.content.pm.ProviderInfo(p.info);
        pi.metaData = p.metaData;
        if ((flags & android.content.pm.PackageManager.GET_URI_PERMISSION_PATTERNS) == 0) {
            pi.uriPermissionPatterns = null;
        }
        pi.applicationInfo = android.content.pm.PackageParser.generateApplicationInfo(p.owner, flags, state, userId);
        return pi;
    }

    public static final class Instrumentation extends android.content.pm.PackageParser.Component<android.content.pm.PackageParser.IntentInfo> implements android.os.Parcelable {
        @android.annotation.UnsupportedAppUsage
        public final android.content.pm.InstrumentationInfo info;

        public Instrumentation(final android.content.pm.PackageParser.ParsePackageItemArgs args, final android.content.pm.InstrumentationInfo _info) {
            super(args, _info);
            info = _info;
        }

        public void setPackageName(java.lang.String packageName) {
            super.setPackageName(packageName);
            info.packageName = packageName;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("Instrumentation{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(info, flags);
        }

        private Instrumentation(android.os.Parcel in) {
            super(in);
            info = in.readParcelable(java.lang.Object.class.getClassLoader());
            if (info.targetPackage != null) {
                info.targetPackage = info.targetPackage.intern();
            }
            if (info.targetProcesses != null) {
                info.targetProcesses = info.targetProcesses.intern();
            }
        }

        public static final Parcelable.Creator CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageParser.Instrumentation>() {
            public android.content.pm.Instrumentation createFromParcel(android.os.Parcel in) {
                return new android.content.pm.Instrumentation(in);
            }

            public android.content.pm.Instrumentation[] newArray(int size) {
                return new android.content.pm.Instrumentation[size];
            }
        };
    }

    @android.annotation.UnsupportedAppUsage
    public static final android.content.pm.InstrumentationInfo generateInstrumentationInfo(android.content.pm.PackageParser.Instrumentation i, int flags) {
        if (i == null)
            return null;

        if ((flags & android.content.pm.PackageManager.GET_META_DATA) == 0) {
            return i.info;
        }
        android.content.pm.InstrumentationInfo ii = new android.content.pm.InstrumentationInfo(i.info);
        ii.metaData = i.metaData;
        return ii;
    }

    public static abstract class IntentInfo extends android.content.IntentFilter {
        @android.annotation.UnsupportedAppUsage
        public boolean hasDefault;

        @android.annotation.UnsupportedAppUsage
        public int labelRes;

        @android.annotation.UnsupportedAppUsage
        public java.lang.CharSequence nonLocalizedLabel;

        @android.annotation.UnsupportedAppUsage
        public int icon;

        @android.annotation.UnsupportedAppUsage
        public int logo;

        @android.annotation.UnsupportedAppUsage
        public int banner;

        public int preferred;

        @android.annotation.UnsupportedAppUsage
        protected IntentInfo() {
        }

        protected IntentInfo(android.os.Parcel dest) {
            super(dest);
            hasDefault = dest.readInt() == 1;
            labelRes = dest.readInt();
            nonLocalizedLabel = dest.readCharSequence();
            icon = dest.readInt();
            logo = dest.readInt();
            banner = dest.readInt();
            preferred = dest.readInt();
        }

        public void writeIntentInfoToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(hasDefault ? 1 : 0);
            dest.writeInt(labelRes);
            dest.writeCharSequence(nonLocalizedLabel);
            dest.writeInt(icon);
            dest.writeInt(logo);
            dest.writeInt(banner);
            dest.writeInt(preferred);
        }
    }

    public static final class ActivityIntentInfo extends android.content.pm.PackageParser.IntentInfo {
        @android.annotation.UnsupportedAppUsage
        public android.content.pm.PackageParser.Activity activity;

        public ActivityIntentInfo(android.content.pm.PackageParser.Activity _activity) {
            activity = _activity;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("ActivityIntentInfo{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            activity.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public ActivityIntentInfo(android.os.Parcel in) {
            super(in);
        }
    }

    public static final class ServiceIntentInfo extends android.content.pm.PackageParser.IntentInfo {
        @android.annotation.UnsupportedAppUsage
        public android.content.pm.PackageParser.Service service;

        public ServiceIntentInfo(android.content.pm.PackageParser.Service _service) {
            service = _service;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("ServiceIntentInfo{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            service.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public ServiceIntentInfo(android.os.Parcel in) {
            super(in);
        }
    }

    public static final class ProviderIntentInfo extends android.content.pm.PackageParser.IntentInfo {
        @android.annotation.UnsupportedAppUsage
        public android.content.pm.PackageParser.Provider provider;

        public ProviderIntentInfo(android.content.pm.PackageParser.Provider provider) {
            this.provider = provider;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("ProviderIntentInfo{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            provider.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public ProviderIntentInfo(android.os.Parcel in) {
            super(in);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void setCompatibilityModeEnabled(boolean compatibilityModeEnabled) {
        android.content.pm.PackageParser.sCompatibilityModeEnabled = compatibilityModeEnabled;
    }

    /**
     *
     *
     * @unknown 
     */
    public static void readConfigUseRoundIcon(android.content.res.Resources r) {
        if (r != null) {
            android.content.pm.PackageParser.sUseRoundIcon = r.getBoolean(com.android.internal.R.bool.config_useRoundIcon);
            return;
        }
        android.content.pm.ApplicationInfo androidAppInfo;
        try {
            androidAppInfo = /* flags */
            android.app.ActivityThread.getPackageManager().getApplicationInfo("android", 0, android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        android.content.res.Resources systemResources = android.content.res.Resources.getSystem();
        // Create in-flight as this overlayable resource is only used when config changes
        android.content.res.Resources overlayableRes = android.app.ResourcesManager.getInstance().getResources(null, null, null, androidAppInfo.resourceDirs, androidAppInfo.sharedLibraryFiles, android.view.Display.DEFAULT_DISPLAY, null, systemResources.getCompatibilityInfo(), systemResources.getClassLoader());
        android.content.pm.PackageParser.sUseRoundIcon = overlayableRes.getBoolean(com.android.internal.R.bool.config_useRoundIcon);
    }

    public static class PackageParserException extends java.lang.Exception {
        public final int error;

        public PackageParserException(int error, java.lang.String detailMessage) {
            super(detailMessage);
            this.error = error;
        }

        public PackageParserException(int error, java.lang.String detailMessage, java.lang.Throwable throwable) {
            super(detailMessage, throwable);
            this.error = error;
        }
    }

    // TODO(b/129261524): Clean up API
    /**
     * PackageInfo parser specifically for apex files.
     * NOTE: It will collect certificates
     *
     * @param apexInfo
     * 		
     * @return PackageInfo
     * @throws PackageParserException
     * 		
     */
    public static android.content.pm.PackageInfo generatePackageInfoFromApex(android.apex.ApexInfo apexInfo, int flags) throws android.content.pm.PackageParser.PackageParserException {
        android.content.pm.PackageParser pp = new android.content.pm.PackageParser();
        java.io.File apexFile = new java.io.File(apexInfo.packagePath);
        final android.content.pm.PackageParser.Package p = pp.parsePackage(apexFile, flags, false);
        android.content.pm.PackageUserState state = new android.content.pm.PackageUserState();
        android.content.pm.PackageInfo pi = android.content.pm.PackageParser.generatePackageInfo(p, EmptyArray.INT, flags, 0, 0, java.util.Collections.emptySet(), state);
        pi.applicationInfo.sourceDir = apexFile.getPath();
        pi.applicationInfo.publicSourceDir = apexFile.getPath();
        if (apexInfo.isFactory) {
            pi.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_SYSTEM;
        } else {
            pi.applicationInfo.flags &= ~android.content.pm.ApplicationInfo.FLAG_SYSTEM;
        }
        if (apexInfo.isActive) {
            pi.applicationInfo.flags |= android.content.pm.ApplicationInfo.FLAG_INSTALLED;
        } else {
            pi.applicationInfo.flags &= ~android.content.pm.ApplicationInfo.FLAG_INSTALLED;
        }
        pi.isApex = true;
        // Collect certificates
        if ((flags & android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES) != 0) {
            android.content.pm.PackageParser.collectCertificates(p, apexFile, false);
            // Keep legacy mechanism for handling signatures. While this is deprecated, it's
            // still part of the public API and needs to be maintained
            if (p.mSigningDetails.hasPastSigningCertificates()) {
                // Package has included signing certificate rotation information.  Return
                // the oldest cert so that programmatic checks keep working even if unaware
                // of key rotation.
                pi.signatures = new android.content.pm.Signature[1];
                pi.signatures[0] = p.mSigningDetails.pastSigningCertificates[0];
            } else
                if (p.mSigningDetails.hasSignatures()) {
                    // otherwise keep old behavior
                    int numberOfSigs = p.mSigningDetails.signatures.length;
                    pi.signatures = new android.content.pm.Signature[numberOfSigs];
                    java.lang.System.arraycopy(p.mSigningDetails.signatures, 0, pi.signatures, 0, numberOfSigs);
                }

            if (p.mSigningDetails != android.content.pm.PackageParser.SigningDetails.UNKNOWN) {
                // only return a valid SigningInfo if there is signing information to report
                pi.signingInfo = new android.content.pm.SigningInfo(p.mSigningDetails);
            } else {
                pi.signingInfo = null;
            }
        }
        return pi;
    }
}

