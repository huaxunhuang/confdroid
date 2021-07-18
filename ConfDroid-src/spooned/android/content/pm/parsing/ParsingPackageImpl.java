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
 * The backing data for a package that was parsed from disk.
 *
 * The field nullability annotations here are for internal reference. For effective nullability,
 * see the parent interfaces.
 *
 * TODO(b/135203078): Convert Lists used as sets into Sets, to better express intended use case
 *
 * @unknown 
 */
public class ParsingPackageImpl implements android.content.pm.parsing.ParsingPackage , android.os.Parcelable {
    private static final java.lang.String TAG = "PackageImpl";

    public static com.android.internal.util.Parcelling.BuiltIn.ForBoolean sForBoolean = Parcelling.Cache.getOrCreate(com.android.internal.util.Parcelling.BuiltIn.ForBoolean.class);

    public static com.android.internal.util.Parcelling.BuiltIn.ForInternedString sForInternedString = Parcelling.Cache.getOrCreate(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class);

    public static com.android.internal.util.Parcelling.BuiltIn.ForInternedStringArray sForInternedStringArray = Parcelling.Cache.getOrCreate(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringArray.class);

    public static com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList sForInternedStringList = Parcelling.Cache.getOrCreate(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class);

    public static com.android.internal.util.Parcelling.BuiltIn.ForInternedStringValueMap sForInternedStringValueMap = Parcelling.Cache.getOrCreate(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringValueMap.class);

    public static com.android.internal.util.Parcelling.BuiltIn.ForStringSet sForStringSet = Parcelling.Cache.getOrCreate(com.android.internal.util.Parcelling.BuiltIn.ForStringSet.class);

    protected static android.content.pm.parsing.component.ParsedIntentInfo.StringPairListParceler sForIntentInfoPairs = Parcelling.Cache.getOrCreate(android.content.pm.parsing.component.ParsedIntentInfo.StringPairListParceler.class);

    private static final java.util.Comparator<android.content.pm.parsing.component.ParsedMainComponent> ORDER_COMPARATOR = ( first, second) -> java.lang.Integer.compare(second.getOrder(), first.getOrder());

    // These are objects because null represents not explicitly set
    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForBoolean.class)
    private java.lang.Boolean supportsSmallScreens;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForBoolean.class)
    private java.lang.Boolean supportsNormalScreens;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForBoolean.class)
    private java.lang.Boolean supportsLargeScreens;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForBoolean.class)
    private java.lang.Boolean supportsExtraLargeScreens;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForBoolean.class)
    private java.lang.Boolean resizeable;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForBoolean.class)
    private java.lang.Boolean anyDensity;

    protected int versionCode;

    protected int versionCodeMajor;

    private int baseRevisionCode;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String versionName;

    private int compileSdkVersion;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String compileSdkVersionCodeName;

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    protected java.lang.String packageName;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String realPackage;

    @android.annotation.NonNull
    protected java.lang.String baseCodePath;

    private boolean requiredForAllUsers;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String restrictedAccountType;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String requiredAccountType;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String overlayTarget;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String overlayTargetName;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String overlayCategory;

    private int overlayPriority;

    private boolean overlayIsStatic;

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringValueMap.class)
    private java.util.Map<java.lang.String, java.lang.String> overlayables = java.util.Collections.emptyMap();

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String staticSharedLibName;

    private long staticSharedLibVersion;

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    private java.util.List<java.lang.String> libraryNames = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    protected java.util.List<java.lang.String> usesLibraries = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    protected java.util.List<java.lang.String> usesOptionalLibraries = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    private java.util.List<java.lang.String> usesStaticLibraries = java.util.Collections.emptyList();

    @android.annotation.Nullable
    private long[] usesStaticLibrariesVersions;

    @android.annotation.Nullable
    private java.lang.String[][] usesStaticLibrariesCertDigests;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String sharedUserId;

    private int sharedUserLabel;

    @android.annotation.NonNull
    private java.util.List<android.content.pm.ConfigurationInfo> configPreferences = java.util.Collections.emptyList();

    @android.annotation.NonNull
    private java.util.List<android.content.pm.FeatureInfo> reqFeatures = java.util.Collections.emptyList();

    @android.annotation.NonNull
    private java.util.List<android.content.pm.FeatureGroupInfo> featureGroups = java.util.Collections.emptyList();

    @android.annotation.Nullable
    private byte[] restrictUpdateHash;

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    protected java.util.List<java.lang.String> originalPackages = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    protected java.util.List<java.lang.String> adoptPermissions = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    private java.util.List<java.lang.String> requestedPermissions = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    private java.util.List<java.lang.String> implicitPermissions = java.util.Collections.emptyList();

    @android.annotation.NonNull
    private java.util.Set<java.lang.String> upgradeKeySets = java.util.Collections.emptySet();

    @android.annotation.NonNull
    private java.util.Map<java.lang.String, android.util.ArraySet<java.security.PublicKey>> keySetMapping = java.util.Collections.emptyMap();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    protected java.util.List<java.lang.String> protectedBroadcasts = java.util.Collections.emptyList();

    @android.annotation.NonNull
    protected java.util.List<android.content.pm.parsing.component.ParsedActivity> activities = java.util.Collections.emptyList();

    @android.annotation.NonNull
    protected java.util.List<android.content.pm.parsing.component.ParsedActivity> receivers = java.util.Collections.emptyList();

    @android.annotation.NonNull
    protected java.util.List<android.content.pm.parsing.component.ParsedService> services = java.util.Collections.emptyList();

    @android.annotation.NonNull
    protected java.util.List<android.content.pm.parsing.component.ParsedProvider> providers = java.util.Collections.emptyList();

    @android.annotation.NonNull
    private java.util.List<android.content.pm.parsing.component.ParsedAttribution> attributions = java.util.Collections.emptyList();

    @android.annotation.NonNull
    protected java.util.List<android.content.pm.parsing.component.ParsedPermission> permissions = java.util.Collections.emptyList();

    @android.annotation.NonNull
    protected java.util.List<android.content.pm.parsing.component.ParsedPermissionGroup> permissionGroups = java.util.Collections.emptyList();

    @android.annotation.NonNull
    protected java.util.List<android.content.pm.parsing.component.ParsedInstrumentation> instrumentations = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(android.content.pm.parsing.component.ParsedIntentInfo.ListParceler.class)
    private java.util.List<android.util.Pair<java.lang.String, android.content.pm.parsing.component.ParsedIntentInfo>> preferredActivityFilters = java.util.Collections.emptyList();

    @android.annotation.NonNull
    private java.util.Map<java.lang.String, android.content.pm.parsing.component.ParsedProcess> processes = java.util.Collections.emptyMap();

    @android.annotation.Nullable
    private android.os.Bundle metaData;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    protected java.lang.String volumeUuid;

    @android.annotation.Nullable
    private android.content.pm.PackageParser.SigningDetails signingDetails;

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    protected java.lang.String codePath;

    private boolean use32BitAbi;

    private boolean visibleToInstantApps;

    private boolean forceQueryable;

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    private java.util.List<android.content.Intent> queriesIntents = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringList.class)
    private java.util.List<java.lang.String> queriesPackages = java.util.Collections.emptyList();

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringSet.class)
    private java.util.Set<java.lang.String> queriesProviders = java.util.Collections.emptySet();

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringArray.class)
    private java.lang.String[] splitClassLoaderNames;

    @android.annotation.Nullable
    protected java.lang.String[] splitCodePaths;

    @android.annotation.Nullable
    private android.util.SparseArray<int[]> splitDependencies;

    @android.annotation.Nullable
    private int[] splitFlags;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedStringArray.class)
    private java.lang.String[] splitNames;

    @android.annotation.Nullable
    private int[] splitRevisionCodes;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String appComponentFactory;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String backupAgentName;

    private int banner;

    private int category;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String classLoaderName;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String className;

    private int compatibleWidthLimitDp;

    private int descriptionRes;

    // Usually there's code to set this to true during parsing, but it's possible to install an APK
    // targeting <R that doesn't contain an <application> tag. That code would be skipped and never
    // assign this, so initialize this to true for those cases.
    private boolean enabled = true;

    private boolean crossProfile;

    private int fullBackupContent;

    private int iconRes;

    private int installLocation = android.content.pm.PackageParser.PARSE_DEFAULT_INSTALL_LOCATION;

    private int labelRes;

    private int largestWidthLimitDp;

    private int logo;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String manageSpaceActivityName;

    private float maxAspectRatio;

    private float minAspectRatio;

    @android.annotation.Nullable
    private android.util.SparseIntArray minExtensionVersions;

    private int minSdkVersion;

    private int networkSecurityConfigRes;

    @android.annotation.Nullable
    private java.lang.CharSequence nonLocalizedLabel;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String permission;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String processName;

    private int requiresSmallestWidthDp;

    private int roundIconRes;

    private int targetSandboxVersion;

    private int targetSdkVersion;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String taskAffinity;

    private int theme;

    private int uiOptions;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String zygotePreloadName;

    private boolean externalStorage;

    private boolean baseHardwareAccelerated;

    private boolean allowBackup;

    private boolean killAfterRestore;

    private boolean restoreAnyVersion;

    private boolean fullBackupOnly;

    private boolean persistent;

    private boolean debuggable;

    private boolean vmSafeMode;

    private boolean hasCode;

    private boolean allowTaskReparenting;

    private boolean allowClearUserData;

    private boolean largeHeap;

    private boolean usesCleartextTraffic;

    private boolean supportsRtl;

    private boolean testOnly;

    private boolean multiArch;

    private boolean extractNativeLibs;

    private boolean game;

    /**
     *
     *
     * @see ParsingPackageRead#getResizeableActivity()
     */
    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForBoolean.class)
    private java.lang.Boolean resizeableActivity;

    private boolean staticSharedLibrary;

    private boolean overlay;

    private boolean isolatedSplitLoading;

    private boolean hasDomainUrls;

    private boolean profileableByShell;

    private boolean backupInForeground;

    private boolean useEmbeddedDex;

    private boolean defaultToDeviceProtectedStorage;

    private boolean directBootAware;

    private boolean partiallyDirectBootAware;

    private boolean resizeableActivityViaSdkVersion;

    private boolean allowClearUserDataOnFailedRestore;

    private boolean allowAudioPlaybackCapture;

    private boolean requestLegacyExternalStorage;

    private boolean usesNonSdkApi;

    private boolean hasFragileUserData;

    private boolean cantSaveState;

    private boolean allowNativeHeapPointerTagging;

    private int autoRevokePermissions;

    private boolean preserveLegacyExternalStorage;

    protected int gwpAsanMode;

    // TODO(chiuwinson): Non-null
    @android.annotation.Nullable
    private android.util.ArraySet<java.lang.String> mimeGroups;

    @com.android.internal.annotations.VisibleForTesting
    public ParsingPackageImpl(@android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    java.lang.String baseCodePath, @android.annotation.NonNull
    java.lang.String codePath, @android.annotation.Nullable
    android.content.res.TypedArray manifestArray) {
        this.packageName = android.text.TextUtils.safeIntern(packageName);
        this.baseCodePath = baseCodePath;
        this.codePath = codePath;
        if (manifestArray != null) {
            versionCode = manifestArray.getInteger(R.styleable.AndroidManifest_versionCode, 0);
            versionCodeMajor = manifestArray.getInteger(R.styleable.AndroidManifest_versionCodeMajor, 0);
            setBaseRevisionCode(manifestArray.getInteger(R.styleable.AndroidManifest_revisionCode, 0));
            setVersionName(manifestArray.getNonConfigurationString(R.styleable.AndroidManifest_versionName, 0));
            setCompileSdkVersion(manifestArray.getInteger(R.styleable.AndroidManifest_compileSdkVersion, 0));
            setCompileSdkVersionCodename(manifestArray.getNonConfigurationString(R.styleable.AndroidManifest_compileSdkVersionCodename, 0));
            setIsolatedSplitLoading(manifestArray.getBoolean(R.styleable.AndroidManifest_isolatedSplits, false));
        }
    }

    public boolean isSupportsSmallScreens() {
        if (supportsSmallScreens == null) {
            return targetSdkVersion >= Build.VERSION_CODES.DONUT;
        }
        return supportsSmallScreens;
    }

    public boolean isSupportsNormalScreens() {
        return (supportsNormalScreens == null) || supportsNormalScreens;
    }

    public boolean isSupportsLargeScreens() {
        if (supportsLargeScreens == null) {
            return targetSdkVersion >= Build.VERSION_CODES.DONUT;
        }
        return supportsLargeScreens;
    }

    public boolean isSupportsExtraLargeScreens() {
        if (supportsExtraLargeScreens == null) {
            return targetSdkVersion >= Build.VERSION_CODES.GINGERBREAD;
        }
        return supportsExtraLargeScreens;
    }

    public boolean isResizeable() {
        if (resizeable == null) {
            return targetSdkVersion >= Build.VERSION_CODES.DONUT;
        }
        return resizeable;
    }

    public boolean isAnyDensity() {
        if (anyDensity == null) {
            return targetSdkVersion >= Build.VERSION_CODES.DONUT;
        }
        return anyDensity;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl sortActivities() {
        java.util.Collections.sort(this.activities, android.content.pm.parsing.ParsingPackageImpl.ORDER_COMPARATOR);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl sortReceivers() {
        java.util.Collections.sort(this.receivers, android.content.pm.parsing.ParsingPackageImpl.ORDER_COMPARATOR);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl sortServices() {
        java.util.Collections.sort(this.services, android.content.pm.parsing.ParsingPackageImpl.ORDER_COMPARATOR);
        return this;
    }

    @java.lang.Override
    public java.lang.Object hideAsParsed() {
        // There is no equivalent for core-only parsing
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addConfigPreference(android.content.pm.ConfigurationInfo configPreference) {
        this.configPreferences = com.android.internal.util.CollectionUtils.add(this.configPreferences, configPreference);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addReqFeature(android.content.pm.FeatureInfo reqFeature) {
        this.reqFeatures = com.android.internal.util.CollectionUtils.add(this.reqFeatures, reqFeature);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addFeatureGroup(android.content.pm.FeatureGroupInfo featureGroup) {
        this.featureGroups = com.android.internal.util.CollectionUtils.add(this.featureGroups, featureGroup);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addProtectedBroadcast(java.lang.String protectedBroadcast) {
        if (!this.protectedBroadcasts.contains(protectedBroadcast)) {
            this.protectedBroadcasts = com.android.internal.util.CollectionUtils.add(this.protectedBroadcasts, android.text.TextUtils.safeIntern(protectedBroadcast));
        }
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addInstrumentation(android.content.pm.parsing.component.ParsedInstrumentation instrumentation) {
        this.instrumentations = com.android.internal.util.CollectionUtils.add(this.instrumentations, instrumentation);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addOriginalPackage(java.lang.String originalPackage) {
        this.originalPackages = com.android.internal.util.CollectionUtils.add(this.originalPackages, originalPackage);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackage addOverlayable(java.lang.String overlayableName, java.lang.String actorName) {
        this.overlayables = com.android.internal.util.CollectionUtils.add(this.overlayables, overlayableName, android.text.TextUtils.safeIntern(actorName));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addAdoptPermission(java.lang.String adoptPermission) {
        this.adoptPermissions = com.android.internal.util.CollectionUtils.add(this.adoptPermissions, android.text.TextUtils.safeIntern(adoptPermission));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addPermission(android.content.pm.parsing.component.ParsedPermission permission) {
        this.permissions = com.android.internal.util.CollectionUtils.add(this.permissions, permission);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addPermissionGroup(android.content.pm.parsing.component.ParsedPermissionGroup permissionGroup) {
        this.permissionGroups = com.android.internal.util.CollectionUtils.add(this.permissionGroups, permissionGroup);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addRequestedPermission(java.lang.String permission) {
        this.requestedPermissions = com.android.internal.util.CollectionUtils.add(this.requestedPermissions, android.text.TextUtils.safeIntern(permission));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addImplicitPermission(java.lang.String permission) {
        this.implicitPermissions = com.android.internal.util.CollectionUtils.add(this.implicitPermissions, android.text.TextUtils.safeIntern(permission));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addKeySet(java.lang.String keySetName, java.security.PublicKey publicKey) {
        android.util.ArraySet<java.security.PublicKey> publicKeys = keySetMapping.get(keySetName);
        if (publicKeys == null) {
            publicKeys = new android.util.ArraySet();
        }
        publicKeys.add(publicKey);
        keySetMapping = com.android.internal.util.CollectionUtils.add(this.keySetMapping, keySetName, publicKeys);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addActivity(android.content.pm.parsing.component.ParsedActivity parsedActivity) {
        this.activities = com.android.internal.util.CollectionUtils.add(this.activities, parsedActivity);
        addMimeGroupsFromComponent(parsedActivity);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addReceiver(android.content.pm.parsing.component.ParsedActivity parsedReceiver) {
        this.receivers = com.android.internal.util.CollectionUtils.add(this.receivers, parsedReceiver);
        addMimeGroupsFromComponent(parsedReceiver);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addService(android.content.pm.parsing.component.ParsedService parsedService) {
        this.services = com.android.internal.util.CollectionUtils.add(this.services, parsedService);
        addMimeGroupsFromComponent(parsedService);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addProvider(android.content.pm.parsing.component.ParsedProvider parsedProvider) {
        this.providers = com.android.internal.util.CollectionUtils.add(this.providers, parsedProvider);
        addMimeGroupsFromComponent(parsedProvider);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addAttribution(android.content.pm.parsing.component.ParsedAttribution attribution) {
        this.attributions = com.android.internal.util.CollectionUtils.add(this.attributions, attribution);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addLibraryName(java.lang.String libraryName) {
        this.libraryNames = com.android.internal.util.CollectionUtils.add(this.libraryNames, android.text.TextUtils.safeIntern(libraryName));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addUsesOptionalLibrary(java.lang.String libraryName) {
        this.usesOptionalLibraries = com.android.internal.util.CollectionUtils.add(this.usesOptionalLibraries, android.text.TextUtils.safeIntern(libraryName));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addUsesLibrary(java.lang.String libraryName) {
        this.usesLibraries = com.android.internal.util.CollectionUtils.add(this.usesLibraries, android.text.TextUtils.safeIntern(libraryName));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl removeUsesOptionalLibrary(java.lang.String libraryName) {
        this.usesOptionalLibraries = com.android.internal.util.CollectionUtils.remove(this.usesOptionalLibraries, libraryName);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addUsesStaticLibrary(java.lang.String libraryName) {
        this.usesStaticLibraries = com.android.internal.util.CollectionUtils.add(this.usesStaticLibraries, android.text.TextUtils.safeIntern(libraryName));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addUsesStaticLibraryVersion(long version) {
        this.usesStaticLibrariesVersions = com.android.internal.util.ArrayUtils.appendLong(this.usesStaticLibrariesVersions, version, true);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addUsesStaticLibraryCertDigests(java.lang.String[] certSha256Digests) {
        this.usesStaticLibrariesCertDigests = com.android.internal.util.ArrayUtils.appendElement(java.lang.String[].class, this.usesStaticLibrariesCertDigests, certSha256Digests, true);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addPreferredActivityFilter(java.lang.String className, android.content.pm.parsing.component.ParsedIntentInfo intentInfo) {
        this.preferredActivityFilters = com.android.internal.util.CollectionUtils.add(this.preferredActivityFilters, android.util.Pair.create(className, intentInfo));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addQueriesIntent(android.content.Intent intent) {
        this.queriesIntents = com.android.internal.util.CollectionUtils.add(this.queriesIntents, intent);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addQueriesPackage(java.lang.String packageName) {
        this.queriesPackages = com.android.internal.util.CollectionUtils.add(this.queriesPackages, android.text.TextUtils.safeIntern(packageName));
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl addQueriesProvider(java.lang.String authority) {
        this.queriesProviders = com.android.internal.util.CollectionUtils.add(this.queriesProviders, authority);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSupportsSmallScreens(int supportsSmallScreens) {
        if (supportsSmallScreens == 1) {
            return this;
        }
        this.supportsSmallScreens = supportsSmallScreens < 0;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSupportsNormalScreens(int supportsNormalScreens) {
        if (supportsNormalScreens == 1) {
            return this;
        }
        this.supportsNormalScreens = supportsNormalScreens < 0;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSupportsLargeScreens(int supportsLargeScreens) {
        if (supportsLargeScreens == 1) {
            return this;
        }
        this.supportsLargeScreens = supportsLargeScreens < 0;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSupportsExtraLargeScreens(int supportsExtraLargeScreens) {
        if (supportsExtraLargeScreens == 1) {
            return this;
        }
        this.supportsExtraLargeScreens = supportsExtraLargeScreens < 0;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setResizeable(int resizeable) {
        if (resizeable == 1) {
            return this;
        }
        this.resizeable = resizeable < 0;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAnyDensity(int anyDensity) {
        if (anyDensity == 1) {
            return this;
        }
        this.anyDensity = anyDensity < 0;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl asSplit(java.lang.String[] splitNames, java.lang.String[] splitCodePaths, int[] splitRevisionCodes, android.util.SparseArray<int[]> splitDependencies) {
        this.splitNames = splitNames;
        this.splitCodePaths = splitCodePaths;
        this.splitRevisionCodes = splitRevisionCodes;
        this.splitDependencies = splitDependencies;
        int count = splitNames.length;
        this.splitFlags = new int[count];
        this.splitClassLoaderNames = new java.lang.String[count];
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSplitHasCode(int splitIndex, boolean splitHasCode) {
        this.splitFlags[splitIndex] = (splitHasCode) ? this.splitFlags[splitIndex] | android.content.pm.ApplicationInfo.FLAG_HAS_CODE : this.splitFlags[splitIndex] & (~android.content.pm.ApplicationInfo.FLAG_HAS_CODE);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSplitClassLoaderName(int splitIndex, java.lang.String classLoaderName) {
        this.splitClassLoaderNames[splitIndex] = classLoaderName;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRequiredAccountType(@android.annotation.Nullable
    java.lang.String requiredAccountType) {
        this.requiredAccountType = android.text.TextUtils.nullIfEmpty(requiredAccountType);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setOverlayTarget(@android.annotation.Nullable
    java.lang.String overlayTarget) {
        this.overlayTarget = android.text.TextUtils.safeIntern(overlayTarget);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setVolumeUuid(@android.annotation.Nullable
    java.lang.String volumeUuid) {
        this.volumeUuid = android.text.TextUtils.safeIntern(volumeUuid);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setStaticSharedLibName(java.lang.String staticSharedLibName) {
        this.staticSharedLibName = android.text.TextUtils.safeIntern(staticSharedLibName);
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSharedUserId(java.lang.String sharedUserId) {
        this.sharedUserId = android.text.TextUtils.safeIntern(sharedUserId);
        return this;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.lang.String getProcessName() {
        return processName != null ? processName : packageName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("Package{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + packageName) + "}";
    }

    @java.lang.Deprecated
    @java.lang.Override
    public android.content.pm.ApplicationInfo toAppInfoWithoutState() {
        android.content.pm.ApplicationInfo appInfo = toAppInfoWithoutStateWithoutFlags();
        appInfo.flags = android.content.pm.parsing.PackageInfoWithoutStateUtils.appInfoFlags(this);
        appInfo.privateFlags = android.content.pm.parsing.PackageInfoWithoutStateUtils.appInfoPrivateFlags(this);
        return appInfo;
    }

    @java.lang.Override
    public android.content.pm.ApplicationInfo toAppInfoWithoutStateWithoutFlags() {
        android.content.pm.ApplicationInfo appInfo = new android.content.pm.ApplicationInfo();
        appInfo.appComponentFactory = appComponentFactory;
        appInfo.backupAgentName = backupAgentName;
        appInfo.banner = banner;
        appInfo.category = category;
        appInfo.classLoaderName = classLoaderName;
        appInfo.className = className;
        appInfo.compatibleWidthLimitDp = compatibleWidthLimitDp;
        appInfo.compileSdkVersion = compileSdkVersion;
        appInfo.compileSdkVersionCodename = compileSdkVersionCodeName;
        // appInfo.credentialProtectedDataDir = credentialProtectedDataDir;
        // appInfo.dataDir = dataDir;
        appInfo.descriptionRes = descriptionRes;
        // appInfo.deviceProtectedDataDir = deviceProtectedDataDir;
        appInfo.enabled = enabled;
        appInfo.fullBackupContent = fullBackupContent;
        // appInfo.hiddenUntilInstalled = hiddenUntilInstalled;
        appInfo.icon = (android.content.pm.PackageParser.sUseRoundIcon && (roundIconRes != 0)) ? roundIconRes : iconRes;
        appInfo.iconRes = iconRes;
        appInfo.roundIconRes = roundIconRes;
        appInfo.installLocation = installLocation;
        appInfo.labelRes = labelRes;
        appInfo.largestWidthLimitDp = largestWidthLimitDp;
        appInfo.logo = logo;
        appInfo.manageSpaceActivityName = manageSpaceActivityName;
        appInfo.maxAspectRatio = maxAspectRatio;
        appInfo.metaData = metaData;
        appInfo.minAspectRatio = minAspectRatio;
        appInfo.minSdkVersion = minSdkVersion;
        appInfo.name = className;
        if (appInfo.name != null) {
            appInfo.name = appInfo.name.trim();
        }
        // appInfo.nativeLibraryDir = nativeLibraryDir;
        // appInfo.nativeLibraryRootDir = nativeLibraryRootDir;
        // appInfo.nativeLibraryRootRequiresIsa = nativeLibraryRootRequiresIsa;
        appInfo.networkSecurityConfigRes = networkSecurityConfigRes;
        appInfo.nonLocalizedLabel = nonLocalizedLabel;
        if (appInfo.nonLocalizedLabel != null) {
            appInfo.nonLocalizedLabel = appInfo.nonLocalizedLabel.toString().trim();
        }
        appInfo.packageName = packageName;
        appInfo.permission = permission;
        // appInfo.primaryCpuAbi = primaryCpuAbi;
        appInfo.processName = getProcessName();
        appInfo.requiresSmallestWidthDp = requiresSmallestWidthDp;
        // appInfo.secondaryCpuAbi = secondaryCpuAbi;
        // appInfo.secondaryNativeLibraryDir = secondaryNativeLibraryDir;
        // appInfo.seInfo = seInfo;
        // appInfo.seInfoUser = seInfoUser;
        // appInfo.sharedLibraryFiles = usesLibraryFiles.isEmpty()
        // ? null : usesLibraryFiles.toArray(new String[0]);
        // appInfo.sharedLibraryInfos = usesLibraryInfos.isEmpty() ? null : usesLibraryInfos;
        appInfo.splitClassLoaderNames = splitClassLoaderNames;
        appInfo.splitDependencies = splitDependencies;
        appInfo.splitNames = splitNames;
        appInfo.storageUuid = android.os.storage.StorageManager.convert(volumeUuid);
        appInfo.targetSandboxVersion = targetSandboxVersion;
        appInfo.targetSdkVersion = targetSdkVersion;
        appInfo.taskAffinity = taskAffinity;
        appInfo.theme = theme;
        // appInfo.uid = uid;
        appInfo.uiOptions = uiOptions;
        appInfo.volumeUuid = volumeUuid;
        appInfo.zygotePreloadName = zygotePreloadName;
        appInfo.crossProfile = isCrossProfile();
        appInfo.setGwpAsanMode(gwpAsanMode);
        appInfo.setBaseCodePath(baseCodePath);
        appInfo.setBaseResourcePath(baseCodePath);
        appInfo.setCodePath(codePath);
        appInfo.setResourcePath(codePath);
        appInfo.setSplitCodePaths(splitCodePaths);
        appInfo.setSplitResourcePaths(splitCodePaths);
        appInfo.setVersionCode(android.content.pm.PackageInfo.composeLongVersionCode(versionCodeMajor, versionCode));
        // TODO(b/135203078): Can this be removed? Looks only used in ActivityInfo.
        // appInfo.showUserIcon = pkg.getShowUserIcon();
        // TODO(b/135203078): Unused?
        // appInfo.resourceDirs = pkg.getResourceDirs();
        // TODO(b/135203078): Unused?
        // appInfo.enabledSetting = pkg.getEnabledSetting();
        // TODO(b/135203078): See ParsingPackageImpl#getHiddenApiEnforcementPolicy
        // appInfo.mHiddenApiPolicy = pkg.getHiddenApiPolicy();
        return appInfo;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        android.content.pm.parsing.ParsingPackageImpl.sForBoolean.parcel(this.supportsSmallScreens, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForBoolean.parcel(this.supportsNormalScreens, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForBoolean.parcel(this.supportsLargeScreens, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForBoolean.parcel(this.supportsExtraLargeScreens, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForBoolean.parcel(this.resizeable, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForBoolean.parcel(this.anyDensity, dest, flags);
        dest.writeInt(this.versionCode);
        dest.writeInt(this.versionCodeMajor);
        dest.writeInt(this.baseRevisionCode);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.versionName, dest, flags);
        dest.writeInt(this.compileSdkVersion);
        dest.writeString(this.compileSdkVersionCodeName);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.packageName, dest, flags);
        dest.writeString(this.realPackage);
        dest.writeString(this.baseCodePath);
        dest.writeBoolean(this.requiredForAllUsers);
        dest.writeString(this.restrictedAccountType);
        dest.writeString(this.requiredAccountType);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.overlayTarget, dest, flags);
        dest.writeString(this.overlayTargetName);
        dest.writeString(this.overlayCategory);
        dest.writeInt(this.overlayPriority);
        dest.writeBoolean(this.overlayIsStatic);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringValueMap.parcel(this.overlayables, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.staticSharedLibName, dest, flags);
        dest.writeLong(this.staticSharedLibVersion);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.libraryNames, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.usesLibraries, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.usesOptionalLibraries, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.usesStaticLibraries, dest, flags);
        dest.writeLongArray(this.usesStaticLibrariesVersions);
        if (this.usesStaticLibrariesCertDigests == null) {
            dest.writeInt(-1);
        } else {
            dest.writeInt(this.usesStaticLibrariesCertDigests.length);
            for (int index = 0; index < this.usesStaticLibrariesCertDigests.length; index++) {
                dest.writeStringArray(this.usesStaticLibrariesCertDigests[index]);
            }
        }
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.sharedUserId, dest, flags);
        dest.writeInt(this.sharedUserLabel);
        dest.writeTypedList(this.configPreferences);
        dest.writeTypedList(this.reqFeatures);
        dest.writeTypedList(this.featureGroups);
        dest.writeByteArray(this.restrictUpdateHash);
        dest.writeStringList(this.originalPackages);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.adoptPermissions, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.requestedPermissions, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.implicitPermissions, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForStringSet.parcel(this.upgradeKeySets, dest, flags);
        dest.writeMap(this.keySetMapping);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.protectedBroadcasts, dest, flags);
        dest.writeTypedList(this.activities);
        dest.writeTypedList(this.receivers);
        dest.writeTypedList(this.services);
        dest.writeTypedList(this.providers);
        dest.writeTypedList(this.attributions);
        dest.writeTypedList(this.permissions);
        dest.writeTypedList(this.permissionGroups);
        dest.writeTypedList(this.instrumentations);
        android.content.pm.parsing.ParsingPackageImpl.sForIntentInfoPairs.parcel(this.preferredActivityFilters, dest, flags);
        dest.writeMap(this.processes);
        dest.writeBundle(this.metaData);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.volumeUuid, dest, flags);
        dest.writeParcelable(this.signingDetails, flags);
        dest.writeString(this.codePath);
        dest.writeBoolean(this.use32BitAbi);
        dest.writeBoolean(this.visibleToInstantApps);
        dest.writeBoolean(this.forceQueryable);
        dest.writeParcelableList(this.queriesIntents, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.parcel(this.queriesPackages, dest, flags);
        dest.writeString(this.appComponentFactory);
        dest.writeString(this.backupAgentName);
        dest.writeInt(this.banner);
        dest.writeInt(this.category);
        dest.writeString(this.classLoaderName);
        dest.writeString(this.className);
        dest.writeInt(this.compatibleWidthLimitDp);
        dest.writeInt(this.descriptionRes);
        dest.writeBoolean(this.enabled);
        dest.writeBoolean(this.crossProfile);
        dest.writeInt(this.fullBackupContent);
        dest.writeInt(this.iconRes);
        dest.writeInt(this.installLocation);
        dest.writeInt(this.labelRes);
        dest.writeInt(this.largestWidthLimitDp);
        dest.writeInt(this.logo);
        dest.writeString(this.manageSpaceActivityName);
        dest.writeFloat(this.maxAspectRatio);
        dest.writeFloat(this.minAspectRatio);
        dest.writeInt(this.minSdkVersion);
        dest.writeInt(this.networkSecurityConfigRes);
        dest.writeCharSequence(this.nonLocalizedLabel);
        dest.writeString(this.permission);
        dest.writeString(this.processName);
        dest.writeInt(this.requiresSmallestWidthDp);
        dest.writeInt(this.roundIconRes);
        dest.writeInt(this.targetSandboxVersion);
        dest.writeInt(this.targetSdkVersion);
        dest.writeString(this.taskAffinity);
        dest.writeInt(this.theme);
        dest.writeInt(this.uiOptions);
        dest.writeString(this.zygotePreloadName);
        dest.writeStringArray(this.splitClassLoaderNames);
        dest.writeStringArray(this.splitCodePaths);
        dest.writeSparseArray(this.splitDependencies);
        dest.writeIntArray(this.splitFlags);
        dest.writeStringArray(this.splitNames);
        dest.writeIntArray(this.splitRevisionCodes);
        dest.writeBoolean(this.externalStorage);
        dest.writeBoolean(this.baseHardwareAccelerated);
        dest.writeBoolean(this.allowBackup);
        dest.writeBoolean(this.killAfterRestore);
        dest.writeBoolean(this.restoreAnyVersion);
        dest.writeBoolean(this.fullBackupOnly);
        dest.writeBoolean(this.persistent);
        dest.writeBoolean(this.debuggable);
        dest.writeBoolean(this.vmSafeMode);
        dest.writeBoolean(this.hasCode);
        dest.writeBoolean(this.allowTaskReparenting);
        dest.writeBoolean(this.allowClearUserData);
        dest.writeBoolean(this.largeHeap);
        dest.writeBoolean(this.usesCleartextTraffic);
        dest.writeBoolean(this.supportsRtl);
        dest.writeBoolean(this.testOnly);
        dest.writeBoolean(this.multiArch);
        dest.writeBoolean(this.extractNativeLibs);
        dest.writeBoolean(this.game);
        android.content.pm.parsing.ParsingPackageImpl.sForBoolean.parcel(this.resizeableActivity, dest, flags);
        dest.writeBoolean(this.staticSharedLibrary);
        dest.writeBoolean(this.overlay);
        dest.writeBoolean(this.isolatedSplitLoading);
        dest.writeBoolean(this.hasDomainUrls);
        dest.writeBoolean(this.profileableByShell);
        dest.writeBoolean(this.backupInForeground);
        dest.writeBoolean(this.useEmbeddedDex);
        dest.writeBoolean(this.defaultToDeviceProtectedStorage);
        dest.writeBoolean(this.directBootAware);
        dest.writeBoolean(this.partiallyDirectBootAware);
        dest.writeBoolean(this.resizeableActivityViaSdkVersion);
        dest.writeBoolean(this.allowClearUserDataOnFailedRestore);
        dest.writeBoolean(this.allowAudioPlaybackCapture);
        dest.writeBoolean(this.requestLegacyExternalStorage);
        dest.writeBoolean(this.usesNonSdkApi);
        dest.writeBoolean(this.hasFragileUserData);
        dest.writeBoolean(this.cantSaveState);
        dest.writeBoolean(this.allowNativeHeapPointerTagging);
        dest.writeInt(this.autoRevokePermissions);
        dest.writeBoolean(this.preserveLegacyExternalStorage);
        dest.writeArraySet(this.mimeGroups);
        dest.writeInt(this.gwpAsanMode);
        dest.writeSparseIntArray(this.minExtensionVersions);
    }

    public ParsingPackageImpl(android.os.Parcel in) {
        // We use the boot classloader for all classes that we load.
        final java.lang.ClassLoader boot = java.lang.Object.class.getClassLoader();
        this.supportsSmallScreens = android.content.pm.parsing.ParsingPackageImpl.sForBoolean.unparcel(in);
        this.supportsNormalScreens = android.content.pm.parsing.ParsingPackageImpl.sForBoolean.unparcel(in);
        this.supportsLargeScreens = android.content.pm.parsing.ParsingPackageImpl.sForBoolean.unparcel(in);
        this.supportsExtraLargeScreens = android.content.pm.parsing.ParsingPackageImpl.sForBoolean.unparcel(in);
        this.resizeable = android.content.pm.parsing.ParsingPackageImpl.sForBoolean.unparcel(in);
        this.anyDensity = android.content.pm.parsing.ParsingPackageImpl.sForBoolean.unparcel(in);
        this.versionCode = in.readInt();
        this.versionCodeMajor = in.readInt();
        this.baseRevisionCode = in.readInt();
        this.versionName = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.compileSdkVersion = in.readInt();
        this.compileSdkVersionCodeName = in.readString();
        this.packageName = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.realPackage = in.readString();
        this.baseCodePath = in.readString();
        this.requiredForAllUsers = in.readBoolean();
        this.restrictedAccountType = in.readString();
        this.requiredAccountType = in.readString();
        this.overlayTarget = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.overlayTargetName = in.readString();
        this.overlayCategory = in.readString();
        this.overlayPriority = in.readInt();
        this.overlayIsStatic = in.readBoolean();
        this.overlayables = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringValueMap.unparcel(in);
        this.staticSharedLibName = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.staticSharedLibVersion = in.readLong();
        this.libraryNames = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.usesLibraries = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.usesOptionalLibraries = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.usesStaticLibraries = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.usesStaticLibrariesVersions = in.createLongArray();
        int digestsSize = in.readInt();
        if (digestsSize >= 0) {
            this.usesStaticLibrariesCertDigests = new java.lang.String[digestsSize][];
            for (int index = 0; index < digestsSize; index++) {
                this.usesStaticLibrariesCertDigests[index] = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringArray.unparcel(in);
            }
        }
        this.sharedUserId = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.sharedUserLabel = in.readInt();
        this.configPreferences = in.createTypedArrayList(android.content.pm.ConfigurationInfo.CREATOR);
        this.reqFeatures = in.createTypedArrayList(android.content.pm.FeatureInfo.CREATOR);
        this.featureGroups = in.createTypedArrayList(android.content.pm.FeatureGroupInfo.CREATOR);
        this.restrictUpdateHash = in.createByteArray();
        this.originalPackages = in.createStringArrayList();
        this.adoptPermissions = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.requestedPermissions = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.implicitPermissions = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.upgradeKeySets = android.content.pm.parsing.ParsingPackageImpl.sForStringSet.unparcel(in);
        this.keySetMapping = in.readHashMap(boot);
        this.protectedBroadcasts = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.activities = in.createTypedArrayList(this.CREATOR);
        this.receivers = in.createTypedArrayList(this.CREATOR);
        this.services = in.createTypedArrayList(this.CREATOR);
        this.providers = in.createTypedArrayList(this.CREATOR);
        this.attributions = in.createTypedArrayList(this.CREATOR);
        this.permissions = in.createTypedArrayList(this.CREATOR);
        this.permissionGroups = in.createTypedArrayList(this.CREATOR);
        this.instrumentations = in.createTypedArrayList(this.CREATOR);
        this.preferredActivityFilters = android.content.pm.parsing.ParsingPackageImpl.sForIntentInfoPairs.unparcel(in);
        this.processes = in.readHashMap(boot);
        this.metaData = in.readBundle(boot);
        this.volumeUuid = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.signingDetails = in.readParcelable(boot);
        this.codePath = in.readString();
        this.use32BitAbi = in.readBoolean();
        this.visibleToInstantApps = in.readBoolean();
        this.forceQueryable = in.readBoolean();
        this.queriesIntents = in.createTypedArrayList(this.CREATOR);
        this.queriesPackages = android.content.pm.parsing.ParsingPackageImpl.sForInternedStringList.unparcel(in);
        this.appComponentFactory = in.readString();
        this.backupAgentName = in.readString();
        this.banner = in.readInt();
        this.category = in.readInt();
        this.classLoaderName = in.readString();
        this.className = in.readString();
        this.compatibleWidthLimitDp = in.readInt();
        this.descriptionRes = in.readInt();
        this.enabled = in.readBoolean();
        this.crossProfile = in.readBoolean();
        this.fullBackupContent = in.readInt();
        this.iconRes = in.readInt();
        this.installLocation = in.readInt();
        this.labelRes = in.readInt();
        this.largestWidthLimitDp = in.readInt();
        this.logo = in.readInt();
        this.manageSpaceActivityName = in.readString();
        this.maxAspectRatio = in.readFloat();
        this.minAspectRatio = in.readFloat();
        this.minSdkVersion = in.readInt();
        this.networkSecurityConfigRes = in.readInt();
        this.nonLocalizedLabel = in.readCharSequence();
        this.permission = in.readString();
        this.processName = in.readString();
        this.requiresSmallestWidthDp = in.readInt();
        this.roundIconRes = in.readInt();
        this.targetSandboxVersion = in.readInt();
        this.targetSdkVersion = in.readInt();
        this.taskAffinity = in.readString();
        this.theme = in.readInt();
        this.uiOptions = in.readInt();
        this.zygotePreloadName = in.readString();
        this.splitClassLoaderNames = in.createStringArray();
        this.splitCodePaths = in.createStringArray();
        this.splitDependencies = in.readSparseArray(boot);
        this.splitFlags = in.createIntArray();
        this.splitNames = in.createStringArray();
        this.splitRevisionCodes = in.createIntArray();
        this.externalStorage = in.readBoolean();
        this.baseHardwareAccelerated = in.readBoolean();
        this.allowBackup = in.readBoolean();
        this.killAfterRestore = in.readBoolean();
        this.restoreAnyVersion = in.readBoolean();
        this.fullBackupOnly = in.readBoolean();
        this.persistent = in.readBoolean();
        this.debuggable = in.readBoolean();
        this.vmSafeMode = in.readBoolean();
        this.hasCode = in.readBoolean();
        this.allowTaskReparenting = in.readBoolean();
        this.allowClearUserData = in.readBoolean();
        this.largeHeap = in.readBoolean();
        this.usesCleartextTraffic = in.readBoolean();
        this.supportsRtl = in.readBoolean();
        this.testOnly = in.readBoolean();
        this.multiArch = in.readBoolean();
        this.extractNativeLibs = in.readBoolean();
        this.game = in.readBoolean();
        this.resizeableActivity = android.content.pm.parsing.ParsingPackageImpl.sForBoolean.unparcel(in);
        this.staticSharedLibrary = in.readBoolean();
        this.overlay = in.readBoolean();
        this.isolatedSplitLoading = in.readBoolean();
        this.hasDomainUrls = in.readBoolean();
        this.profileableByShell = in.readBoolean();
        this.backupInForeground = in.readBoolean();
        this.useEmbeddedDex = in.readBoolean();
        this.defaultToDeviceProtectedStorage = in.readBoolean();
        this.directBootAware = in.readBoolean();
        this.partiallyDirectBootAware = in.readBoolean();
        this.resizeableActivityViaSdkVersion = in.readBoolean();
        this.allowClearUserDataOnFailedRestore = in.readBoolean();
        this.allowAudioPlaybackCapture = in.readBoolean();
        this.requestLegacyExternalStorage = in.readBoolean();
        this.usesNonSdkApi = in.readBoolean();
        this.hasFragileUserData = in.readBoolean();
        this.cantSaveState = in.readBoolean();
        this.allowNativeHeapPointerTagging = in.readBoolean();
        this.autoRevokePermissions = in.readInt();
        this.preserveLegacyExternalStorage = in.readBoolean();
        this.mimeGroups = ((android.util.ArraySet<java.lang.String>) (in.readArraySet(boot)));
        this.gwpAsanMode = in.readInt();
        this.minExtensionVersions = in.readSparseIntArray();
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.ParsingPackageImpl> CREATOR = new android.os.Parcelable.Creator<android.content.pm.parsing.ParsingPackageImpl>() {
        @java.lang.Override
        public android.content.pm.parsing.ParsingPackageImpl createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.ParsingPackageImpl(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.ParsingPackageImpl[] newArray(int size) {
            return new android.content.pm.parsing.ParsingPackageImpl[size];
        }
    };

    @java.lang.Override
    public int getVersionCode() {
        return versionCode;
    }

    @java.lang.Override
    public int getVersionCodeMajor() {
        return versionCodeMajor;
    }

    @java.lang.Override
    public int getBaseRevisionCode() {
        return baseRevisionCode;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getVersionName() {
        return versionName;
    }

    @java.lang.Override
    public int getCompileSdkVersion() {
        return compileSdkVersion;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getCompileSdkVersionCodeName() {
        return compileSdkVersionCodeName;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.lang.String getPackageName() {
        return packageName;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getRealPackage() {
        return realPackage;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.lang.String getBaseCodePath() {
        return baseCodePath;
    }

    @java.lang.Override
    public boolean isRequiredForAllUsers() {
        return requiredForAllUsers;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getRestrictedAccountType() {
        return restrictedAccountType;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getRequiredAccountType() {
        return requiredAccountType;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getOverlayTarget() {
        return overlayTarget;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getOverlayTargetName() {
        return overlayTargetName;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getOverlayCategory() {
        return overlayCategory;
    }

    @java.lang.Override
    public int getOverlayPriority() {
        return overlayPriority;
    }

    @java.lang.Override
    public boolean isOverlayIsStatic() {
        return overlayIsStatic;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getOverlayables() {
        return overlayables;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getStaticSharedLibName() {
        return staticSharedLibName;
    }

    @java.lang.Override
    public long getStaticSharedLibVersion() {
        return staticSharedLibVersion;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getLibraryNames() {
        return libraryNames;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getUsesLibraries() {
        return usesLibraries;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getUsesOptionalLibraries() {
        return usesOptionalLibraries;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getUsesStaticLibraries() {
        return usesStaticLibraries;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public long[] getUsesStaticLibrariesVersions() {
        return usesStaticLibrariesVersions;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String[][] getUsesStaticLibrariesCertDigests() {
        return usesStaticLibrariesCertDigests;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getSharedUserId() {
        return sharedUserId;
    }

    @java.lang.Override
    public int getSharedUserLabel() {
        return sharedUserLabel;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.ConfigurationInfo> getConfigPreferences() {
        return configPreferences;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.FeatureInfo> getReqFeatures() {
        return reqFeatures;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.FeatureGroupInfo> getFeatureGroups() {
        return featureGroups;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public byte[] getRestrictUpdateHash() {
        return restrictUpdateHash;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getOriginalPackages() {
        return originalPackages;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getAdoptPermissions() {
        return adoptPermissions;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getRequestedPermissions() {
        return requestedPermissions;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getImplicitPermissions() {
        return implicitPermissions;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.Set<java.lang.String> getUpgradeKeySets() {
        return upgradeKeySets;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.Map<java.lang.String, android.util.ArraySet<java.security.PublicKey>> getKeySetMapping() {
        return keySetMapping;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getProtectedBroadcasts() {
        return protectedBroadcasts;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.parsing.component.ParsedActivity> getActivities() {
        return activities;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.parsing.component.ParsedActivity> getReceivers() {
        return receivers;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.parsing.component.ParsedService> getServices() {
        return services;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.parsing.component.ParsedProvider> getProviders() {
        return providers;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.parsing.component.ParsedAttribution> getAttributions() {
        return attributions;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.parsing.component.ParsedPermission> getPermissions() {
        return permissions;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.parsing.component.ParsedPermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.pm.parsing.component.ParsedInstrumentation> getInstrumentations() {
        return instrumentations;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.util.Pair<java.lang.String, android.content.pm.parsing.component.ParsedIntentInfo>> getPreferredActivityFilters() {
        return preferredActivityFilters;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.Map<java.lang.String, android.content.pm.parsing.component.ParsedProcess> getProcesses() {
        return processes;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public android.os.Bundle getMetaData() {
        return metaData;
    }

    private void addMimeGroupsFromComponent(android.content.pm.parsing.component.ParsedComponent component) {
        for (int i = component.getIntents().size() - 1; i >= 0; i--) {
            android.content.IntentFilter filter = component.getIntents().get(i);
            for (int groupIndex = filter.countMimeGroups() - 1; groupIndex >= 0; groupIndex--) {
                mimeGroups = com.android.internal.util.ArrayUtils.add(mimeGroups, filter.getMimeGroup(groupIndex));
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public java.util.Set<java.lang.String> getMimeGroups() {
        return mimeGroups;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getVolumeUuid() {
        return volumeUuid;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public android.content.pm.PackageParser.SigningDetails getSigningDetails() {
        return signingDetails;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.lang.String getCodePath() {
        return codePath;
    }

    @java.lang.Override
    public boolean isUse32BitAbi() {
        return use32BitAbi;
    }

    @java.lang.Override
    public boolean isVisibleToInstantApps() {
        return visibleToInstantApps;
    }

    @java.lang.Override
    public boolean isForceQueryable() {
        return forceQueryable;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.content.Intent> getQueriesIntents() {
        return queriesIntents;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<java.lang.String> getQueriesPackages() {
        return queriesPackages;
    }

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.Set<java.lang.String> getQueriesProviders() {
        return queriesProviders;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String[] getSplitClassLoaderNames() {
        return splitClassLoaderNames;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String[] getSplitCodePaths() {
        return splitCodePaths;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public android.util.SparseArray<int[]> getSplitDependencies() {
        return splitDependencies;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public int[] getSplitFlags() {
        return splitFlags;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String[] getSplitNames() {
        return splitNames;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public int[] getSplitRevisionCodes() {
        return splitRevisionCodes;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getAppComponentFactory() {
        return appComponentFactory;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getBackupAgentName() {
        return backupAgentName;
    }

    @java.lang.Override
    public int getBanner() {
        return banner;
    }

    @java.lang.Override
    public int getCategory() {
        return category;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getClassLoaderName() {
        return classLoaderName;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getClassName() {
        return className;
    }

    @java.lang.Override
    public int getCompatibleWidthLimitDp() {
        return compatibleWidthLimitDp;
    }

    @java.lang.Override
    public int getDescriptionRes() {
        return descriptionRes;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return enabled;
    }

    @java.lang.Override
    public boolean isCrossProfile() {
        return crossProfile;
    }

    @java.lang.Override
    public int getFullBackupContent() {
        return fullBackupContent;
    }

    @java.lang.Override
    public int getIconRes() {
        return iconRes;
    }

    @java.lang.Override
    public int getInstallLocation() {
        return installLocation;
    }

    @java.lang.Override
    public int getLabelRes() {
        return labelRes;
    }

    @java.lang.Override
    public int getLargestWidthLimitDp() {
        return largestWidthLimitDp;
    }

    @java.lang.Override
    public int getLogo() {
        return logo;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getManageSpaceActivityName() {
        return manageSpaceActivityName;
    }

    @java.lang.Override
    public float getMaxAspectRatio() {
        return maxAspectRatio;
    }

    @java.lang.Override
    public float getMinAspectRatio() {
        return minAspectRatio;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public android.util.SparseIntArray getMinExtensionVersions() {
        return minExtensionVersions;
    }

    @java.lang.Override
    public int getMinSdkVersion() {
        return minSdkVersion;
    }

    @java.lang.Override
    public int getNetworkSecurityConfigRes() {
        return networkSecurityConfigRes;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.CharSequence getNonLocalizedLabel() {
        return nonLocalizedLabel;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getPermission() {
        return permission;
    }

    @java.lang.Override
    public int getRequiresSmallestWidthDp() {
        return requiresSmallestWidthDp;
    }

    @java.lang.Override
    public int getRoundIconRes() {
        return roundIconRes;
    }

    @java.lang.Override
    public int getTargetSandboxVersion() {
        return targetSandboxVersion;
    }

    @java.lang.Override
    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getTaskAffinity() {
        return taskAffinity;
    }

    @java.lang.Override
    public int getTheme() {
        return theme;
    }

    @java.lang.Override
    public int getUiOptions() {
        return uiOptions;
    }

    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.String getZygotePreloadName() {
        return zygotePreloadName;
    }

    @java.lang.Override
    public boolean isExternalStorage() {
        return externalStorage;
    }

    @java.lang.Override
    public boolean isBaseHardwareAccelerated() {
        return baseHardwareAccelerated;
    }

    @java.lang.Override
    public boolean isAllowBackup() {
        return allowBackup;
    }

    @java.lang.Override
    public boolean isKillAfterRestore() {
        return killAfterRestore;
    }

    @java.lang.Override
    public boolean isRestoreAnyVersion() {
        return restoreAnyVersion;
    }

    @java.lang.Override
    public boolean isFullBackupOnly() {
        return fullBackupOnly;
    }

    @java.lang.Override
    public boolean isPersistent() {
        return persistent;
    }

    @java.lang.Override
    public boolean isDebuggable() {
        return debuggable;
    }

    @java.lang.Override
    public boolean isVmSafeMode() {
        return vmSafeMode;
    }

    @java.lang.Override
    public boolean isHasCode() {
        return hasCode;
    }

    @java.lang.Override
    public boolean isAllowTaskReparenting() {
        return allowTaskReparenting;
    }

    @java.lang.Override
    public boolean isAllowClearUserData() {
        return allowClearUserData;
    }

    @java.lang.Override
    public boolean isLargeHeap() {
        return largeHeap;
    }

    @java.lang.Override
    public boolean isUsesCleartextTraffic() {
        return usesCleartextTraffic;
    }

    @java.lang.Override
    public boolean isSupportsRtl() {
        return supportsRtl;
    }

    @java.lang.Override
    public boolean isTestOnly() {
        return testOnly;
    }

    @java.lang.Override
    public boolean isMultiArch() {
        return multiArch;
    }

    @java.lang.Override
    public boolean isExtractNativeLibs() {
        return extractNativeLibs;
    }

    @java.lang.Override
    public boolean isGame() {
        return game;
    }

    /**
     *
     *
     * @see ParsingPackageRead#getResizeableActivity()
     */
    @android.annotation.Nullable
    @java.lang.Override
    public java.lang.Boolean getResizeableActivity() {
        return resizeableActivity;
    }

    @java.lang.Override
    public boolean isStaticSharedLibrary() {
        return staticSharedLibrary;
    }

    @java.lang.Override
    public boolean isOverlay() {
        return overlay;
    }

    @java.lang.Override
    public boolean isIsolatedSplitLoading() {
        return isolatedSplitLoading;
    }

    @java.lang.Override
    public boolean isHasDomainUrls() {
        return hasDomainUrls;
    }

    @java.lang.Override
    public boolean isProfileableByShell() {
        return profileableByShell;
    }

    @java.lang.Override
    public boolean isBackupInForeground() {
        return backupInForeground;
    }

    @java.lang.Override
    public boolean isUseEmbeddedDex() {
        return useEmbeddedDex;
    }

    @java.lang.Override
    public boolean isDefaultToDeviceProtectedStorage() {
        return defaultToDeviceProtectedStorage;
    }

    @java.lang.Override
    public boolean isDirectBootAware() {
        return directBootAware;
    }

    @java.lang.Override
    public int getGwpAsanMode() {
        return gwpAsanMode;
    }

    @java.lang.Override
    public boolean isPartiallyDirectBootAware() {
        return partiallyDirectBootAware;
    }

    @java.lang.Override
    public boolean isResizeableActivityViaSdkVersion() {
        return resizeableActivityViaSdkVersion;
    }

    @java.lang.Override
    public boolean isAllowClearUserDataOnFailedRestore() {
        return allowClearUserDataOnFailedRestore;
    }

    @java.lang.Override
    public boolean isAllowAudioPlaybackCapture() {
        return allowAudioPlaybackCapture;
    }

    @java.lang.Override
    public boolean isRequestLegacyExternalStorage() {
        return requestLegacyExternalStorage;
    }

    @java.lang.Override
    public boolean isUsesNonSdkApi() {
        return usesNonSdkApi;
    }

    @java.lang.Override
    public boolean isHasFragileUserData() {
        return hasFragileUserData;
    }

    @java.lang.Override
    public boolean isCantSaveState() {
        return cantSaveState;
    }

    @java.lang.Override
    public boolean isAllowNativeHeapPointerTagging() {
        return allowNativeHeapPointerTagging;
    }

    @java.lang.Override
    public int getAutoRevokePermissions() {
        return autoRevokePermissions;
    }

    @java.lang.Override
    public boolean hasPreserveLegacyExternalStorage() {
        return preserveLegacyExternalStorage;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setBaseRevisionCode(int value) {
        baseRevisionCode = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setCompileSdkVersion(int value) {
        compileSdkVersion = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRequiredForAllUsers(boolean value) {
        requiredForAllUsers = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setOverlayPriority(int value) {
        overlayPriority = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setOverlayIsStatic(boolean value) {
        overlayIsStatic = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setStaticSharedLibVersion(long value) {
        staticSharedLibVersion = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSharedUserLabel(int value) {
        sharedUserLabel = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRestrictUpdateHash(@android.annotation.Nullable
    byte... value) {
        restrictUpdateHash = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setUpgradeKeySets(@android.annotation.NonNull
    java.util.Set<java.lang.String> value) {
        upgradeKeySets = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setProcesses(@android.annotation.NonNull
    java.util.Map<java.lang.String, android.content.pm.parsing.component.ParsedProcess> value) {
        processes = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setMetaData(@android.annotation.Nullable
    android.os.Bundle value) {
        metaData = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSigningDetails(@android.annotation.Nullable
    android.content.pm.PackageParser.SigningDetails value) {
        signingDetails = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setUse32BitAbi(boolean value) {
        use32BitAbi = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setVisibleToInstantApps(boolean value) {
        visibleToInstantApps = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setForceQueryable(boolean value) {
        forceQueryable = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setBanner(int value) {
        banner = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setCategory(int value) {
        category = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setCompatibleWidthLimitDp(int value) {
        compatibleWidthLimitDp = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setDescriptionRes(int value) {
        descriptionRes = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setEnabled(boolean value) {
        enabled = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setCrossProfile(boolean value) {
        crossProfile = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setFullBackupContent(int value) {
        fullBackupContent = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setIconRes(int value) {
        iconRes = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setInstallLocation(int value) {
        installLocation = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setLabelRes(int value) {
        labelRes = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setLargestWidthLimitDp(int value) {
        largestWidthLimitDp = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setLogo(int value) {
        logo = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setMaxAspectRatio(float value) {
        maxAspectRatio = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setMinAspectRatio(float value) {
        minAspectRatio = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setMinExtensionVersions(@android.annotation.Nullable
    android.util.SparseIntArray value) {
        minExtensionVersions = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setMinSdkVersion(int value) {
        minSdkVersion = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setNetworkSecurityConfigRes(int value) {
        networkSecurityConfigRes = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setNonLocalizedLabel(@android.annotation.Nullable
    java.lang.CharSequence value) {
        nonLocalizedLabel = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRequiresSmallestWidthDp(int value) {
        requiresSmallestWidthDp = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRoundIconRes(int value) {
        roundIconRes = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setTargetSandboxVersion(int value) {
        targetSandboxVersion = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setTargetSdkVersion(int value) {
        targetSdkVersion = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setTheme(int value) {
        theme = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setUiOptions(int value) {
        uiOptions = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setExternalStorage(boolean value) {
        externalStorage = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setBaseHardwareAccelerated(boolean value) {
        baseHardwareAccelerated = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAllowBackup(boolean value) {
        allowBackup = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setKillAfterRestore(boolean value) {
        killAfterRestore = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRestoreAnyVersion(boolean value) {
        restoreAnyVersion = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setFullBackupOnly(boolean value) {
        fullBackupOnly = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setPersistent(boolean value) {
        persistent = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setDebuggable(boolean value) {
        debuggable = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setVmSafeMode(boolean value) {
        vmSafeMode = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setHasCode(boolean value) {
        hasCode = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAllowTaskReparenting(boolean value) {
        allowTaskReparenting = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAllowClearUserData(boolean value) {
        allowClearUserData = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setLargeHeap(boolean value) {
        largeHeap = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setUsesCleartextTraffic(boolean value) {
        usesCleartextTraffic = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setSupportsRtl(boolean value) {
        supportsRtl = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setTestOnly(boolean value) {
        testOnly = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setMultiArch(boolean value) {
        multiArch = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setExtractNativeLibs(boolean value) {
        extractNativeLibs = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setGame(boolean value) {
        game = value;
        return this;
    }

    /**
     *
     *
     * @see ParsingPackageRead#getResizeableActivity()
     */
    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setResizeableActivity(@android.annotation.Nullable
    java.lang.Boolean value) {
        resizeableActivity = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setStaticSharedLibrary(boolean value) {
        staticSharedLibrary = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setOverlay(boolean value) {
        overlay = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setIsolatedSplitLoading(boolean value) {
        isolatedSplitLoading = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setHasDomainUrls(boolean value) {
        hasDomainUrls = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setProfileableByShell(boolean value) {
        profileableByShell = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setBackupInForeground(boolean value) {
        backupInForeground = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setUseEmbeddedDex(boolean value) {
        useEmbeddedDex = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setDefaultToDeviceProtectedStorage(boolean value) {
        defaultToDeviceProtectedStorage = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setDirectBootAware(boolean value) {
        directBootAware = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setGwpAsanMode(int value) {
        gwpAsanMode = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setPartiallyDirectBootAware(boolean value) {
        partiallyDirectBootAware = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setResizeableActivityViaSdkVersion(boolean value) {
        resizeableActivityViaSdkVersion = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAllowClearUserDataOnFailedRestore(boolean value) {
        allowClearUserDataOnFailedRestore = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAllowAudioPlaybackCapture(boolean value) {
        allowAudioPlaybackCapture = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRequestLegacyExternalStorage(boolean value) {
        requestLegacyExternalStorage = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setUsesNonSdkApi(boolean value) {
        usesNonSdkApi = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setHasFragileUserData(boolean value) {
        hasFragileUserData = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setCantSaveState(boolean value) {
        cantSaveState = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAllowNativeHeapPointerTagging(boolean value) {
        allowNativeHeapPointerTagging = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAutoRevokePermissions(int value) {
        autoRevokePermissions = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setPreserveLegacyExternalStorage(boolean value) {
        preserveLegacyExternalStorage = value;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setVersionName(java.lang.String versionName) {
        this.versionName = versionName;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackage setCompileSdkVersionCodename(java.lang.String compileSdkVersionCodename) {
        this.compileSdkVersionCodeName = compileSdkVersionCodename;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setProcessName(java.lang.String processName) {
        this.processName = processName;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRealPackage(@android.annotation.Nullable
    java.lang.String realPackage) {
        this.realPackage = realPackage;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setRestrictedAccountType(@android.annotation.Nullable
    java.lang.String restrictedAccountType) {
        this.restrictedAccountType = restrictedAccountType;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setOverlayTargetName(@android.annotation.Nullable
    java.lang.String overlayTargetName) {
        this.overlayTargetName = overlayTargetName;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setOverlayCategory(@android.annotation.Nullable
    java.lang.String overlayCategory) {
        this.overlayCategory = overlayCategory;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setAppComponentFactory(@android.annotation.Nullable
    java.lang.String appComponentFactory) {
        this.appComponentFactory = appComponentFactory;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setBackupAgentName(@android.annotation.Nullable
    java.lang.String backupAgentName) {
        this.backupAgentName = backupAgentName;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setClassLoaderName(@android.annotation.Nullable
    java.lang.String classLoaderName) {
        this.classLoaderName = classLoaderName;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setClassName(@android.annotation.Nullable
    java.lang.String className) {
        this.className = className;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setManageSpaceActivityName(@android.annotation.Nullable
    java.lang.String manageSpaceActivityName) {
        this.manageSpaceActivityName = manageSpaceActivityName;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setPermission(@android.annotation.Nullable
    java.lang.String permission) {
        this.permission = permission;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setTaskAffinity(@android.annotation.Nullable
    java.lang.String taskAffinity) {
        this.taskAffinity = taskAffinity;
        return this;
    }

    @java.lang.Override
    public android.content.pm.parsing.ParsingPackageImpl setZygotePreloadName(@android.annotation.Nullable
    java.lang.String zygotePreloadName) {
        this.zygotePreloadName = zygotePreloadName;
        return this;
    }
}

