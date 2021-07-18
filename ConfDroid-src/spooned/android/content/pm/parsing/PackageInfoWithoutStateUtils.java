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
 *
 *
 * @unknown *
 */
public class PackageInfoWithoutStateUtils {
    @android.annotation.Nullable
    public static android.content.pm.PackageInfo generate(android.content.pm.parsing.ParsingPackageRead pkg, int[] gids, @android.content.pm.PackageManager.PackageInfoFlags
    int flags, long firstInstallTime, long lastUpdateTime, java.util.Set<java.lang.String> grantedPermissions, android.content.pm.PackageUserState state, int userId) {
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateWithComponents(pkg, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, state, userId, null);
    }

    @android.annotation.Nullable
    public static android.content.pm.PackageInfo generate(android.content.pm.parsing.ParsingPackageRead pkg, android.apex.ApexInfo apexInfo, int flags) {
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateWithComponents(pkg, EmptyArray.INT, flags, 0, 0, java.util.Collections.emptySet(), new android.content.pm.PackageUserState(), android.os.UserHandle.getCallingUserId(), apexInfo);
    }

    @android.annotation.Nullable
    private static android.content.pm.PackageInfo generateWithComponents(android.content.pm.parsing.ParsingPackageRead pkg, int[] gids, @android.content.pm.PackageManager.PackageInfoFlags
    int flags, long firstInstallTime, long lastUpdateTime, java.util.Set<java.lang.String> grantedPermissions, android.content.pm.PackageUserState state, int userId, @android.annotation.Nullable
    android.apex.ApexInfo apexInfo) {
        android.content.pm.ApplicationInfo applicationInfo = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateApplicationInfo(pkg, flags, state, userId);
        if (applicationInfo == null) {
            return null;
        }
        android.content.pm.PackageInfo info = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateWithoutComponents(pkg, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, state, userId, apexInfo, applicationInfo);
        if (info == null) {
            return null;
        }
        if ((flags & android.content.pm.PackageManager.GET_ACTIVITIES) != 0) {
            final int N = pkg.getActivities().size();
            if (N > 0) {
                int num = 0;
                final android.content.pm.ActivityInfo[] res = new android.content.pm.ActivityInfo[N];
                for (int i = 0; i < N; i++) {
                    final android.content.pm.parsing.component.ParsedActivity a = pkg.getActivities().get(i);
                    if (android.content.pm.parsing.component.ComponentParseUtils.isMatch(state, false, pkg.isEnabled(), a, flags)) {
                        if (android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(a.getName())) {
                            continue;
                        }
                        res[num++] = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateActivityInfo(pkg, a, flags, state, applicationInfo, userId);
                    }
                }
                info.activities = com.android.internal.util.ArrayUtils.trimToSize(res, num);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_RECEIVERS) != 0) {
            final int size = pkg.getReceivers().size();
            if (size > 0) {
                int num = 0;
                final android.content.pm.ActivityInfo[] res = new android.content.pm.ActivityInfo[size];
                for (int i = 0; i < size; i++) {
                    final android.content.pm.parsing.component.ParsedActivity a = pkg.getReceivers().get(i);
                    if (android.content.pm.parsing.component.ComponentParseUtils.isMatch(state, false, pkg.isEnabled(), a, flags)) {
                        res[num++] = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateActivityInfo(pkg, a, flags, state, applicationInfo, userId);
                    }
                }
                info.receivers = com.android.internal.util.ArrayUtils.trimToSize(res, num);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_SERVICES) != 0) {
            final int size = pkg.getServices().size();
            if (size > 0) {
                int num = 0;
                final android.content.pm.ServiceInfo[] res = new android.content.pm.ServiceInfo[size];
                for (int i = 0; i < size; i++) {
                    final android.content.pm.parsing.component.ParsedService s = pkg.getServices().get(i);
                    if (android.content.pm.parsing.component.ComponentParseUtils.isMatch(state, false, pkg.isEnabled(), s, flags)) {
                        res[num++] = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateServiceInfo(pkg, s, flags, state, applicationInfo, userId);
                    }
                }
                info.services = com.android.internal.util.ArrayUtils.trimToSize(res, num);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_PROVIDERS) != 0) {
            final int size = pkg.getProviders().size();
            if (size > 0) {
                int num = 0;
                final android.content.pm.ProviderInfo[] res = new android.content.pm.ProviderInfo[size];
                for (int i = 0; i < size; i++) {
                    final android.content.pm.parsing.component.ParsedProvider pr = pkg.getProviders().get(i);
                    if (android.content.pm.parsing.component.ComponentParseUtils.isMatch(state, false, pkg.isEnabled(), pr, flags)) {
                        res[num++] = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateProviderInfo(pkg, pr, flags, state, applicationInfo, userId);
                    }
                }
                info.providers = com.android.internal.util.ArrayUtils.trimToSize(res, num);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_INSTRUMENTATION) != 0) {
            int N = pkg.getInstrumentations().size();
            if (N > 0) {
                info.instrumentation = new android.content.pm.InstrumentationInfo[N];
                for (int i = 0; i < N; i++) {
                    info.instrumentation[i] = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateInstrumentationInfo(pkg.getInstrumentations().get(i), pkg, flags, userId);
                }
            }
        }
        return info;
    }

    @android.annotation.Nullable
    public static android.content.pm.PackageInfo generateWithoutComponents(android.content.pm.parsing.ParsingPackageRead pkg, int[] gids, @android.content.pm.PackageManager.PackageInfoFlags
    int flags, long firstInstallTime, long lastUpdateTime, java.util.Set<java.lang.String> grantedPermissions, android.content.pm.PackageUserState state, int userId, @android.annotation.Nullable
    android.apex.ApexInfo apexInfo, @android.annotation.NonNull
    android.content.pm.ApplicationInfo applicationInfo) {
        if (!android.content.pm.parsing.PackageInfoWithoutStateUtils.checkUseInstalled(pkg, state, flags)) {
            return null;
        }
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateWithoutComponentsUnchecked(pkg, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, state, userId, apexInfo, applicationInfo);
    }

    /**
     * This bypasses critical checks that are necessary for usage with data passed outside of
     * system server.
     *
     * Prefer {@link #generateWithoutComponents(ParsingPackageRead, int[], int, long, long, Set,
     * PackageUserState, int, ApexInfo, ApplicationInfo)}.
     */
    @android.annotation.NonNull
    public static android.content.pm.PackageInfo generateWithoutComponentsUnchecked(android.content.pm.parsing.ParsingPackageRead pkg, int[] gids, @android.content.pm.PackageManager.PackageInfoFlags
    int flags, long firstInstallTime, long lastUpdateTime, java.util.Set<java.lang.String> grantedPermissions, android.content.pm.PackageUserState state, int userId, @android.annotation.Nullable
    android.apex.ApexInfo apexInfo, @android.annotation.NonNull
    android.content.pm.ApplicationInfo applicationInfo) {
        android.content.pm.PackageInfo pi = new android.content.pm.PackageInfo();
        pi.packageName = pkg.getPackageName();
        pi.splitNames = pkg.getSplitNames();
        pi.versionCode = pkg.getVersionCode();
        pi.versionCodeMajor = pkg.getVersionCodeMajor();
        pi.baseRevisionCode = pkg.getBaseRevisionCode();
        pi.splitRevisionCodes = pkg.getSplitRevisionCodes();
        pi.versionName = pkg.getVersionName();
        pi.sharedUserId = pkg.getSharedUserId();
        pi.sharedUserLabel = pkg.getSharedUserLabel();
        pi.applicationInfo = applicationInfo;
        pi.installLocation = pkg.getInstallLocation();
        if (((pi.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) || ((pi.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)) {
            pi.requiredForAllUsers = pkg.isRequiredForAllUsers();
        }
        pi.restrictedAccountType = pkg.getRestrictedAccountType();
        pi.requiredAccountType = pkg.getRequiredAccountType();
        pi.overlayTarget = pkg.getOverlayTarget();
        pi.targetOverlayableName = pkg.getOverlayTargetName();
        pi.overlayCategory = pkg.getOverlayCategory();
        pi.overlayPriority = pkg.getOverlayPriority();
        pi.mOverlayIsStatic = pkg.isOverlayIsStatic();
        pi.compileSdkVersion = pkg.getCompileSdkVersion();
        pi.compileSdkVersionCodename = pkg.getCompileSdkVersionCodeName();
        pi.firstInstallTime = firstInstallTime;
        pi.lastUpdateTime = lastUpdateTime;
        if ((flags & android.content.pm.PackageManager.GET_GIDS) != 0) {
            pi.gids = gids;
        }
        if ((flags & android.content.pm.PackageManager.GET_CONFIGURATIONS) != 0) {
            int size = pkg.getConfigPreferences().size();
            if (size > 0) {
                pi.configPreferences = new android.content.pm.ConfigurationInfo[size];
                pkg.getConfigPreferences().toArray(pi.configPreferences);
            }
            size = pkg.getReqFeatures().size();
            if (size > 0) {
                pi.reqFeatures = new android.content.pm.FeatureInfo[size];
                pkg.getReqFeatures().toArray(pi.reqFeatures);
            }
            size = pkg.getFeatureGroups().size();
            if (size > 0) {
                pi.featureGroups = new android.content.pm.FeatureGroupInfo[size];
                pkg.getFeatureGroups().toArray(pi.featureGroups);
            }
        }
        if ((flags & android.content.pm.PackageManager.GET_PERMISSIONS) != 0) {
            int size = com.android.internal.util.ArrayUtils.size(pkg.getPermissions());
            if (size > 0) {
                pi.permissions = new android.content.pm.PermissionInfo[size];
                for (int i = 0; i < size; i++) {
                    pi.permissions[i] = android.content.pm.parsing.PackageInfoWithoutStateUtils.generatePermissionInfo(pkg.getPermissions().get(i), flags);
                }
            }
            size = pkg.getRequestedPermissions().size();
            if (size > 0) {
                pi.requestedPermissions = new java.lang.String[size];
                pi.requestedPermissionsFlags = new int[size];
                for (int i = 0; i < size; i++) {
                    final java.lang.String perm = pkg.getRequestedPermissions().get(i);
                    pi.requestedPermissions[i] = perm;
                    // The notion of required permissions is deprecated but for compatibility.
                    pi.requestedPermissionsFlags[i] |= android.content.pm.PackageInfo.REQUESTED_PERMISSION_REQUIRED;
                    if ((grantedPermissions != null) && grantedPermissions.contains(perm)) {
                        pi.requestedPermissionsFlags[i] |= android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED;
                    }
                }
            }
        }
        if (apexInfo != null) {
            java.io.File apexFile = new java.io.File(apexInfo.modulePath);
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
        }
        android.content.pm.PackageParser.SigningDetails signingDetails = pkg.getSigningDetails();
        // deprecated method of getting signing certificates
        if ((flags & android.content.pm.PackageManager.GET_SIGNATURES) != 0) {
            if (signingDetails.hasPastSigningCertificates()) {
                // Package has included signing certificate rotation information.  Return the oldest
                // cert so that programmatic checks keep working even if unaware of key rotation.
                pi.signatures = new android.content.pm.Signature[1];
                pi.signatures[0] = signingDetails.pastSigningCertificates[0];
            } else
                if (signingDetails.hasSignatures()) {
                    // otherwise keep old behavior
                    int numberOfSigs = signingDetails.signatures.length;
                    pi.signatures = new android.content.pm.Signature[numberOfSigs];
                    java.lang.System.arraycopy(signingDetails.signatures, 0, pi.signatures, 0, numberOfSigs);
                }

        }
        // replacement for GET_SIGNATURES
        if ((flags & android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES) != 0) {
            if (signingDetails != android.content.pm.PackageParser.SigningDetails.UNKNOWN) {
                // only return a valid SigningInfo if there is signing information to report
                pi.signingInfo = new android.content.pm.SigningInfo(signingDetails);
            } else {
                pi.signingInfo = null;
            }
        }
        return pi;
    }

    @android.annotation.Nullable
    public static android.content.pm.ApplicationInfo generateApplicationInfo(android.content.pm.parsing.ParsingPackageRead pkg, @android.content.pm.PackageManager.ApplicationInfoFlags
    int flags, android.content.pm.PackageUserState state, int userId) {
        if (pkg == null) {
            return null;
        }
        if (!android.content.pm.parsing.PackageInfoWithoutStateUtils.checkUseInstalled(pkg, state, flags)) {
            return null;
        }
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateApplicationInfoUnchecked(pkg, flags, state, userId);
    }

    /**
     * This bypasses critical checks that are necessary for usage with data passed outside of
     * system server.
     *
     * Prefer {@link #generateApplicationInfo(ParsingPackageRead, int, PackageUserState, int)}.
     */
    @android.annotation.NonNull
    public static android.content.pm.ApplicationInfo generateApplicationInfoUnchecked(@android.annotation.NonNull
    android.content.pm.parsing.ParsingPackageRead pkg, @android.content.pm.PackageManager.ApplicationInfoFlags
    int flags, android.content.pm.PackageUserState state, int userId) {
        // Make shallow copy so we can store the metadata/libraries safely
        android.content.pm.ApplicationInfo ai = pkg.toAppInfoWithoutState();
        // Init handles data directories
        // TODO(b/135203078): Consolidate the data directory logic, remove initForUser
        ai.initForUser(userId);
        if ((flags & android.content.pm.PackageManager.GET_META_DATA) == 0) {
            ai.metaData = null;
        }
        if ((flags & android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES) == 0) {
            ai.sharedLibraryFiles = null;
            ai.sharedLibraryInfos = null;
        }
        // CompatibilityMode is global state.
        if (!android.content.pm.PackageParser.sCompatibilityModeEnabled) {
            ai.disableCompatibilityMode();
        }
        ai.flags |= (android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(state.stopped, android.content.pm.ApplicationInfo.FLAG_STOPPED) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(state.installed, android.content.pm.ApplicationInfo.FLAG_INSTALLED)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(state.suspended, android.content.pm.ApplicationInfo.FLAG_SUSPENDED);
        ai.privateFlags |= (android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(state.instantApp, android.content.pm.ApplicationInfo.PRIVATE_FLAG_INSTANT) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(state.virtualPreload, android.content.pm.ApplicationInfo.PRIVATE_FLAG_VIRTUAL_PRELOAD)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(state.hidden, android.content.pm.ApplicationInfo.PRIVATE_FLAG_HIDDEN);
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
        ai.resourceDirs = state.getAllOverlayPaths();
        return ai;
    }

    @android.annotation.Nullable
    public static android.content.pm.ActivityInfo generateActivityInfo(android.content.pm.parsing.ParsingPackageRead pkg, android.content.pm.parsing.component.ParsedActivity a, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags, android.content.pm.PackageUserState state, @android.annotation.Nullable
    android.content.pm.ApplicationInfo applicationInfo, int userId) {
        if (a == null)
            return null;

        if (!android.content.pm.parsing.PackageInfoWithoutStateUtils.checkUseInstalled(pkg, state, flags)) {
            return null;
        }
        if (applicationInfo == null) {
            applicationInfo = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateApplicationInfo(pkg, flags, state, userId);
        }
        if (applicationInfo == null) {
            return null;
        }
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateActivityInfoUnchecked(a, applicationInfo);
    }

    /**
     * This bypasses critical checks that are necessary for usage with data passed outside of
     * system server.
     *
     * Prefer {@link #generateActivityInfo(ParsingPackageRead, ParsedActivity, int,
     * PackageUserState, ApplicationInfo, int)}.
     */
    @android.annotation.NonNull
    public static android.content.pm.ActivityInfo generateActivityInfoUnchecked(@android.annotation.NonNull
    android.content.pm.parsing.component.ParsedActivity a, @android.annotation.NonNull
    android.content.pm.ApplicationInfo applicationInfo) {
        // Make shallow copies so we can store the metadata safely
        android.content.pm.ActivityInfo ai = new android.content.pm.ActivityInfo();
        android.content.pm.parsing.PackageInfoWithoutStateUtils.assignSharedFieldsForComponentInfo(ai, a);
        ai.targetActivity = a.getTargetActivity();
        ai.processName = a.getProcessName();
        ai.exported = a.isExported();
        ai.theme = a.getTheme();
        ai.uiOptions = a.getUiOptions();
        ai.parentActivityName = a.getParentActivityName();
        ai.permission = a.getPermission();
        ai.taskAffinity = a.getTaskAffinity();
        ai.flags = a.getFlags();
        ai.privateFlags = a.getPrivateFlags();
        ai.launchMode = a.getLaunchMode();
        ai.documentLaunchMode = a.getDocumentLaunchMode();
        ai.maxRecents = a.getMaxRecents();
        ai.configChanges = a.getConfigChanges();
        ai.softInputMode = a.getSoftInputMode();
        ai.persistableMode = a.getPersistableMode();
        ai.lockTaskLaunchMode = a.getLockTaskLaunchMode();
        ai.screenOrientation = a.getScreenOrientation();
        ai.resizeMode = a.getResizeMode();
        java.lang.Float maxAspectRatio = a.getMaxAspectRatio();
        ai.maxAspectRatio = (maxAspectRatio != null) ? maxAspectRatio : 0.0F;
        java.lang.Float minAspectRatio = a.getMinAspectRatio();
        ai.minAspectRatio = (minAspectRatio != null) ? minAspectRatio : 0.0F;
        ai.supportsSizeChanges = a.getSupportsSizeChanges();
        ai.requestedVrComponent = a.getRequestedVrComponent();
        ai.rotationAnimation = a.getRotationAnimation();
        ai.colorMode = a.getColorMode();
        ai.windowLayout = a.getWindowLayout();
        ai.metaData = a.getMetaData();
        ai.applicationInfo = applicationInfo;
        return ai;
    }

    @android.annotation.Nullable
    public static android.content.pm.ActivityInfo generateActivityInfo(android.content.pm.parsing.ParsingPackageRead pkg, android.content.pm.parsing.component.ParsedActivity a, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags, android.content.pm.PackageUserState state, int userId) {
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateActivityInfo(pkg, a, flags, state, null, userId);
    }

    @android.annotation.Nullable
    public static android.content.pm.ServiceInfo generateServiceInfo(android.content.pm.parsing.ParsingPackageRead pkg, android.content.pm.parsing.component.ParsedService s, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags, android.content.pm.PackageUserState state, @android.annotation.Nullable
    android.content.pm.ApplicationInfo applicationInfo, int userId) {
        if (s == null)
            return null;

        if (!android.content.pm.parsing.PackageInfoWithoutStateUtils.checkUseInstalled(pkg, state, flags)) {
            return null;
        }
        if (applicationInfo == null) {
            applicationInfo = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateApplicationInfo(pkg, flags, state, userId);
        }
        if (applicationInfo == null) {
            return null;
        }
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateServiceInfoUnchecked(s, applicationInfo);
    }

    /**
     * This bypasses critical checks that are necessary for usage with data passed outside of
     * system server.
     *
     * Prefer {@link #generateServiceInfo(ParsingPackageRead, ParsedService, int, PackageUserState,
     * ApplicationInfo, int)}.
     */
    @android.annotation.NonNull
    public static android.content.pm.ServiceInfo generateServiceInfoUnchecked(@android.annotation.NonNull
    android.content.pm.parsing.component.ParsedService s, @android.annotation.NonNull
    android.content.pm.ApplicationInfo applicationInfo) {
        // Make shallow copies so we can store the metadata safely
        android.content.pm.ServiceInfo si = new android.content.pm.ServiceInfo();
        android.content.pm.parsing.PackageInfoWithoutStateUtils.assignSharedFieldsForComponentInfo(si, s);
        si.exported = s.isExported();
        si.flags = s.getFlags();
        si.metaData = s.getMetaData();
        si.permission = s.getPermission();
        si.processName = s.getProcessName();
        si.mForegroundServiceType = s.getForegroundServiceType();
        si.applicationInfo = applicationInfo;
        return si;
    }

    @android.annotation.Nullable
    public static android.content.pm.ServiceInfo generateServiceInfo(android.content.pm.parsing.ParsingPackageRead pkg, android.content.pm.parsing.component.ParsedService s, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags, android.content.pm.PackageUserState state, int userId) {
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateServiceInfo(pkg, s, flags, state, null, userId);
    }

    @android.annotation.Nullable
    public static android.content.pm.ProviderInfo generateProviderInfo(android.content.pm.parsing.ParsingPackageRead pkg, android.content.pm.parsing.component.ParsedProvider p, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags, android.content.pm.PackageUserState state, @android.annotation.Nullable
    android.content.pm.ApplicationInfo applicationInfo, int userId) {
        if (p == null)
            return null;

        if (!android.content.pm.parsing.PackageInfoWithoutStateUtils.checkUseInstalled(pkg, state, flags)) {
            return null;
        }
        if (applicationInfo == null) {
            applicationInfo = android.content.pm.parsing.PackageInfoWithoutStateUtils.generateApplicationInfo(pkg, flags, state, userId);
        }
        if (applicationInfo == null) {
            return null;
        }
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateProviderInfoUnchecked(p, flags, applicationInfo);
    }

    /**
     * This bypasses critical checks that are necessary for usage with data passed outside of
     * system server.
     *
     * Prefer {@link #generateProviderInfo(ParsingPackageRead, ParsedProvider, int,
     * PackageUserState, ApplicationInfo, int)}.
     */
    @android.annotation.NonNull
    public static android.content.pm.ProviderInfo generateProviderInfoUnchecked(@android.annotation.NonNull
    android.content.pm.parsing.component.ParsedProvider p, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags, @android.annotation.NonNull
    android.content.pm.ApplicationInfo applicationInfo) {
        // Make shallow copies so we can store the metadata safely
        android.content.pm.ProviderInfo pi = new android.content.pm.ProviderInfo();
        android.content.pm.parsing.PackageInfoWithoutStateUtils.assignSharedFieldsForComponentInfo(pi, p);
        pi.exported = p.isExported();
        pi.flags = p.getFlags();
        pi.processName = p.getProcessName();
        pi.authority = p.getAuthority();
        pi.isSyncable = p.isSyncable();
        pi.readPermission = p.getReadPermission();
        pi.writePermission = p.getWritePermission();
        pi.grantUriPermissions = p.isGrantUriPermissions();
        pi.forceUriPermissions = p.isForceUriPermissions();
        pi.multiprocess = p.isMultiProcess();
        pi.initOrder = p.getInitOrder();
        pi.uriPermissionPatterns = p.getUriPermissionPatterns();
        pi.pathPermissions = p.getPathPermissions();
        pi.metaData = p.getMetaData();
        if ((flags & android.content.pm.PackageManager.GET_URI_PERMISSION_PATTERNS) == 0) {
            pi.uriPermissionPatterns = null;
        }
        pi.applicationInfo = applicationInfo;
        return pi;
    }

    @android.annotation.Nullable
    public static android.content.pm.ProviderInfo generateProviderInfo(android.content.pm.parsing.ParsingPackageRead pkg, android.content.pm.parsing.component.ParsedProvider p, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags, android.content.pm.PackageUserState state, int userId) {
        return android.content.pm.parsing.PackageInfoWithoutStateUtils.generateProviderInfo(pkg, p, flags, state, null, userId);
    }

    @android.annotation.Nullable
    public static android.content.pm.InstrumentationInfo generateInstrumentationInfo(android.content.pm.parsing.component.ParsedInstrumentation i, android.content.pm.parsing.ParsingPackageRead pkg, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags, int userId) {
        if (i == null)
            return null;

        android.content.pm.InstrumentationInfo ii = new android.content.pm.InstrumentationInfo();
        android.content.pm.parsing.PackageInfoWithoutStateUtils.assignSharedFieldsForPackageItemInfo(ii, i);
        ii.targetPackage = i.getTargetPackage();
        ii.targetProcesses = i.getTargetProcesses();
        ii.handleProfiling = i.isHandleProfiling();
        ii.functionalTest = i.isFunctionalTest();
        ii.sourceDir = pkg.getBaseCodePath();
        ii.publicSourceDir = pkg.getBaseCodePath();
        ii.splitNames = pkg.getSplitNames();
        ii.splitSourceDirs = pkg.getSplitCodePaths();
        ii.splitPublicSourceDirs = pkg.getSplitCodePaths();
        ii.splitDependencies = pkg.getSplitDependencies();
        ii.dataDir = android.content.pm.parsing.PackageInfoWithoutStateUtils.getDataDir(pkg, userId).getAbsolutePath();
        ii.deviceProtectedDataDir = android.content.pm.parsing.PackageInfoWithoutStateUtils.getDeviceProtectedDataDir(pkg, userId).getAbsolutePath();
        ii.credentialProtectedDataDir = android.content.pm.parsing.PackageInfoWithoutStateUtils.getCredentialProtectedDataDir(pkg, userId).getAbsolutePath();
        if ((flags & android.content.pm.PackageManager.GET_META_DATA) == 0) {
            return ii;
        }
        ii.metaData = i.getMetaData();
        return ii;
    }

    @android.annotation.Nullable
    public static android.content.pm.PermissionInfo generatePermissionInfo(android.content.pm.parsing.component.ParsedPermission p, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags) {
        if (p == null)
            return null;

        android.content.pm.PermissionInfo pi = new android.content.pm.PermissionInfo(p.getBackgroundPermission());
        android.content.pm.parsing.PackageInfoWithoutStateUtils.assignSharedFieldsForPackageItemInfo(pi, p);
        pi.group = p.getGroup();
        pi.requestRes = p.getRequestRes();
        pi.protectionLevel = p.getProtectionLevel();
        pi.descriptionRes = p.getDescriptionRes();
        pi.flags = p.getFlags();
        if ((flags & android.content.pm.PackageManager.GET_META_DATA) == 0) {
            return pi;
        }
        pi.metaData = p.getMetaData();
        return pi;
    }

    @android.annotation.Nullable
    public static android.content.pm.PermissionGroupInfo generatePermissionGroupInfo(android.content.pm.parsing.component.ParsedPermissionGroup pg, @android.content.pm.PackageManager.ComponentInfoFlags
    int flags) {
        if (pg == null)
            return null;

        android.content.pm.PermissionGroupInfo pgi = new android.content.pm.PermissionGroupInfo(pg.getRequestDetailResourceId(), pg.getBackgroundRequestResourceId(), pg.getBackgroundRequestDetailResourceId());
        android.content.pm.parsing.PackageInfoWithoutStateUtils.assignSharedFieldsForPackageItemInfo(pgi, pg);
        pgi.descriptionRes = pg.getDescriptionRes();
        pgi.priority = pg.getPriority();
        pgi.requestRes = pg.getRequestRes();
        pgi.flags = pg.getFlags();
        if ((flags & android.content.pm.PackageManager.GET_META_DATA) == 0) {
            return pgi;
        }
        pgi.metaData = pg.getMetaData();
        return pgi;
    }

    private static void assignSharedFieldsForComponentInfo(@android.annotation.NonNull
    android.content.pm.ComponentInfo componentInfo, @android.annotation.NonNull
    android.content.pm.parsing.component.ParsedMainComponent mainComponent) {
        android.content.pm.parsing.PackageInfoWithoutStateUtils.assignSharedFieldsForPackageItemInfo(componentInfo, mainComponent);
        componentInfo.descriptionRes = mainComponent.getDescriptionRes();
        componentInfo.directBootAware = mainComponent.isDirectBootAware();
        componentInfo.enabled = mainComponent.isEnabled();
        componentInfo.splitName = mainComponent.getSplitName();
    }

    private static void assignSharedFieldsForPackageItemInfo(@android.annotation.NonNull
    android.content.pm.PackageItemInfo packageItemInfo, @android.annotation.NonNull
    android.content.pm.parsing.component.ParsedComponent component) {
        packageItemInfo.nonLocalizedLabel = android.content.pm.parsing.component.ComponentParseUtils.getNonLocalizedLabel(component);
        packageItemInfo.icon = android.content.pm.parsing.component.ComponentParseUtils.getIcon(component);
        packageItemInfo.banner = component.getBanner();
        packageItemInfo.labelRes = component.getLabelRes();
        packageItemInfo.logo = component.getLogo();
        packageItemInfo.name = component.getName();
        packageItemInfo.packageName = component.getPackageName();
    }

    @android.annotation.CheckResult
    private static int flag(boolean hasFlag, int flag) {
        if (hasFlag) {
            return flag;
        } else {
            return 0;
        }
    }

    /**
     *
     *
     * @see ApplicationInfo#flags
     */
    public static int appInfoFlags(android.content.pm.parsing.ParsingPackageRead pkg) {
        // @formatter:off
        return (((((((((((((((((((((((android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isExternalStorage(), android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isBaseHardwareAccelerated(), android.content.pm.ApplicationInfo.FLAG_HARDWARE_ACCELERATED)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isAllowBackup(), android.content.pm.ApplicationInfo.FLAG_ALLOW_BACKUP)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isKillAfterRestore(), android.content.pm.ApplicationInfo.FLAG_KILL_AFTER_RESTORE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isRestoreAnyVersion(), android.content.pm.ApplicationInfo.FLAG_RESTORE_ANY_VERSION)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isFullBackupOnly(), android.content.pm.ApplicationInfo.FLAG_FULL_BACKUP_ONLY)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isPersistent(), android.content.pm.ApplicationInfo.FLAG_PERSISTENT)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isDebuggable(), android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isVmSafeMode(), android.content.pm.ApplicationInfo.FLAG_VM_SAFE_MODE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isHasCode(), android.content.pm.ApplicationInfo.FLAG_HAS_CODE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isAllowTaskReparenting(), android.content.pm.ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isAllowClearUserData(), android.content.pm.ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isLargeHeap(), android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isUsesCleartextTraffic(), android.content.pm.ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isSupportsRtl(), android.content.pm.ApplicationInfo.FLAG_SUPPORTS_RTL)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isTestOnly(), android.content.pm.ApplicationInfo.FLAG_TEST_ONLY)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isMultiArch(), android.content.pm.ApplicationInfo.FLAG_MULTIARCH)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isExtractNativeLibs(), android.content.pm.ApplicationInfo.FLAG_EXTRACT_NATIVE_LIBS)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isGame(), android.content.pm.ApplicationInfo.FLAG_IS_GAME)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isSupportsSmallScreens(), android.content.pm.ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isSupportsNormalScreens(), android.content.pm.ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isSupportsLargeScreens(), android.content.pm.ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isSupportsExtraLargeScreens(), android.content.pm.ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isResizeable(), android.content.pm.ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isAnyDensity(), android.content.pm.ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES);
        // @formatter:on
    }

    /**
     *
     *
     * @see ApplicationInfo#privateFlags
     */
    public static int appInfoPrivateFlags(android.content.pm.parsing.ParsingPackageRead pkg) {
        // @formatter:off
        int privateFlags = ((((((((((((((((android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isStaticSharedLibrary(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_STATIC_SHARED_LIBRARY) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isOverlay(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_IS_RESOURCE_OVERLAY)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isIsolatedSplitLoading(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_ISOLATED_SPLIT_LOADING)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isHasDomainUrls(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_HAS_DOMAIN_URLS)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isProfileableByShell(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_PROFILEABLE_BY_SHELL)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isBackupInForeground(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_BACKUP_IN_FOREGROUND)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isUseEmbeddedDex(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_USE_EMBEDDED_DEX)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isDefaultToDeviceProtectedStorage(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_DEFAULT_TO_DEVICE_PROTECTED_STORAGE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isDirectBootAware(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_DIRECT_BOOT_AWARE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isPartiallyDirectBootAware(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_PARTIALLY_DIRECT_BOOT_AWARE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isAllowClearUserDataOnFailedRestore(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_ALLOW_CLEAR_USER_DATA_ON_FAILED_RESTORE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isAllowAudioPlaybackCapture(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_ALLOW_AUDIO_PLAYBACK_CAPTURE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isRequestLegacyExternalStorage(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_REQUEST_LEGACY_EXTERNAL_STORAGE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isUsesNonSdkApi(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_USES_NON_SDK_API)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isHasFragileUserData(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_HAS_FRAGILE_USER_DATA)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isCantSaveState(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_CANT_SAVE_STATE)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isResizeableActivityViaSdkVersion(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION)) | android.content.pm.parsing.PackageInfoWithoutStateUtils.flag(pkg.isAllowNativeHeapPointerTagging(), android.content.pm.ApplicationInfo.PRIVATE_FLAG_ALLOW_NATIVE_HEAP_POINTER_TAGGING);
        // @formatter:on
        java.lang.Boolean resizeableActivity = pkg.getResizeableActivity();
        if (resizeableActivity != null) {
            if (resizeableActivity) {
                privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE;
            } else {
                privateFlags |= android.content.pm.ApplicationInfo.PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_UNRESIZEABLE;
            }
        }
        return privateFlags;
    }

    private static boolean checkUseInstalled(android.content.pm.parsing.ParsingPackageRead pkg, android.content.pm.PackageUserState state, @android.content.pm.PackageManager.PackageInfoFlags
    int flags) {
        // If available for the target user
        return state.isAvailable(flags);
    }

    @android.annotation.NonNull
    public static java.io.File getDataDir(android.content.pm.parsing.ParsingPackageRead pkg, int userId) {
        if ("android".equals(pkg.getPackageName())) {
            return android.os.Environment.getDataSystemDirectory();
        }
        if (pkg.isDefaultToDeviceProtectedStorage() && android.content.pm.PackageManager.APPLY_DEFAULT_TO_DEVICE_PROTECTED_STORAGE) {
            return android.content.pm.parsing.PackageInfoWithoutStateUtils.getDeviceProtectedDataDir(pkg, userId);
        } else {
            return android.content.pm.parsing.PackageInfoWithoutStateUtils.getCredentialProtectedDataDir(pkg, userId);
        }
    }

    @android.annotation.NonNull
    public static java.io.File getDeviceProtectedDataDir(android.content.pm.parsing.ParsingPackageRead pkg, int userId) {
        return android.os.Environment.getDataUserDePackageDirectory(pkg.getVolumeUuid(), userId, pkg.getPackageName());
    }

    @android.annotation.NonNull
    public static java.io.File getCredentialProtectedDataDir(android.content.pm.parsing.ParsingPackageRead pkg, int userId) {
        return android.os.Environment.getDataUserCePackageDirectory(pkg.getVolumeUuid(), userId, pkg.getPackageName());
    }
}

