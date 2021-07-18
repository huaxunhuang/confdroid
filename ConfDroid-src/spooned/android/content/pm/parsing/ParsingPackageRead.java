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
 * Everything written by {@link ParsingPackage} and readable back.
 *
 * @unknown 
 */
@java.lang.SuppressWarnings("UnusedReturnValue")
public interface ParsingPackageRead extends android.os.Parcelable {
    /**
     *
     *
     * @see ActivityInfo
     * @see PackageInfo#activities
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.parsing.component.ParsedActivity> getActivities();

    /**
     * The names of packages to adopt ownership of permissions from, parsed under
     * {@link PackageParser#TAG_ADOPT_PERMISSIONS}.
     *
     * @see R.styleable#AndroidManifestOriginalPackage_name
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getAdoptPermissions();

    /**
     *
     *
     * @see PackageInfo#configPreferences
     * @see R.styleable#AndroidManifestUsesConfiguration
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.ConfigurationInfo> getConfigPreferences();

    @android.annotation.NonNull
    java.util.List<android.content.pm.parsing.component.ParsedAttribution> getAttributions();

    /**
     *
     *
     * @see PackageInfo#featureGroups
     * @see R.styleable#AndroidManifestUsesFeature
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.FeatureGroupInfo> getFeatureGroups();

    /**
     * Permissions requested but not in the manifest. These may have been split or migrated from
     * previous versions/definitions.
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getImplicitPermissions();

    /**
     *
     *
     * @see android.content.pm.InstrumentationInfo
     * @see PackageInfo#instrumentation
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.parsing.component.ParsedInstrumentation> getInstrumentations();

    /**
     * For use with {@link com.android.server.pm.KeySetManagerService}. Parsed in
     * {@link PackageParser#TAG_KEY_SETS}.
     *
     * @see R.styleable#AndroidManifestKeySet
     * @see R.styleable#AndroidManifestPublicKey
     */
    @android.annotation.NonNull
    java.util.Map<java.lang.String, android.util.ArraySet<java.security.PublicKey>> getKeySetMapping();

    /**
     * Library names this package is declared as, for use by other packages with "uses-library".
     *
     * @see R.styleable#AndroidManifestLibrary
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getLibraryNames();

    /**
     * For system use to migrate from an old package name to a new one, moving over data
     * if available.
     *
     * @see R.styleable#AndroidManifestOriginalPackage}
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getOriginalPackages();

    /**
     * Map of overlayable name to actor name.
     */
    @android.annotation.NonNull
    java.util.Map<java.lang.String, java.lang.String> getOverlayables();

    /**
     *
     *
     * @see android.content.pm.PermissionInfo
     * @see PackageInfo#permissions
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.parsing.component.ParsedPermission> getPermissions();

    /**
     *
     *
     * @see android.content.pm.PermissionGroupInfo
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.parsing.component.ParsedPermissionGroup> getPermissionGroups();

    /**
     * Used to determine the default preferred handler of an {@link Intent}.
     *
     * Map of component className to intent info inside that component.
     * TODO(b/135203078): Is this actually used/working?
     */
    @android.annotation.NonNull
    java.util.List<android.util.Pair<java.lang.String, android.content.pm.parsing.component.ParsedIntentInfo>> getPreferredActivityFilters();

    /**
     * System protected broadcasts.
     *
     * @see R.styleable#AndroidManifestProtectedBroadcast
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getProtectedBroadcasts();

    /**
     *
     *
     * @see android.content.pm.ProviderInfo
     * @see PackageInfo#providers
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.parsing.component.ParsedProvider> getProviders();

    /**
     *
     *
     * @see android.content.pm.ProcessInfo
     */
    @android.annotation.NonNull
    java.util.Map<java.lang.String, android.content.pm.parsing.component.ParsedProcess> getProcesses();

    /**
     * Since they share several attributes, receivers are parsed as {@link ParsedActivity}, even
     * though they represent different functionality.
     * TODO(b/135203078): Reconsider this and maybe make ParsedReceiver so it's not so confusing
     *
     * @see ActivityInfo
     * @see PackageInfo#receivers
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.parsing.component.ParsedActivity> getReceivers();

    /**
     *
     *
     * @see PackageInfo#reqFeatures
     * @see R.styleable#AndroidManifestUsesFeature
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.FeatureInfo> getReqFeatures();

    /**
     * All the permissions declared. This is an effective set, and may include permissions
     * transformed from split/migrated permissions from previous versions, so may not be exactly
     * what the package declares in its manifest.
     *
     * @see PackageInfo#requestedPermissions
     * @see R.styleable#AndroidManifestUsesPermission
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getRequestedPermissions();

    /**
     * Whether or not the app requested explicitly resizeable Activities.
     * A null value means nothing was explicitly requested.
     */
    @android.annotation.Nullable
    java.lang.Boolean getResizeableActivity();

    /**
     *
     *
     * @see ServiceInfo
     * @see PackageInfo#services
     */
    @android.annotation.NonNull
    java.util.List<android.content.pm.parsing.component.ParsedService> getServices();

    /**
     *
     *
     * @see R.styleable#AndroidManifestUsesLibrary
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getUsesLibraries();

    /**
     * Like {@link #getUsesLibraries()}, but marked optional by setting
     * {@link R.styleable#AndroidManifestUsesLibrary_required} to false . Application is expected
     * to handle absence manually.
     *
     * @see R.styleable#AndroidManifestUsesLibrary
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getUsesOptionalLibraries();

    /**
     * TODO(b/135203078): Move static library stuff to an inner data class
     *
     * @see R.styleable#AndroidManifestUsesStaticLibrary
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getUsesStaticLibraries();

    /**
     *
     *
     * @see R.styleable#AndroidManifestUsesStaticLibrary_certDigest
     */
    @android.annotation.Nullable
    java.lang.String[][] getUsesStaticLibrariesCertDigests();

    /**
     *
     *
     * @see R.styleable#AndroidManifestUsesStaticLibrary_version
     */
    @android.annotation.Nullable
    long[] getUsesStaticLibrariesVersions();

    /**
     * Intents that this package may query or require and thus requires visibility into.
     *
     * @see R.styleable#AndroidManifestQueriesIntent
     */
    @android.annotation.NonNull
    java.util.List<android.content.Intent> getQueriesIntents();

    /**
     * Other packages that this package may query or require and thus requires visibility into.
     *
     * @see R.styleable#AndroidManifestQueriesPackage
     */
    @android.annotation.NonNull
    java.util.List<java.lang.String> getQueriesPackages();

    /**
     * Authorities that this package may query or require and thus requires visibility into.
     *
     * @see R.styleable#AndroidManifestQueriesProvider
     */
    @android.annotation.NonNull
    java.util.Set<java.lang.String> getQueriesProviders();

    /**
     * We store the application meta-data independently to avoid multiple unwanted references
     * TODO(b/135203078): What does this comment mean?
     * TODO(b/135203078): Make all the Bundles immutable (and non-null by shared empty reference?)
     */
    @android.annotation.Nullable
    android.os.Bundle getMetaData();

    /**
     *
     *
     * @see R.styleable#AndroidManifestApplication_forceQueryable
     */
    boolean isForceQueryable();

    /**
     *
     *
     * @see ApplicationInfo#maxAspectRatio
     * @see R.styleable#AndroidManifestApplication_maxAspectRatio
     */
    float getMaxAspectRatio();

    /**
     *
     *
     * @see ApplicationInfo#minAspectRatio
     * @see R.styleable#AndroidManifestApplication_minAspectRatio
     */
    float getMinAspectRatio();

    /**
     *
     *
     * @see ApplicationInfo#permission
     * @see R.styleable#AndroidManifestApplication_permission
     */
    @android.annotation.Nullable
    java.lang.String getPermission();

    /**
     *
     *
     * @see ApplicationInfo#processName
     * @see R.styleable#AndroidManifestApplication_process
     */
    @android.annotation.NonNull
    java.lang.String getProcessName();

    /**
     *
     *
     * @see PackageInfo#sharedUserId
     * @see R.styleable#AndroidManifest_sharedUserId
     */
    @java.lang.Deprecated
    @android.annotation.Nullable
    java.lang.String getSharedUserId();

    /**
     *
     *
     * @see R.styleable#AndroidManifestStaticLibrary_name
     */
    @android.annotation.Nullable
    java.lang.String getStaticSharedLibName();

    /**
     *
     *
     * @see ApplicationInfo#taskAffinity
     * @see R.styleable#AndroidManifestApplication_taskAffinity
     */
    @android.annotation.Nullable
    java.lang.String getTaskAffinity();

    /**
     *
     *
     * @see ApplicationInfo#targetSdkVersion
     * @see R.styleable#AndroidManifestUsesSdk_targetSdkVersion
     */
    int getTargetSdkVersion();

    /**
     *
     *
     * @see ApplicationInfo#uiOptions
     * @see R.styleable#AndroidManifestApplication_uiOptions
     */
    int getUiOptions();

    boolean isCrossProfile();

    boolean isResizeableActivityViaSdkVersion();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_HARDWARE_ACCELERATED
     */
    boolean isBaseHardwareAccelerated();

    /**
     * If omitted from manifest, returns true if {@link #getTargetSdkVersion()} >=
     * {@link android.os.Build.VERSION_CODES#DONUT}.
     *
     * @see R.styleable#AndroidManifestSupportsScreens_resizeable
     * @see ApplicationInfo#FLAG_RESIZEABLE_FOR_SCREENS
     */
    boolean isResizeable();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_ALLOW_AUDIO_PLAYBACK_CAPTURE
     */
    boolean isAllowAudioPlaybackCapture();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_ALLOW_BACKUP
     */
    boolean isAllowBackup();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_ALLOW_CLEAR_USER_DATA
     */
    boolean isAllowClearUserData();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_ALLOW_CLEAR_USER_DATA_ON_FAILED_RESTORE
     */
    boolean isAllowClearUserDataOnFailedRestore();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_ALLOW_TASK_REPARENTING
     */
    boolean isAllowTaskReparenting();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_IS_RESOURCE_OVERLAY
     * @see ApplicationInfo#isResourceOverlay()
     */
    boolean isOverlay();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_BACKUP_IN_FOREGROUND
     */
    boolean isBackupInForeground();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_CANT_SAVE_STATE
     */
    boolean isCantSaveState();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_DEBUGGABLE
     */
    boolean isDebuggable();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_DEFAULT_TO_DEVICE_PROTECTED_STORAGE
     */
    boolean isDefaultToDeviceProtectedStorage();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_DIRECT_BOOT_AWARE
     */
    boolean isDirectBootAware();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_EXTERNAL_STORAGE
     */
    boolean isExternalStorage();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_EXTRACT_NATIVE_LIBS
     */
    boolean isExtractNativeLibs();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_FULL_BACKUP_ONLY
     */
    boolean isFullBackupOnly();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_HAS_CODE
     */
    boolean isHasCode();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_HAS_FRAGILE_USER_DATA
     */
    boolean isHasFragileUserData();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_IS_GAME
     */
    @java.lang.Deprecated
    boolean isGame();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_ISOLATED_SPLIT_LOADING
     */
    boolean isIsolatedSplitLoading();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_KILL_AFTER_RESTORE
     */
    boolean isKillAfterRestore();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_LARGE_HEAP
     */
    boolean isLargeHeap();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_MULTIARCH
     */
    boolean isMultiArch();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_PARTIALLY_DIRECT_BOOT_AWARE
     */
    boolean isPartiallyDirectBootAware();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_PERSISTENT
     */
    boolean isPersistent();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_PROFILEABLE_BY_SHELL
     */
    boolean isProfileableByShell();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_REQUEST_LEGACY_EXTERNAL_STORAGE
     */
    boolean isRequestLegacyExternalStorage();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_RESTORE_ANY_VERSION
     */
    boolean isRestoreAnyVersion();

    // ParsingPackageRead setSplitHasCode(int splitIndex, boolean splitHasCode);
    /**
     * Flags of any split APKs; ordered by parsed splitName
     */
    @android.annotation.Nullable
    int[] getSplitFlags();

    /**
     *
     *
     * @see ApplicationInfo#splitSourceDirs
     */
    @android.annotation.Nullable
    java.lang.String[] getSplitCodePaths();

    /**
     *
     *
     * @see ApplicationInfo#splitDependencies
     */
    @android.annotation.Nullable
    android.util.SparseArray<int[]> getSplitDependencies();

    /**
     *
     *
     * @see ApplicationInfo#splitNames
     * @see PackageInfo#splitNames
     */
    @android.annotation.Nullable
    java.lang.String[] getSplitNames();

    /**
     *
     *
     * @see PackageInfo#splitRevisionCodes
     */
    int[] getSplitRevisionCodes();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_STATIC_SHARED_LIBRARY
     */
    boolean isStaticSharedLibrary();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_SUPPORTS_RTL
     */
    boolean isSupportsRtl();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_TEST_ONLY
     */
    boolean isTestOnly();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_USE_EMBEDDED_DEX
     */
    boolean isUseEmbeddedDex();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_USES_CLEARTEXT_TRAFFIC
     */
    boolean isUsesCleartextTraffic();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_USES_NON_SDK_API
     */
    boolean isUsesNonSdkApi();

    /**
     * Set if the any of components are visible to instant applications.
     *
     * @see R.styleable#AndroidManifestActivity_visibleToInstantApps
     * @see R.styleable#AndroidManifestProvider_visibleToInstantApps
     * @see R.styleable#AndroidManifestService_visibleToInstantApps
     */
    boolean isVisibleToInstantApps();

    /**
     *
     *
     * @see ApplicationInfo#FLAG_VM_SAFE_MODE
     */
    boolean isVmSafeMode();

    /**
     * If omitted from manifest, returns true if {@link #getTargetSdkVersion()} >=
     * {@link android.os.Build.VERSION_CODES#DONUT}.
     *
     * @see R.styleable#AndroidManifestSupportsScreens_anyDensity
     * @see ApplicationInfo#FLAG_SUPPORTS_SCREEN_DENSITIES
     */
    boolean isAnyDensity();

    /**
     *
     *
     * @see ApplicationInfo#appComponentFactory
     * @see R.styleable#AndroidManifestApplication_appComponentFactory
     */
    @android.annotation.Nullable
    java.lang.String getAppComponentFactory();

    /**
     *
     *
     * @see ApplicationInfo#backupAgentName
     * @see R.styleable#AndroidManifestApplication_backupAgent
     */
    @android.annotation.Nullable
    java.lang.String getBackupAgentName();

    /**
     *
     *
     * @see ApplicationInfo#banner
     * @see R.styleable#AndroidManifestApplication_banner
     */
    int getBanner();

    /**
     *
     *
     * @see ApplicationInfo#category
     * @see R.styleable#AndroidManifestApplication_appCategory
     */
    int getCategory();

    /**
     *
     *
     * @see ApplicationInfo#classLoaderName
     * @see R.styleable#AndroidManifestApplication_classLoader
     */
    @android.annotation.Nullable
    java.lang.String getClassLoaderName();

    /**
     *
     *
     * @see ApplicationInfo#className
     * @see R.styleable#AndroidManifestApplication_name
     */
    @android.annotation.Nullable
    java.lang.String getClassName();

    java.lang.String getPackageName();

    /**
     * Path of base APK
     */
    java.lang.String getBaseCodePath();

    /**
     * Path where this package was found on disk. For monolithic packages
     * this is path to single base APK file; for cluster packages this is
     * path to the cluster directory.
     */
    @android.annotation.NonNull
    java.lang.String getCodePath();

    /**
     *
     *
     * @see ApplicationInfo#compatibleWidthLimitDp
     * @see R.styleable#AndroidManifestSupportsScreens_compatibleWidthLimitDp
     */
    int getCompatibleWidthLimitDp();

    /**
     *
     *
     * @see ApplicationInfo#descriptionRes
     * @see R.styleable#AndroidManifestApplication_description
     */
    int getDescriptionRes();

    /**
     *
     *
     * @see ApplicationInfo#enabled
     * @see R.styleable#AndroidManifestApplication_enabled
     */
    boolean isEnabled();

    /**
     *
     *
     * @see ApplicationInfo#fullBackupContent
     * @see R.styleable#AndroidManifestApplication_fullBackupContent
     */
    int getFullBackupContent();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_HAS_DOMAIN_URLS
     */
    boolean isHasDomainUrls();

    /**
     *
     *
     * @see ApplicationInfo#iconRes
     * @see R.styleable#AndroidManifestApplication_icon
     */
    int getIconRes();

    /**
     *
     *
     * @see ApplicationInfo#installLocation
     * @see R.styleable#AndroidManifest_installLocation
     */
    int getInstallLocation();

    /**
     *
     *
     * @see ApplicationInfo#labelRes
     * @see R.styleable#AndroidManifestApplication_label
     */
    int getLabelRes();

    /**
     *
     *
     * @see ApplicationInfo#largestWidthLimitDp
     * @see R.styleable#AndroidManifestSupportsScreens_largestWidthLimitDp
     */
    int getLargestWidthLimitDp();

    /**
     *
     *
     * @see ApplicationInfo#logo
     * @see R.styleable#AndroidManifestApplication_logo
     */
    int getLogo();

    /**
     *
     *
     * @see ApplicationInfo#manageSpaceActivityName
     * @see R.styleable#AndroidManifestApplication_manageSpaceActivity
     */
    @android.annotation.Nullable
    java.lang.String getManageSpaceActivityName();

    /**
     *
     *
     * @see ApplicationInfo#minExtensionVersions
     * @see R.styleable#AndroidManifestExtensionSdk
     */
    @android.annotation.Nullable
    android.util.SparseIntArray getMinExtensionVersions();

    /**
     *
     *
     * @see ApplicationInfo#minSdkVersion
     * @see R.styleable#AndroidManifestUsesSdk_minSdkVersion
     */
    int getMinSdkVersion();

    /**
     *
     *
     * @see ApplicationInfo#networkSecurityConfigRes
     * @see R.styleable#AndroidManifestApplication_networkSecurityConfig
     */
    int getNetworkSecurityConfigRes();

    /**
     * If {@link R.styleable#AndroidManifestApplication_label} is a string literal, this is it.
     * Otherwise, it's stored as {@link #getLabelRes()}.
     *
     * @see ApplicationInfo#nonLocalizedLabel
     * @see R.styleable#AndroidManifestApplication_label
     */
    @android.annotation.Nullable
    java.lang.CharSequence getNonLocalizedLabel();

    /**
     *
     *
     * @see PackageInfo#overlayCategory
     * @see R.styleable#AndroidManifestResourceOverlay_category
     */
    @android.annotation.Nullable
    java.lang.String getOverlayCategory();

    /**
     *
     *
     * @see PackageInfo#mOverlayIsStatic
     */
    boolean isOverlayIsStatic();

    /**
     *
     *
     * @see PackageInfo#overlayPriority
     * @see R.styleable#AndroidManifestResourceOverlay_priority
     */
    int getOverlayPriority();

    /**
     *
     *
     * @see PackageInfo#overlayTarget
     * @see R.styleable#AndroidManifestResourceOverlay_targetPackage
     */
    @android.annotation.Nullable
    java.lang.String getOverlayTarget();

    /**
     *
     *
     * @see PackageInfo#targetOverlayableName
     * @see R.styleable#AndroidManifestResourceOverlay_targetName
     */
    @android.annotation.Nullable
    java.lang.String getOverlayTargetName();

    /**
     * If a system app declares {@link #getOriginalPackages()}, and the app was previously installed
     * under one of those original package names, the {@link #getPackageName()} system identifier
     * will be changed to that previously installed name. This will then be non-null, set to the
     * manifest package name, for tracking the package under its true name.
     *
     * TODO(b/135203078): Remove this in favor of checking originalPackages.isEmpty and
     *  getManifestPackageName
     */
    @android.annotation.Nullable
    java.lang.String getRealPackage();

    /**
     * The required account type without which this application will not function.
     *
     * @see PackageInfo#requiredAccountType
     * @see R.styleable#AndroidManifestApplication_requiredAccountType
     */
    @android.annotation.Nullable
    java.lang.String getRequiredAccountType();

    /**
     *
     *
     * @see PackageInfo#requiredForAllUsers
     * @see R.styleable#AndroidManifestApplication_requiredForAllUsers
     */
    boolean isRequiredForAllUsers();

    /**
     *
     *
     * @see ApplicationInfo#requiresSmallestWidthDp
     * @see R.styleable#AndroidManifestSupportsScreens_requiresSmallestWidthDp
     */
    int getRequiresSmallestWidthDp();

    /**
     * SHA-512 hash of the only APK that can be used to update a system package.
     *
     * @see R.styleable#AndroidManifestRestrictUpdate
     */
    @android.annotation.Nullable
    byte[] getRestrictUpdateHash();

    /**
     * The restricted account authenticator type that is used by this application
     *
     * @see PackageInfo#restrictedAccountType
     * @see R.styleable#AndroidManifestApplication_restrictedAccountType
     */
    @android.annotation.Nullable
    java.lang.String getRestrictedAccountType();

    /**
     *
     *
     * @see ApplicationInfo#roundIconRes
     * @see R.styleable#AndroidManifestApplication_roundIcon
     */
    int getRoundIconRes();

    /**
     *
     *
     * @see PackageInfo#sharedUserLabel
     * @see R.styleable#AndroidManifest_sharedUserLabel
     */
    @java.lang.Deprecated
    int getSharedUserLabel();

    /**
     * The signature data of all APKs in this package, which must be exactly the same across the
     * base and splits.
     */
    android.content.pm.PackageParser.SigningDetails getSigningDetails();

    /**
     *
     *
     * @see ApplicationInfo#splitClassLoaderNames
     * @see R.styleable#AndroidManifestApplication_classLoader
     */
    @android.annotation.Nullable
    java.lang.String[] getSplitClassLoaderNames();

    /**
     *
     *
     * @see R.styleable#AndroidManifestStaticLibrary_version
     */
    long getStaticSharedLibVersion();

    /**
     * If omitted from manifest, returns true if {@link #getTargetSdkVersion()} >=
     * {@link android.os.Build.VERSION_CODES#DONUT}.
     *
     * @see R.styleable#AndroidManifestSupportsScreens_largeScreens
     * @see ApplicationInfo#FLAG_SUPPORTS_LARGE_SCREENS
     */
    boolean isSupportsLargeScreens();

    /**
     * If omitted from manifest, returns true.
     *
     * @see R.styleable#AndroidManifestSupportsScreens_normalScreens
     * @see ApplicationInfo#FLAG_SUPPORTS_NORMAL_SCREENS
     */
    boolean isSupportsNormalScreens();

    /**
     * If omitted from manifest, returns true if {@link #getTargetSdkVersion()} >=
     * {@link android.os.Build.VERSION_CODES#DONUT}.
     *
     * @see R.styleable#AndroidManifestSupportsScreens_smallScreens
     * @see ApplicationInfo#FLAG_SUPPORTS_SMALL_SCREENS
     */
    boolean isSupportsSmallScreens();

    /**
     * If omitted from manifest, returns true if {@link #getTargetSdkVersion()} >=
     * {@link android.os.Build.VERSION_CODES#GINGERBREAD}.
     *
     * @see R.styleable#AndroidManifestSupportsScreens_xlargeScreens
     * @see ApplicationInfo#FLAG_SUPPORTS_XLARGE_SCREENS
     */
    boolean isSupportsExtraLargeScreens();

    /**
     *
     *
     * @see ApplicationInfo#PRIVATE_FLAG_ALLOW_NATIVE_HEAP_POINTER_TAGGING
     */
    boolean isAllowNativeHeapPointerTagging();

    int getAutoRevokePermissions();

    boolean hasPreserveLegacyExternalStorage();

    /**
     *
     *
     * @see ApplicationInfo#targetSandboxVersion
     * @see R.styleable#AndroidManifest_targetSandboxVersion
     */
    @java.lang.Deprecated
    int getTargetSandboxVersion();

    /**
     *
     *
     * @see ApplicationInfo#theme
     * @see R.styleable#AndroidManifestApplication_theme
     */
    int getTheme();

    /**
     * For use with {@link com.android.server.pm.KeySetManagerService}. Parsed in
     * {@link PackageParser#TAG_KEY_SETS}.
     *
     * @see R.styleable#AndroidManifestUpgradeKeySet
     */
    @android.annotation.NonNull
    java.util.Set<java.lang.String> getUpgradeKeySets();

    /**
     * The install time abi override to choose 32bit abi's when multiple abi's
     * are present. This is only meaningfull for multiarch applications.
     * The use32bitAbi attribute is ignored if cpuAbiOverride is also set.
     */
    boolean isUse32BitAbi();

    /**
     *
     *
     * @see ApplicationInfo#volumeUuid
     */
    @android.annotation.Nullable
    java.lang.String getVolumeUuid();

    /**
     *
     *
     * @see ApplicationInfo#zygotePreloadName
     */
    @android.annotation.Nullable
    java.lang.String getZygotePreloadName();

    /**
     * Revision code of base APK
     */
    int getBaseRevisionCode();

    /**
     *
     *
     * @see PackageInfo#versionName
     */
    @android.annotation.Nullable
    java.lang.String getVersionName();

    /**
     *
     *
     * @see PackageInfo#versionCodeMajor
     */
    @android.annotation.Nullable
    int getVersionCode();

    /**
     *
     *
     * @see PackageInfo#versionCodeMajor
     */
    @android.annotation.Nullable
    int getVersionCodeMajor();

    /**
     *
     *
     * @see ApplicationInfo#compileSdkVersion
     * @see R.styleable#AndroidManifest_compileSdkVersion
     */
    int getCompileSdkVersion();

    /**
     *
     *
     * @see ApplicationInfo#compileSdkVersionCodename
     * @see R.styleable#AndroidManifest_compileSdkVersionCodename
     */
    @android.annotation.Nullable
    java.lang.String getCompileSdkVersionCodeName();

    @android.annotation.Nullable
    java.util.Set<java.lang.String> getMimeGroups();

    /**
     *
     *
     * @see ApplicationInfo#gwpAsanMode
     * @see R.styleable#AndroidManifest_gwpAsanMode
     */
    public int getGwpAsanMode();

    // TODO(b/135203078): Hide and enforce going through PackageInfoUtils
    android.content.pm.ApplicationInfo toAppInfoWithoutState();

    /**
     * same as toAppInfoWithoutState except without flag computation.
     */
    android.content.pm.ApplicationInfo toAppInfoWithoutStateWithoutFlags();
}

