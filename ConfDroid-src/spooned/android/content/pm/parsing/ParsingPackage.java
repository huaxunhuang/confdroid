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
 * Methods used for mutation during direct package parsing.
 *
 * @unknown 
 */
@java.lang.SuppressWarnings("UnusedReturnValue")
public interface ParsingPackage extends android.content.pm.parsing.ParsingPackageRead {
    android.content.pm.parsing.ParsingPackage addActivity(android.content.pm.parsing.component.ParsedActivity parsedActivity);

    android.content.pm.parsing.ParsingPackage addAdoptPermission(java.lang.String adoptPermission);

    android.content.pm.parsing.ParsingPackage addConfigPreference(android.content.pm.ConfigurationInfo configPreference);

    android.content.pm.parsing.ParsingPackage addFeatureGroup(android.content.pm.FeatureGroupInfo featureGroup);

    android.content.pm.parsing.ParsingPackage addImplicitPermission(java.lang.String permission);

    android.content.pm.parsing.ParsingPackage addInstrumentation(android.content.pm.parsing.component.ParsedInstrumentation instrumentation);

    android.content.pm.parsing.ParsingPackage addKeySet(java.lang.String keySetName, java.security.PublicKey publicKey);

    android.content.pm.parsing.ParsingPackage addLibraryName(java.lang.String libraryName);

    android.content.pm.parsing.ParsingPackage addOriginalPackage(java.lang.String originalPackage);

    android.content.pm.parsing.ParsingPackage addOverlayable(java.lang.String overlayableName, java.lang.String actorName);

    android.content.pm.parsing.ParsingPackage addPermission(android.content.pm.parsing.component.ParsedPermission permission);

    android.content.pm.parsing.ParsingPackage addPermissionGroup(android.content.pm.parsing.component.ParsedPermissionGroup permissionGroup);

    android.content.pm.parsing.ParsingPackage addPreferredActivityFilter(java.lang.String className, android.content.pm.parsing.component.ParsedIntentInfo intentInfo);

    android.content.pm.parsing.ParsingPackage addProtectedBroadcast(java.lang.String protectedBroadcast);

    android.content.pm.parsing.ParsingPackage addProvider(android.content.pm.parsing.component.ParsedProvider parsedProvider);

    android.content.pm.parsing.ParsingPackage addAttribution(android.content.pm.parsing.component.ParsedAttribution attribution);

    android.content.pm.parsing.ParsingPackage addReceiver(android.content.pm.parsing.component.ParsedActivity parsedReceiver);

    android.content.pm.parsing.ParsingPackage addReqFeature(android.content.pm.FeatureInfo reqFeature);

    android.content.pm.parsing.ParsingPackage addRequestedPermission(java.lang.String permission);

    android.content.pm.parsing.ParsingPackage addService(android.content.pm.parsing.component.ParsedService parsedService);

    android.content.pm.parsing.ParsingPackage addUsesLibrary(java.lang.String libraryName);

    android.content.pm.parsing.ParsingPackage addUsesOptionalLibrary(java.lang.String libraryName);

    android.content.pm.parsing.ParsingPackage addUsesStaticLibrary(java.lang.String libraryName);

    android.content.pm.parsing.ParsingPackage addUsesStaticLibraryCertDigests(java.lang.String[] certSha256Digests);

    android.content.pm.parsing.ParsingPackage addUsesStaticLibraryVersion(long version);

    android.content.pm.parsing.ParsingPackage addQueriesIntent(android.content.Intent intent);

    android.content.pm.parsing.ParsingPackage addQueriesPackage(java.lang.String packageName);

    android.content.pm.parsing.ParsingPackage addQueriesProvider(java.lang.String authority);

    android.content.pm.parsing.ParsingPackage setProcesses(@android.annotation.NonNull
    java.util.Map<java.lang.String, android.content.pm.parsing.component.ParsedProcess> processes);

    android.content.pm.parsing.ParsingPackage asSplit(java.lang.String[] splitNames, java.lang.String[] splitCodePaths, int[] splitRevisionCodes, @android.annotation.Nullable
    android.util.SparseArray<int[]> splitDependencies);

    android.content.pm.parsing.ParsingPackage setMetaData(android.os.Bundle metaData);

    android.content.pm.parsing.ParsingPackage setForceQueryable(boolean forceQueryable);

    android.content.pm.parsing.ParsingPackage setMaxAspectRatio(float maxAspectRatio);

    android.content.pm.parsing.ParsingPackage setMinAspectRatio(float minAspectRatio);

    android.content.pm.parsing.ParsingPackage setPermission(java.lang.String permission);

    android.content.pm.parsing.ParsingPackage setProcessName(java.lang.String processName);

    android.content.pm.parsing.ParsingPackage setSharedUserId(java.lang.String sharedUserId);

    android.content.pm.parsing.ParsingPackage setStaticSharedLibName(java.lang.String staticSharedLibName);

    android.content.pm.parsing.ParsingPackage setTaskAffinity(java.lang.String taskAffinity);

    android.content.pm.parsing.ParsingPackage setTargetSdkVersion(int targetSdkVersion);

    android.content.pm.parsing.ParsingPackage setUiOptions(int uiOptions);

    android.content.pm.parsing.ParsingPackage setBaseHardwareAccelerated(boolean baseHardwareAccelerated);

    android.content.pm.parsing.ParsingPackage setResizeableActivity(java.lang.Boolean resizeable);

    android.content.pm.parsing.ParsingPackage setResizeableActivityViaSdkVersion(boolean resizeableViaSdkVersion);

    android.content.pm.parsing.ParsingPackage setAllowAudioPlaybackCapture(boolean allowAudioPlaybackCapture);

    android.content.pm.parsing.ParsingPackage setAllowBackup(boolean allowBackup);

    android.content.pm.parsing.ParsingPackage setAllowClearUserData(boolean allowClearUserData);

    android.content.pm.parsing.ParsingPackage setAllowClearUserDataOnFailedRestore(boolean allowClearUserDataOnFailedRestore);

    android.content.pm.parsing.ParsingPackage setAllowTaskReparenting(boolean allowTaskReparenting);

    android.content.pm.parsing.ParsingPackage setOverlay(boolean isOverlay);

    android.content.pm.parsing.ParsingPackage setBackupInForeground(boolean backupInForeground);

    android.content.pm.parsing.ParsingPackage setCantSaveState(boolean cantSaveState);

    android.content.pm.parsing.ParsingPackage setDebuggable(boolean debuggable);

    android.content.pm.parsing.ParsingPackage setDefaultToDeviceProtectedStorage(boolean defaultToDeviceProtectedStorage);

    android.content.pm.parsing.ParsingPackage setDirectBootAware(boolean directBootAware);

    android.content.pm.parsing.ParsingPackage setExternalStorage(boolean externalStorage);

    android.content.pm.parsing.ParsingPackage setExtractNativeLibs(boolean extractNativeLibs);

    android.content.pm.parsing.ParsingPackage setFullBackupOnly(boolean fullBackupOnly);

    android.content.pm.parsing.ParsingPackage setHasCode(boolean hasCode);

    android.content.pm.parsing.ParsingPackage setHasFragileUserData(boolean hasFragileUserData);

    android.content.pm.parsing.ParsingPackage setGame(boolean isGame);

    android.content.pm.parsing.ParsingPackage setIsolatedSplitLoading(boolean isolatedSplitLoading);

    android.content.pm.parsing.ParsingPackage setKillAfterRestore(boolean killAfterRestore);

    android.content.pm.parsing.ParsingPackage setLargeHeap(boolean largeHeap);

    android.content.pm.parsing.ParsingPackage setMultiArch(boolean multiArch);

    android.content.pm.parsing.ParsingPackage setPartiallyDirectBootAware(boolean partiallyDirectBootAware);

    android.content.pm.parsing.ParsingPackage setPersistent(boolean persistent);

    android.content.pm.parsing.ParsingPackage setProfileableByShell(boolean profileableByShell);

    android.content.pm.parsing.ParsingPackage setRequestLegacyExternalStorage(boolean requestLegacyExternalStorage);

    android.content.pm.parsing.ParsingPackage setAllowNativeHeapPointerTagging(boolean allowNativeHeapPointerTagging);

    android.content.pm.parsing.ParsingPackage setAutoRevokePermissions(int autoRevokePermissions);

    android.content.pm.parsing.ParsingPackage setPreserveLegacyExternalStorage(boolean preserveLegacyExternalStorage);

    android.content.pm.parsing.ParsingPackage setRestoreAnyVersion(boolean restoreAnyVersion);

    android.content.pm.parsing.ParsingPackage setSplitHasCode(int splitIndex, boolean splitHasCode);

    android.content.pm.parsing.ParsingPackage setStaticSharedLibrary(boolean staticSharedLibrary);

    android.content.pm.parsing.ParsingPackage setSupportsRtl(boolean supportsRtl);

    android.content.pm.parsing.ParsingPackage setTestOnly(boolean testOnly);

    android.content.pm.parsing.ParsingPackage setUseEmbeddedDex(boolean useEmbeddedDex);

    android.content.pm.parsing.ParsingPackage setUsesCleartextTraffic(boolean usesCleartextTraffic);

    android.content.pm.parsing.ParsingPackage setUsesNonSdkApi(boolean usesNonSdkApi);

    android.content.pm.parsing.ParsingPackage setVisibleToInstantApps(boolean visibleToInstantApps);

    android.content.pm.parsing.ParsingPackage setVmSafeMode(boolean vmSafeMode);

    android.content.pm.parsing.ParsingPackage removeUsesOptionalLibrary(java.lang.String libraryName);

    android.content.pm.parsing.ParsingPackage setAnyDensity(int anyDensity);

    android.content.pm.parsing.ParsingPackage setAppComponentFactory(java.lang.String appComponentFactory);

    android.content.pm.parsing.ParsingPackage setBackupAgentName(java.lang.String backupAgentName);

    android.content.pm.parsing.ParsingPackage setBanner(int banner);

    android.content.pm.parsing.ParsingPackage setCategory(int category);

    android.content.pm.parsing.ParsingPackage setClassLoaderName(java.lang.String classLoaderName);

    android.content.pm.parsing.ParsingPackage setClassName(java.lang.String className);

    android.content.pm.parsing.ParsingPackage setCompatibleWidthLimitDp(int compatibleWidthLimitDp);

    android.content.pm.parsing.ParsingPackage setDescriptionRes(int descriptionRes);

    android.content.pm.parsing.ParsingPackage setEnabled(boolean enabled);

    android.content.pm.parsing.ParsingPackage setGwpAsanMode(int gwpAsanMode);

    android.content.pm.parsing.ParsingPackage setCrossProfile(boolean crossProfile);

    android.content.pm.parsing.ParsingPackage setFullBackupContent(int fullBackupContent);

    android.content.pm.parsing.ParsingPackage setHasDomainUrls(boolean hasDomainUrls);

    android.content.pm.parsing.ParsingPackage setIconRes(int iconRes);

    android.content.pm.parsing.ParsingPackage setInstallLocation(int installLocation);

    android.content.pm.parsing.ParsingPackage setLabelRes(int labelRes);

    android.content.pm.parsing.ParsingPackage setLargestWidthLimitDp(int largestWidthLimitDp);

    android.content.pm.parsing.ParsingPackage setLogo(int logo);

    android.content.pm.parsing.ParsingPackage setManageSpaceActivityName(java.lang.String manageSpaceActivityName);

    android.content.pm.parsing.ParsingPackage setMinExtensionVersions(@android.annotation.Nullable
    android.util.SparseIntArray minExtensionVersions);

    android.content.pm.parsing.ParsingPackage setMinSdkVersion(int minSdkVersion);

    android.content.pm.parsing.ParsingPackage setNetworkSecurityConfigRes(int networkSecurityConfigRes);

    android.content.pm.parsing.ParsingPackage setNonLocalizedLabel(java.lang.CharSequence nonLocalizedLabel);

    android.content.pm.parsing.ParsingPackage setOverlayCategory(java.lang.String overlayCategory);

    android.content.pm.parsing.ParsingPackage setOverlayIsStatic(boolean overlayIsStatic);

    android.content.pm.parsing.ParsingPackage setOverlayPriority(int overlayPriority);

    android.content.pm.parsing.ParsingPackage setOverlayTarget(java.lang.String overlayTarget);

    android.content.pm.parsing.ParsingPackage setOverlayTargetName(java.lang.String overlayTargetName);

    android.content.pm.parsing.ParsingPackage setRealPackage(java.lang.String realPackage);

    android.content.pm.parsing.ParsingPackage setRequiredAccountType(java.lang.String requiredAccountType);

    android.content.pm.parsing.ParsingPackage setRequiredForAllUsers(boolean requiredForAllUsers);

    android.content.pm.parsing.ParsingPackage setRequiresSmallestWidthDp(int requiresSmallestWidthDp);

    android.content.pm.parsing.ParsingPackage setResizeable(int resizeable);

    android.content.pm.parsing.ParsingPackage setRestrictUpdateHash(byte[] restrictUpdateHash);

    android.content.pm.parsing.ParsingPackage setRestrictedAccountType(java.lang.String restrictedAccountType);

    android.content.pm.parsing.ParsingPackage setRoundIconRes(int roundIconRes);

    android.content.pm.parsing.ParsingPackage setSharedUserLabel(int sharedUserLabel);

    android.content.pm.parsing.ParsingPackage setSigningDetails(android.content.pm.PackageParser.SigningDetails signingDetails);

    android.content.pm.parsing.ParsingPackage setSplitClassLoaderName(int splitIndex, java.lang.String classLoaderName);

    android.content.pm.parsing.ParsingPackage setStaticSharedLibVersion(long staticSharedLibVersion);

    android.content.pm.parsing.ParsingPackage setSupportsLargeScreens(int supportsLargeScreens);

    android.content.pm.parsing.ParsingPackage setSupportsNormalScreens(int supportsNormalScreens);

    android.content.pm.parsing.ParsingPackage setSupportsSmallScreens(int supportsSmallScreens);

    android.content.pm.parsing.ParsingPackage setSupportsExtraLargeScreens(int supportsExtraLargeScreens);

    android.content.pm.parsing.ParsingPackage setTargetSandboxVersion(int targetSandboxVersion);

    android.content.pm.parsing.ParsingPackage setTheme(int theme);

    android.content.pm.parsing.ParsingPackage setUpgradeKeySets(@android.annotation.NonNull
    java.util.Set<java.lang.String> upgradeKeySets);

    android.content.pm.parsing.ParsingPackage setUse32BitAbi(boolean use32BitAbi);

    android.content.pm.parsing.ParsingPackage setVolumeUuid(@android.annotation.Nullable
    java.lang.String volumeUuid);

    android.content.pm.parsing.ParsingPackage setZygotePreloadName(java.lang.String zygotePreloadName);

    android.content.pm.parsing.ParsingPackage sortActivities();

    android.content.pm.parsing.ParsingPackage sortReceivers();

    android.content.pm.parsing.ParsingPackage sortServices();

    android.content.pm.parsing.ParsingPackage setBaseRevisionCode(int baseRevisionCode);

    android.content.pm.parsing.ParsingPackage setVersionName(java.lang.String versionName);

    android.content.pm.parsing.ParsingPackage setCompileSdkVersion(int compileSdkVersion);

    android.content.pm.parsing.ParsingPackage setCompileSdkVersionCodename(java.lang.String compileSdkVersionCodename);

    // TODO(b/135203078): This class no longer has access to ParsedPackage, find a replacement
    // for moving to the next step
    @java.lang.Deprecated
    java.lang.Object hideAsParsed();
}

